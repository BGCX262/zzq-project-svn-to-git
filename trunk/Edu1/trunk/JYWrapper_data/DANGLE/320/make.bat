@echo off
setlocal enabledelayedexpansion

rem script used to build the data

rem ---- call each data make script, warning, order is important
if "%BBX%"=="1" (
	copy /y data-sound_bbx_ini data-sound.ini
)

rem icon
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

rem math
if exist math (
    cd math
    if exist make.bat (
        call make.bat %1 %2 %3
    )
    cd ..
)

rem sound
if exist sound (
    cd sound
    if exist make.bat (
        call make.bat %1 %2 %3
    )
    cd ..
)
if not %ERRORLEVEL%==0 (
    goto end
)

rem gfx
if exist gfx (
    cd gfx
    if exist make.bat (
        call make.bat %1 %2 %3
    )
    cd ..
)

rem config
if exist config (
    cd config
    if exist make.bat (
        call make.bat %1 %2 %3
    )
    cd ..
)

rem array
if exist array (
    cd array
    if exist make.bat (
        call make.bat %1 %2 %3
    )
    cd ..
)

if not %ERRORLEVEL%==0 (
    goto end
)

rem text
rem if exist text (
rem cd text
rem if exist make.bat (
rem     call make.bat %1 %2 %3
rem )
rem cd ..
rem )
rem if not %ERRORLEVEL%==0 (
rem     goto end
rem )

:end
endlocal