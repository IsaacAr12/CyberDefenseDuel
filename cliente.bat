@echo off
echo Iniciando cliente...

"C:\Program Files\Java\jdk-25.0.2\bin\java.exe" ^
--module-path ".\lib\javafx-sdk-21\lib" ^
--add-modules javafx.controls,javafx.fxml ^
-Djava.library.path=.\lib\javafx-sdk-21\bin ^
-cp "bin;.\lib\gson-2.10.1.jar" ^
client.MainApp

pause