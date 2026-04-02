@echo off
setlocal enabledelayedexpansion

echo Compilando proyecto...

IF EXIST bin (
    rmdir /s /q bin
)

mkdir bin

set sources=

for /r src %%f in (*.java) do (
    set sources=!sources! "%%f"
)

"C:\Program Files\Java\jdk-25.0.2\bin\javac.exe" ^
--module-path ".\lib\javafx-sdk-21\lib" ^
--add-modules javafx.controls,javafx.fxml ^
-cp ".\lib\gson-2.10.1.jar" ^
-d bin ^
!sources!

echo.
echo Compilacion terminada
pause