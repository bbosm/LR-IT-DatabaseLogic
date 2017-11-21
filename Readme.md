# mydb  
Кузенко https://magistrs2016.github.io/  
  
# Installations:  
Download & Install JRE 1.8, JDK 1.8, Git  
Download & Install Intelij IDEA Community 
  
# Init enviroment  
Intelij IDEA start window: Configure > Project Defaults > Project Structure - point SDK to JRE  
Open - folder with project, press OK, press OK  
Select "Migrate to Gradle wrapper and sync project"  
If gradle sync failed - close project, delete .idea/modules, .idea/modules.xml files and open project again  
Import changes  
View - Tool Windows - Gradle - Tasks: application - run = run app  
Run IDEA server app, it will crash, it's ok

Windows: add to Path: C:\Program Files\Java\jdk1.8.0_144\bin; C:\Program Files\Java\jre1.8.0_144\bin  
Open cmd and run command, don't close cmd: tnameserv -ORBInitialPort 8080  
Open another cmd in build/classes/main and run command: rmic -iiop transfer.ServerImpl  
Run IDEA server app, it will work ("RMI(IIOP) Server ready..." message)  
