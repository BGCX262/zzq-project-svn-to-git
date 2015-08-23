@echo off
setlocal enabledelayedexpansion

@echo ...声音...

REM 用于加密后再进入jar的资源目录
set DIR_TO_PACK=%1

REM 不经加密处理，直接进入jar包的资源目录
set DIR_NOT_TO_PACK=%2

REM 资源打包过程生成的源代码目录
set DIR_SOURCE=%3

xcopy /Y /S *.*  "%DIR_NOT_TO_PACK%" > NUL
del "%DIR_NOT_TO_PACK%"\make.bat /F /Q > NULL
:end
endlocal