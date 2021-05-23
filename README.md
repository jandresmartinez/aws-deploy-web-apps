# aws-deploy-web-service

Spring boot project to expose an API to launch web applications. Calling this API it is possible to launch AWS instances
and instantiate them with one web application using "user_data" (https://docs.aws.amazon.com/es_es/AWSEC2/latest/UserGuide/user-data.html)
 field from AWS.  
 
 You can run the application simply by executing:
 
    git clone https://github.com/jandresmartinez/aws-deploy-web-apps.git
    cd aws-deploy-web-apps
    docker-compose up
 

## Shortcuts
* It is using only on demand t2 instances.
* One web app is deployed per instance
* AMI-ID should have Docker installed in the default login user
* REST API has no authentication method

## Open Swagger UI web interface

    http://rest-ip:rest-port/swagger-ui.html

## API methods

They are available in swagger interface, but the most important is to create one new app. Example 

    curl --location --request POST 'IP:PORT/web-apps/new-launch' \
    --header 'Content-Type: application/json' \
    --data-raw '{
   
    "name": "Joomla",
    "userData": "#! /bin/bash\ncurl -sSL https://raw.githubusercontent.com/bitnami/bitnami-docker-joomla/master/docker-compose.yml > docker-compose.yml\ndocker-compose up -d"
    }'

As noticed, user data is used to trigger command during the startup process. In the example, Joomla application is 
deployed using Bitnami source (https://github.com/bitnami/bitnami-docker-joomla#run-the-application-using-docker-compose)
 .More available options could be wordpress,mediawiki,etc. If needed, when triggering the web app, AWS ELB can be also
 created to have high availability, routing,etc. 
 
It is also possible to list web apps, delete one web apps, get its status and check aws cpu metrics. 


## Build Docker Image 

It is possible to build Docker image:

#### If you have maven installed:
    
    mvn clean install
#### With one container
    docker run -it --rm -v ${PWD}:/opt/maven -v ${HOME}/.m2:/root/.m2 -w /opt/maven maven:3.2-jdk-8 mvn clean install  

Finally it is possible to build the image:
   
    docker build -t jandresmartinez/bitnavm:latest .
    
## Run test 

It is possible to build Docker image:

#### If you have maven installed:
    
    mvn test
#### With one container
    docker run -it --rm -v ${PWD}:/opt/maven -v ${HOME}/.m2:/root/.m2 -w /opt/maven maven:3.2-jdk-8 mvn test
    
It is recommended to share local .m2 with the container to avoid issues.