package com.deploy.demo.executor;

import java.util.concurrent.FutureTask;

public class FutureCancellableTask extends FutureTask {

    private CancellableCallable task;

    public FutureCancellableTask(CancellableCallable c) {
        super(c);
        this.task = c;
    }

    public CancellableCallable getTask() {
        return task;
    }

    public boolean cancel(boolean mayInterruptIfRunning) {
        // This method is invoked only when cancellation is explicitly requested. We have
        // additional code in the custom executor to handle executor shutdown.

        // First invoke the cancel method to unblock the task if necessary
        // and help it better respond to the actual interruption, which will follow.
        try {
            task.cancel();
        } catch (Throwable t) {
            // Not doing anything in this PoC, but it might
            // be a good idea to log this exception.
        }

        // Now we invoke the parent class, which will
        // ultimately call Thread#interrupt().
        return super.cancel(mayInterruptIfRunning);
    }
}