@echo off

set currentPath=%cd%

IF /I "%~1" == "-h" goto :Help
IF /I "%~1" == "-help" goto :Help
IF /I "%~1" == "--help" goto :Help
IF /I "%~1" == "--h" goto :Help

REM --- Main script logic starts here ---

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
jmeter -n -t EntireFlow.jmx ^
-JnUser1=%eachGroup% -JinitialDelay1=0 -JstartupTime=%startupTime% -JholdLoad1=%holdLoad1% ^
-JnUser2=%eachGroup% -JinitialDelay2=%initialDelay2% -JholdLoad2=%holdLoad2% ^
-JnUser3=%eachGroup% -JinitialDelay3=%initialDelay3% -JholdLoad3=%holdLoad3% ^
-JnUser4=%lastUserLine% -JinitialDelay4=%initialDelay4% -JholdLoad4=%holdLoad4% ^
-l reportResult.jtl & jmeter -g reportResult.jtl -o report


rem jmeter.bat -n -t BackEnd-Perf-Test-ReportingTask.jmx
rem -JnUsers=%nUsers% -JRampUp=%~2 -JnLoops=%~3 -l reportResult.jtl & jmeter.bat -g reportResult.jtl -o report

GOTO :EOF

goto :Help

:Help
@echo off
echo.
echo =========================================================================================
echo USAGE: runPerformanceTest [TEST_TYPE] [USERS_AMOUNT] [DURATION_SECONDS]
echo =========================================================================================
echo.
echo DESCRIPTION:
echo   Executes the EntireFlow.jmx test with a load profile suited for the specified test type.
echo.
echo -----------------------------------------------------------------------------------------
echo ^| TEST TYPE      ^| PURPOSE ^& RECOMMENDED PARAMETERS                                     ^|
echo -----------------------------------------------------------------------------------------
echo ^| **capacity** ^| Find max sustainable throughput. (High Users / Medium Time)            ^|
echo ^| **response** ^| Measure performance under typical load. (Avg Users / Short Time)       ^|
echo ^| **load**     ^| Generic test to evaluate performance at specified load.                ^|
echo ^| **stress**   ^| Break the system to find bottlenecks. (Very High Users / Short Time)   ^|
echo ^| **stability**^| Find resource leaks/degradation over time. (Moderate Users / LONG Time)^|
echo -----------------------------------------------------------------------------------------
echo.

echo EXAMPLES:
echo   - **Capacity:** runPerformanceTest capacity 500 600       (500 users for 10 minutes)
echo   - **Response Time:** runPerformanceTest response 80 300   (80 users for 5 minutes)
echo   - **Load:** runPerformanceTest load 150 900               (150 users for 15 minutes)
echo   - **Stress:** runPerformanceTest stress 1000 120          (1000 users for 2 minutes)
echo   - **Stability:** runPerformanceTest stability 50 14400    (50 users for 4 hours)
echo.
echo PARAMETERS:
echo   [TEST_TYPE]       - The type of performance test (e.g., capacity, stability, stress).
echo   [USERS_AMOUNT]    - Total number of concurrent virtual users.
echo   [DURATION_SECONDS] - Total time the test will run (in seconds).
echo.
echo =========================================================================================
echo.
goto :EOF