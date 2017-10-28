# mydb  
Кузенко  
https://magistrs2016.github.io/  

# To run:  
Download & Install JRE, JDK, Git  
Download & Install Eclipse for Java EE Oxygen  

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
Project - Run As - Run on Server  
