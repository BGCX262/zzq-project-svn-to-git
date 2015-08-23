@echo off
setlocal enabledelayedexpansion

rem script used to build the data

rem ---- call each data make script, warning, order is important

@echo config
if exist config (
    cd config
    if exist make.bat (
        call make.bat %1 %2 %3
    )
    cd ..
)
if not %ERRORLEVEL%==0 (
    goto end
)

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

@echo math
if exist math (
    cd math
    if exist make.bat (
        call make.bat %1 %2 %3
    )
    cd ..
)

@echo sound
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

@echo gfx
if exist gfx (
    cd gfx
    if exist make.bat (
        call make.bat %1 %2 %3
    )
    cd ..
)

@echo config
if exist config (
    cd config
    if exist make.bat (
        call make.bat %1 %2 %3
    )
    cd ..
)

@echo array
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

:end
endlocal