@echo off
REM ============================================================
REM Job Portal - Stop Local Development (Windows)
REM ============================================================

echo.
echo [INFO] Stopping all Job Portal services...
echo.

docker-compose down

echo.
echo [INFO] All services stopped.
echo.

REM Option to remove volumes
set /p cleanup="Do you want to remove all data volumes? (y/N): "
if /i "%cleanup%"=="y" (
    echo [INFO] Removing volumes...
    docker-compose down -v
    echo [INFO] Volumes removed.
)

echo.
echo [INFO] Cleanup complete.
pause
