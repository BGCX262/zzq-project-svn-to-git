@echo off
setlocal enabledelayedexpansion

@echo ...资源打包...

REM 用于加密后再进入jar的资源目录
set DIR_TO_PACK=%1

REM 不经加密处理，直接进入jar包的资源目录
set DIR_NOT_TO_PACK=%2

REM 资源打包过程生成的源代码目录
set DIR_SOURCE=%3

@echo 图标Icon
pushd
if exist icon (
    cd icon
    if exist make.bat (
        call make.bat %1 %2 %3
    )
    cd ..
)
popd

if not %ERRORLEVEL%==0 (
    goto end
)

@echo ...配置Config...
pushd
if exist config (
    cd config
    if exist make.bat (
        call make.bat %1 %2 %3
    )
    cd ..
)
popd
if not %ERRORLEVEL%==0 (
    goto end
)

@echo ...声音Sound...
pushd
if exist sound (
    cd sound
    if exist make.bat (
        call make.bat %1 %2 %3
    )
    cd ..
)
popd
if not %ERRORLEVEL%==0 (
    goto end
)

@echo ...图片Gfx...
pushd
if exist gfx (
    cd gfx
    if exist make.bat (
        call make.bat %1 %2 %3
    )
    cd ..
)
popd

:end
endlocal