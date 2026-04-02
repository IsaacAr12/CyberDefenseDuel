@echo off
echo Iniciando servidor...

"C:\Program Files\Java\jdk-25.0.2\bin\java.exe" ^
-cp "bin;.\lib\gson-2.10.1.jar" ^
server.Server

pause