@echo off
setlocal enabledelayedexpansion

@echo ...��Դ���...

REM ���ڼ��ܺ��ٽ���jar����ԴĿ¼
set DIR_TO_PACK=%1

REM �������ܴ���ֱ�ӽ���jar������ԴĿ¼
set DIR_NOT_TO_PACK=%2

REM ��Դ����������ɵ�Դ����Ŀ¼
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