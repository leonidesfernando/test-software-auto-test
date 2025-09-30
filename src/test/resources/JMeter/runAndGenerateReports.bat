@echo off

set currentPath=%cd%

IF EXIST "%currentPath%\reportResult.jtl" DEL /s /Q "reportResult.jtl" 1>nul

@echo on
@echo "Removing previous reports data if exists"
@echo off
IF EXIST "%currentPath%\report\" RMDIR /Q/S report\ & @echo "  > report folder REMOVED OK"
IF EXIST "%currentPath%\jmeter.log" DEL /s /Q "%currentPath%\jmeter.log" 1>nul & @echo "  > jmeter.log folder REMOVED OK"
IF EXIST "%currentPath%\temp\" RMDIR /Q/S %currentPath%\temp & @echo "  > temp folder REMOVED OK"

set /A users = %~1
set /A time = %~2
set /A LINES = 4
set /A eachGroup = %users% / %LINES%
rem set /A startupTime = %eachGroup% * 2
set /A startupTime = %eachGroup% / 2 
echo users: %users%  time: %time% lines: %LINES% eachLineUser: %eachGroup% startupTime: %startupTime%

set /A initialDelay2 = %startupTime%
set /A initialDelay3 = %startupTime% * 2
set /A initialDelay4 = %startupTime% * 3

set /A holdLoad1 = %time% + 4* %startupTime%
set /A holdLoad2 = %time% + 3* %startupTime%
set /A holdLoad3 = %time% + 2* %startupTime%
set /A holdLoad4 = %time% +  %startupTime%
set /A lastUserLine = %eachGroup% + %users% %% %LINES%
echo lastUserLine %lastUserLine%

echo times: %holdLoad1%  %holdLoad2% %holdLoad3%  %holdLoad4%

echo "initialDelay:" %initialDelay2%  %initialDelay3%  %initialDelay4%


@echo on
@echo "Running JMeter Tests and generating dashboard report"
jmeter.bat -n -t EntireFlow.jmx ^
-JnUser1=%eachGroup% -JinitialDelay1=0 -JstartupTime=%startupTime% -JholdLoad1=%holdLoad1% ^
-JnUser2=%eachGroup% -JinitialDelay2=%initialDelay2% -JholdLoad2=%holdLoad2% ^
-JnUser3=%eachGroup% -JinitialDelay3=%initialDelay3% -JholdLoad3=%holdLoad3% ^
-JnUser4=%lastUserLine% -JinitialDelay4=%initialDelay4% -JholdLoad4=%holdLoad4% ^
-l reportResult.jtl & jmeter.bat -g reportResult.jtl -o report



rem jmeter.bat -n -t BackEnd-Perf-Test-ReportingTask.jmx 
rem -JnUsers=%nUsers% -JRampUp=%~2 -JnLoops=%~3 -l reportResult.jtl & jmeter.bat -g reportResult.jtl -o report
