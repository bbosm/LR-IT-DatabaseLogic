<<<<<<< HEAD
﻿# mydb  
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
=======
﻿# mydb
Кузенко 
https://magistrs2016.github.io/
  
# Installations:  
Download & Install JRE 1.8, JDK 1.8, Git  
Download & Install Intelij IDEA Community, Eclipse Jee Oxygen (later Tomcat 8)  

# Laba - Branches:  
2, 3, 8, 11, 14) master = core, web clients  
4) RMIServer, RMIClient  
5) IIOPServer, IIOPClient  
7) IIOPServer, TORBAClient  
9) TORBAServer, TORBAClient  
8) restserver  
11, 14) webservlet  
  
# Init enviroment, lab 2-3  
Intelij IDEA start window: Configure > Project Defaults > Project Structure - point SDK to JRE  
Open - folder with project, press OK, press OK  
Select "Migrate to Gradle wrapper and sync project"  
If gradle sync failed - close project, delete .idea/modules, .idea/modules.xml files and open project again, Import changes  
View - Tool Windows - Gradle - Tasks:  
  application - run = run app  
  verification - test = run tests  


>>>>>>> master
