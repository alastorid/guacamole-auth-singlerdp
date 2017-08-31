Example setup
=============

A quick and dirty setup of Guacamole with guac-auth-singlerdp on CentOS 7.


**Disclaimer:** The setup shown here only covers what's necessary to make
everything work. It doesn't cover making everything work *nicely* or
anything resembling proper security and should only be run on your own 
private, trusted network.

Additionally, for the sake of simplicity, directions are "hard-coded" for
Guacamole 0.9.12, the version provided in the EPEL packages at the time of
this writing (2017-08-31). Wherever you have to explicitly use a version 
number, make sure everything matches.

Most of the commands given require elevated permissions; i.e. if you're not
logged in as `root`, then you'll need to use `sudo` *a lot*. The few commands
that don't require elevated permissions will say so.

We're going to assume you're starting with a fresh minimal installation of 
CentOS 7, a working network connection, and internet access. At the end, 
your system should be able to serve an xfce desktop over http.



Begin
-----

Enable the EPEL repository and update the system.
```bash
yum install epel-release
yum update
```



Part 1: Setting up the RDP server
---------------------------------

*If you already have another RDP server setup that doesn't require NLA, then
you can skip to Part 2 and use that server's IP or hostname in place of the
localhost address in the `guacamole-auth-singlerdp` configuration.*

Install the xfce desktop and xrdp server.
```bash
yum groups install xfce
yum install xrdp
```

Comment out the `Xvnc` section in `/etc/xrdp/xrdp.ini` so that `Xorg` 
is the only session option. (`Xorg` sessions support resizing, so if you
have an existing session, you should always end up reconnecting to the
existing session.)
```bash
sed -i '/^\[Xvnc\]/,/^\ *$/s/^/\#/g' /etc/xrdp/xrdp.ini
```

Enable and start the xrdp service.
```bash
systemctl enable xrdp
systemctl start xrdp
```

Create an executable `.xsession` file in your home directory to specify an
xfce session. (This is a per-user step and doesn't require elevated 
permissions. Do this for every user that will access the remote desktop.)
```bash
echo "xfce4-session" > ~/.xsession
chmod +x ~/.xsession
```

At this point, you'll probably want to test to make sure that the remote 
desktop is working. So let's add a temporary firewall rule to accept remote 
desktop connections. (This rule will be in effect until you reboot or reload
firewall rules.)
```bash
firewall-cmd --add-port=3389/tcp
```



Part 2: Setting up Guacamole
----------------------------

Install system packages for guacamole and tomcat. Enable and start the 
`guacd` and `tomcat` services.
```bash
yum install tomcat guacd libguac libguac-client-rdp
systemctl enable guacd
systemctl start guacd
systemctl enable tomcat
systemctl start tomcat
```

Download the Guacamole web application, `guacamole-0.9.12-incubating.war`, 
from https://guacamole.incubator.apache.org/releases/.

Download or build the guac-auth-singlerdp extension 
`guac-auth-singlerdp-0.9.12.jar`. 

Copy the `.war` file to tomcat's webapps directory.
```bash
cp guacamole-0.9.12-incubating.war /usr/share/tomcat/webapps/
```

Install the guacamole-auth-singlerdp extension.
```bash
mkdir -p /usr/share/tomcat/.guacamole/extensions
cp guacamole-auth-singlerdp-0.9.12.jar /usr/share/tomcat/.guacamole/extensions
```

Configure guacamole-auth-singlerdp. Create the file 
`/usr/share/tomcat/.guacamole/guacamole.properties` and add three lines
defining the title, hostname (or IP), and port for the RDP connection.
```bash
cat << EOF > /usr/share/tomcat/.guacamole/guacamole.properties
singlerdp-title=Single RDP
singlerdp-hostname=127.0.0.1
singlerdp-port=3389
EOF
```

Restart tomcat to deploy the Guacamole web application.
```bash
systemctl restart tomcat
```

Add a permanent firewall rule to allow http connections to tomcat.
```bash
firewall-cmd --permanent --add-port=8080/tcp
firewall-cmd --reload
```



Done
----
You can now direct your browser to a URL like this to access your RDP server:
http://*yourserver*:8080/guacamole-0.9.12-incubating


