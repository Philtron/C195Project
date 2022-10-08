Title: 
C195 Scheduler Version: 2.5
Purpose: 
Provide GUI appointment scheduling capabilities working with a MYSQL database. 

Author: 
Philip Sauer
Student ID: 001156531
Psauer1@WGU.edu
(505) 239-3041

IDE Used to develop: Intellij IDEA Community Edition 2020.1.2 x64
JDK Used: JAVA SE 17.0.4
JAVAFX Version Used: 17.0.1

How to run :
Make sure you have mysql-connector-java-8.0.25.jar and javafx-sdk-17.0.1 on your machine. 
Unzip the C195Project folder. In Intellij go to File > Open. Navigate to the unzipped C195Project folder and click OK. 
After the project finishes initial indexing and setup, go to File > Project Structure and click on Libraries. Click the 
plus (+) sign in the middle pane and from the new project library popup, click Java. Navigate to your javafx directory. 
You can control click each jar, add the javafx jars in your lib folder. Confirm until the libraries are added. 
Now again click the + sign and again choose Java. Now navigate to where your JDBC driver, mysql-connector-java-8.0.25.jar,
 is located. Confirm until the driver is added. 
From the main file, right click and select run Main.main(). It won’t run, you should get a message that the project is 
missing javafx elements.  Now go to Run > Edit Configurations. In the new window, click on Modify options and choose to 
add VM options. 
In the text field for VM options enter: --module-path %PATH_TO_FX%  --add-modules javafx.fxml,javafx.controls,javafx.graphics 
where PATH_TO_FX is the location of your javafx lib folder. 
You should now be able to run the program by right clicking and choosing run Main.main() or by clicking the green run 
triangle at the top of the IDE. 

JAVADOCS folder is located in the C195Project/Docs folder. 

Additional Report: 
The additional report functionality is the ability to check appointments filtered by the user who scheduled them. 

