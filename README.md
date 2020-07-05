guacamole-auth-singlerdp
========================

This extension makes [Guacamole](https://guacamole.incubator.apache.org/) 
behave as a web front-end for a single RDP server. Only the RDP server 
prompts for user credentials; the Guacamole app itself does not.



Building the extension
======================

Use [Maven](http://maven.apache.org/). Go into the root directory of 
this repository (where `pom.xml` is located) and run:
```
mvn package
```

If there are no build errors, you'll find the jar file in the `target` 
directory.


Installing the extension
========================

Copy the jar to `GUACAMOLE_HOME/extensions/`. (For tomcat on CentOS 7, 
`GUACAMOLE_HOME` will be `/usr/share/tomcat/.guacamole/`.)

Edit configuration in `GUACAMOLE_HOME/guacamole.properties`:
```
singlerdp-title=Single RDP
singlerdp-hostname=127.0.0.1
singlerdp-port=3389
```

These three lines are all you need in `guacamole.properties`. Change the 
hostname to the IP or hostname of the target RDP server and the title to 
what you want to appear in the title bar. You'll only need to change the 
port if your RDP server is using a non-standard port.

Optional Settings
-----------------

The following additional settings can be configured in `guacamole.properties`
but are not required.


Setting                    | Default 
---------------------------|--------------
singlerdp-ignore-cert      | false


Updating for new guacamole versions
===================================

Edit the guacamole version in the dependencies section of `pom.xml` and in 
`src/main/resources/guac-manifest.json`, then build the jar and install it.


