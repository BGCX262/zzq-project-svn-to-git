@echo off
setlocal enabledelayedexpansion

@echo ...��Դ���...

REM ���ڼ��ܺ��ٽ���jar����ԴĿ¼
set DIR_TO_PACK=%1

REM �������ܴ���ֱ�ӽ���jar������ԴĿ¼
set DIR_NOT_TO_PACK=%2

REM ��Դ����������ɵ�Դ����Ŀ¼
set DIR_SOURCE=%3

@echo ͼ��Icon
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

@echo ...����Config...
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

@echo ...����Sound...
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

@echo ...ͼƬGfx...
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