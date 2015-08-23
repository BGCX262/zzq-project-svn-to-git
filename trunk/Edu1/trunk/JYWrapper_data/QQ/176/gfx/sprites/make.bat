
@echo off
setlocal enabledelayedexpansion

echo ...BUILDING GFX...

REM DIRECTORY, WHERE DATA THAT NEED TO BE PACKED, MUST BE COPIED
set DIR_TO_PACK=%1

REM DIRECTORY, WHERE DATA WHICH DON'T NEED TO BE PACKED, MUST BE COPIED
set DIR_NOT_TO_PACKED=%2

REM DIRECTORY, WHERE TO SOURCE FILES MUST BE COPIED
set DIR_SOURCE=%3

if "%BBX%"=="1" (
	copy /y splash_bbx.sprite splash.sprite
	copy /y logo_BBX.sprite logo.sprite
)
	rem spalsh
	rem copy /y splash_%LANG%.sprite splash.sprite
echo ........%CD%
rem	--- converting SPRITES to BSPRITES
	"%TOOLS_PATH%\AuroraGT\AuroraGT.exe" "export.sprcmd"

rem --- copy to output
	move /y *.gif "%DIR_TO_PACK%" > NUL
	copy /y *.java "%DIR_SOURCE%" > NUL

:end
endlocal