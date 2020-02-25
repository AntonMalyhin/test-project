# test-project

To build the project, you will need to download and unpack the latest (or recent) version of Maven (https://maven.apache.org/download.cgi) and put the mvn command on your path. Then, you will need to install a Java 11 JDK, and make sure you can run java from the command line. Also you need a Docker installation to verify the image was built correctly.

To build the project, you have to run the next Maven comand from the directory where your project's root pom.xml placed is:
```
mvn clean package
```
To build the Docker image for web application, you have to invoke the next commands from the project's root folder:
```
cd ./test-project-server
mvn  dockerfile:build -Ddockerfile.repository=some-repo/test-project-server -Ddockerfile.tag=SNAPSHOT
```
Then you can verify that image was created succefully:
```
docker image list
```
You should see the created image in the list:
```
REPOSITORY                      TAG                    IMAGE ID            CREATED             SIZE
some-repo/test-project-server   development-SNAPSHOT   edd8d91ee205        8 hours ago         237MB
```
Then you can try to run the application:
```
docker run -p 8081:8081 some-repo/test-project-server:development-SNAPSHOT
```
If everything was fine you will see the next log of the application start:
```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v2.2.0.RELEASE)

2020-02-25 13:27:59.557  INFO 6 --- [           main] com.test.Application                     : Starting Application vdevelopment-SNAPSHOT on 9344dcfc6f45 with PID 6 (/app.jar started by root in /)
2020-02-25 13:27:59.561  INFO 6 --- [           main] com.test.Application                     : The following profiles are active: default
2020-02-25 13:28:01.444  INFO 6 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port(s): 8080 (http)
2020-02-25 13:28:01.457  INFO 6 --- [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2020-02-25 13:28:01.458  INFO 6 --- [           main] org.apache.catalina.core.StandardEngine  : Starting Servlet engine: [Apache Tomcat/9.0.27]
2020-02-25 13:28:01.541  INFO 6 --- [           main] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2020-02-25 13:28:01.541  INFO 6 --- [           main] o.s.web.context.ContextLoader            : Root WebApplicationContext: initialization completed in 1845 ms
2020-02-25 13:28:02.017 DEBUG 6 --- [           main] o.s.w.f.CommonsRequestLoggingFilter      : Filter 'logFilter' configured for use
2020-02-25 13:28:02.476  INFO 6 --- [           main] o.s.b.a.e.web.EndpointLinksResolver      : Exposing 2 endpoint(s) beneath base path '/actuator'
2020-02-25 13:28:02.566  INFO 6 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http) with context path ''
2020-02-25 13:28:02.572  INFO 6 --- [           main] com.test.Application                     : Started Application in 4.041 seconds (JVM running for 4.739)
```
