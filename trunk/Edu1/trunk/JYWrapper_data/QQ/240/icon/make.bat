
@echo off
setlocal enabledelayedexpansion

echo ...BUILDING ICON...

REM DIRECTORY, WHERE DATA THAT NEED TO BE PACKED, MUST BE COPIED
set DIR_TO_PACK=%1

REM DIRECTORY, WHERE DATA WHICH DON'T NEED TO BE PACKED, MUST BE COPIED
set DIR_NOT_TO_PACKED=%2

REM DIRECTORY, WHERE TO SOURCE FILES MUST BE COPIED
set DIR_SOURCE=%3

REM copy all png files to DIR_NOT_TO_PACK

echo icon size %ICON_SIZE%....

if not "%ICON_SIZE%"=="" (
		copy /y icon_%ICON_SIZE%.png icon.png
)
copy icon.png "%DIR_NOT_TO_PACKED%" > nul

endlocal