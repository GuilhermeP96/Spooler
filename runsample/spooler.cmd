@ECHO OFF
set NLS_LANG=.AL32UTF8
java -jar %~dp0\Spooler.jar %~dp0\config.txt >> %~dp0\log.txt 2>&1
EXIT