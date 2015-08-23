
@echo off
setlocal enabledelayedexpansion

echo ...BUILDING GFX...

REM DIRECTORY, WHERE DATA THAT NEED TO BE PACKED, MUST BE COPIED
set DIR_TO_PACK=%1

REM DIRECTORY, WHERE DATA WHICH DON'T NEED TO BE PACKED, MUST BE COPIED
set DIR_NOT_TO_PACKED=%2

REM DIRECTORY, WHERE TO SOURCE FILES MUST BE COPIED
set DIR_SOURCE=%3

	rem spalsh
	rem copy /y splash_%LANG%.sprite splash.sprite
echo ........%CD%
rem	--- converting SPRITES to BSPRITES
	"%TOOLS_PATH%\AuroraGT\AuroraGT.exe" "export.sprcmd"

rem --- copy to output
	move /y *.bmp "%DIR_NOT_TO_PACKED%" > NUL
	move png\*.png "%DIR_NOT_TO_PACKED%" > NUL
	copy /y *.java "%DIR_SOURCE%" > NUL
:end
endlocal