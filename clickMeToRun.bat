
javac -d classes theJavaFiles\*.java

cd classes

@echo off
title Server application
echo - - - - - - - - - - - - - - - 
echo Welcome to Jasons server setup
echo - - - - - - - - - - - - - - - 
echo.
pause
cls
echo Are you running a server?
echo Y/N?
set /p input=Answer: 
if %input%==Y goto server
if %input%==N goto client
:server
cls
echo Running server setup
java theJavaFiles.ChatRoom

:client
cls
echo Running client setup
java theJavaFiles.Client

pause