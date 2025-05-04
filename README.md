# KUTowerDefense
Repository for the COMP302 term project

In order to run this in eclipse;
1) You have to download JavaFX SDK 21.0.7 on this [link](https://gluonhq.com/products/javafx/).<br />
2) Go to Settings/Preferences.<br />
3) Go to Java -> BuildPath -> UserLibraries.
4) Click New Button.<br />
   <img width="600" alt="image" src="https://github.com/user-attachments/assets/399cd17c-569e-464f-a5c0-4661b78927a6" />
5) Give your Library a name.<br />
6) Click Add External JARs.<br />
   <img width="500" alt="image" src="https://github.com/user-attachments/assets/e2f059cd-6eec-4e33-bb71-d7f614feb0d1" />
   <br />
7) Select all .jar files in javafx-sdk-21.0.7/lib.<br />
   <img width="400" alt="image" src="https://github.com/user-attachments/assets/7a8fc3ac-0f17-43e9-be23-85a315effcb8" />
   <br />
8) Then Click Apply and Close.<br />
9) Right Click on the project and click properties.<br />
<img width="400" alt="image" src="https://github.com/user-attachments/assets/f0a0bea1-6bf7-4635-9b75-19ca8f5f1ae7" />
<br />
10) Go to Java Build Path and go to Libraries section and click on ModulePath and Click Add Library. <br />
<img width="800" alt="a" src="https://github.com/user-attachments/assets/e052fc58-e255-41f0-91b8-3e49a7dfed15" />
<br />
11) Click User Library and click next.<br />
<img width="500" alt="b" src="https://github.com/user-attachments/assets/cc5290f2-6ac5-4ea8-b19d-bc3c3d738aec" />
<br />
12) Then Click on the library(in the photo it is javafx) that you named and click Finish.<br />
<img width="500" alt="c" src="https://github.com/user-attachments/assets/23d21771-e1f4-4187-834d-94b2c178d8b4" />
<br />
13) Right Click on the project and Hover your mouse to Run as and Click Run configurations.<br />
<img width="700" alt="d" src="https://github.com/user-attachments/assets/483fea9f-88f1-4eb7-bcca-4582dc28e74d" />
<br />
14)Then under the Java Application Click on the main class (in our case it is KuTowerDefenseA) then click on arguments and paste this into VM Arguments: <br />
Linux/Mac: --module-path /path/to/javafx-sdk-21.0.7/lib --add-modules javafx.controls,javafx.fxml
Windows: --module-path "\path\to\javafx-sdk-21.0.7\lib" --add-modules javafx.controls,javafx.fxml
For mac you have to turn off the section "Use the -XstartOnFirstThread argument when launching with SWT"<br />
<img width="800" alt="damn" src="https://github.com/user-attachments/assets/01900101-1031-4b04-baa8-4691a46226a5" />




