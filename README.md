# contactDirectory
**Main requirements:**
- Apache Tomcat version 8 and higher
- MySQL Server version 6 and higher
- JDK version 1.8

To launch the application you should:
 + paste WebSide.war in the directory
  _**webapps**_ on Tomcat;
 + in server.xml
configuration file add the following tag:

  <Context docBase="WebSide" path="" debug="0"
        reloadable="true"/>
  
 + in the directory for storing photos add the 
 no_avatar.png file which is located in WebSide 
 resources directory