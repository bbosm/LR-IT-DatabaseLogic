# mydb
Кузенко  
https://magistrs2016.github.io/  
  
# Installations:  
Download & Install JRE 1.8, JDK 1.8, Git  
Download & Install Intelij IDEA Community, Eclipse Jee Oxygen (Tomcat 8.0 from Eclipse)  
  
# Server (Eclipse)  
Open IDE and create workspace  
File - New - Maven Project  
Press Next, then choose maven-archetype-webapp, press Next  
Group Id and Artifact Id = mydb, Finish  
Project properties - Java Build Path - Source: remove anothers, leave only mydb/src/main/java, Apply  
Project properties - Project Facets - Runtimes: check (or add new) Apache Tomcat 8.0, Apply and Close  
Copy repository (whole mydb folder) into workspace, with replace  
Project: right click - refresh  
Markers - Java Problems - right click on first one - Quick Fix - Change JRE to 1.7 - Finish  
Markers - Faceted Project Problem - right click on first one - Quick Fix - change to Java 1.7 - Finish  
Servers - create Tomcat 8.0 server (skip if server exists), Next, add mydb to Configured, Finish    
Project - Run As - Run on Server  
