@echo off
title Warlords of Hell - Initialize Server - Lauched at [%DATE%] - [%TIME%]
"C:\Program Files\java\jdk1.7.0\bin\java.exe" -Xmx512m -cp bin;deps/poi.jar;deps/mysql.jar;deps/mina.jar;deps/slf4j.jar;deps/slf4j-nop.jar;deps/jython.jar;log4j-1.2.15.jar; server.Server
pause