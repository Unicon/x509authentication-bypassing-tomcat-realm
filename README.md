x509authentication-bypassing-tomcat-realm
========================================

This projects contains custom implementation of Apache Tomcat 7 Realm whos primary purpose is to bypass the X509 client certificate's Subject field comparison against pre-configured Tomcat's authentication sources.

This is useful in situations where web applications deployed in Tomcat have parts of their resources (URLs) protected by standard `security-constraint` mechanism with `CLIENT-CERT` login configuration that wish to just do the client-side X509 certificate authentication themselves or to delegate to an external authentication mechanism and not rely on the standard Tomcat's Realm to perform this step.

## Configuration

### Build

At the project's root directory from the command line simply execute `./gradlew` (or `gradlew.bat` for Windows OS)

### Install 

Copy `buld/libs/x509authentication-bypassing-tomcat-realm-1.0.jar` to `$CATALINA_HOME/lib` (where `$CATALINA_HOME` is the root directory of Tomcat 7 installation)

### Configure Realm

In `$CATALINA_HOME/conf/server.xml` add the following inside the `Engine` configuration element:

```xml
<Realm className="org.apache.catalina.realm.CombinedRealm">      
	<Realm className="net.unicon.tomcat7.realm.X509AuthenticationBypassingRealm"/>                              
</Realm>
```

### Configure container security constriant with `CLIENT-CERT` and `X509` role in application's web.xml for resources which need this Realm to be applied to. Example configuration:

```xml
<security-constraint>
        <web-resource-collection>
            <web-resource-name>ALL</web-resource-name>
            <url-pattern>/resource/that/needs/to/bypass/tomcat/x509/authentication</url-pattern>
        </web-resource-collection>
        <user-data-constraint>
            <transport-guarantee>CONFIDENTIAL</transport-guarantee>
        </user-data-constraint>
        <auth-constraint>
            <role-name>X509</role-name>
        </auth-constraint>
</security-constraint>
<login-config>
         <auth-method>CLIENT-CERT</auth-method>
</login-config>
```
