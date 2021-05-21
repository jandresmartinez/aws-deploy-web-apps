package com.deploy.demo.executor;

import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

@Slf4j
public class CancellableExecutor extends ThreadPoolExecutor {

    private Set<CancellableCallable> cancellableCallables = Collections.synchronizedSet(new HashSet<>());

    public CancellableExecutor(int poolSize) {
        super(poolSize, poolSize, Long.MAX_VALUE, TimeUnit.DAYS, new LinkedBlockingQueue<>());
    }

    @Override
    protected RunnableFuture newTaskFor(Callable callable) {
        if (callable instanceof CancellableCallable) {
            return new FutureCancellableTask((CancellableCallable) callable);
        } else {
            return super.newTaskFor(callable);
        }
    }

    @Override
    public void beforeExecute(Thread t, Runnable r) {
        if (r instanceof FutureCancellableTask) {
            CancellableCallable c = ((FutureCancellableTask) r).getTask();
            log.info("Notified before task executes " + c);
            cancellableCallables.add(c);
        }
    }

    @Override
    public void afterExecute(Runnable r, Throwable t) {
        if (r instanceof FutureCancellableTask) {
            CancellableCallable c = ((FutureCancellableTask) r).getTask();
            log.info("Notified after task executes " + c);
            cancellableCallables.remove(c);
        }
    }

    @Override
    public List<Runnable> shutdownNow() {
        log.info("ShutdownNow called and " + cancellableCallables.size() + " tasks will be stopped");

        for (CancellableCallable cancellableCallable : cancellableCallables) {
            cancellableCallable.cancel();
        }

        return super.shutdownNow();
    }
}