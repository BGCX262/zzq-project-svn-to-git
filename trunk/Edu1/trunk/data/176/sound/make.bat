@echo off
setlocal enabledelayedexpansion

@echo ...����...

REM ���ڼ��ܺ��ٽ���jar����ԴĿ¼
set DIR_TO_PACK=%1

REM �������ܴ���ֱ�ӽ���jar������ԴĿ¼
set DIR_NOT_TO_PACK=%2

REM ��Դ����������ɵ�Դ����Ŀ¼
set DIR_SOURCE=%3

xcopy /Y /S *.*  "%DIR_NOT_TO_PACK%" > NUL
del "%DIR_NOT_TO_PACK%"\make.bat /F /Q > NULL
:end
endlocal