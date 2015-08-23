@echo off
setlocal enabledelayedexpansion

@echo ...资源打包...

REM 用于加密后再进入jar的资源目录
set DIR_TO_PACK=%1

REM 不经加密处理，直接进入jar包的资源目录
set DIR_NOT_TO_PACK=%2

REM 资源打包过程生成的源代码目录
set DIR_SOURCE=%3

xcopy /Y /S sound\*.*  "%DIR_NOT_TO_PACK%"\sound\ > NUL
xcopy /Y /S sp\*.*  "%DIR_NOT_TO_PACK%"\sp\ > NUL
@echo icon
if exist icon (
    cd icon
    if exist make.bat (
        call make.bat %1 %2 %3
    )
    cd ..
)
if not %ERRORLEVEL%==0 (
    goto end
)


del "%DIR_NOT_TO_PACK%"\make.bat /F /Q > NULL
:end
endlocal