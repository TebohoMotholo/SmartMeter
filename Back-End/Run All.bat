@echo off
REM — activate your virtualenv (if you have one) —
REM adjust the path to your venv if needed
if exist venv\Scripts\activate (
  call venv\Scripts\activate
)

REM — start Flask server in new window —
start "Flask Server" cmd /k "python server.py"

REM — give the server a second to spin up —
timeout /t 2 /nobreak > nul

REM — start simulated meter in new window —
start "Meter Box" cmd /k "python simulated_meter.py"

echo.
echo Both Server and Simulated Meter started.
pause
