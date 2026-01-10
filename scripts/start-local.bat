@echo off
REM ============================================================
REM Job Portal - Local Development Script (Windows)
REM Starts all services using Docker Compose
REM ============================================================

echo.
echo ============================================================
echo   Job Portal Management System - Local Development
echo ============================================================
echo.

REM Check if Docker is running
docker info > nul 2>&1
if %errorlevel% neq 0 (
    echo [ERROR] Docker is not running. Please start Docker Desktop.
    pause
    exit /b 1
)

echo [INFO] Starting infrastructure services...
docker-compose up -d mysql postgres redis

echo [INFO] Waiting for databases to be ready...
timeout /t 30 /nobreak > nul

echo [INFO] Starting microservices...
docker-compose up -d api-gateway auth-service job-service application-service

echo [INFO] Starting monitoring stack...
docker-compose up -d prometheus grafana

echo.
echo ============================================================
echo   Services are starting...
echo ============================================================
echo.
echo   API Gateway:     http://localhost:8080
echo   Auth Service:    http://localhost:8081
echo   Job Service:     http://localhost:8083
echo   App Service:     http://localhost:8084
echo.
echo   Prometheus:      http://localhost:9090
echo   Grafana:         http://localhost:3000 (admin/admin123)
echo.
echo   MySQL:           localhost:3306
echo   PostgreSQL:      localhost:5432
echo   Redis:           localhost:6379
echo.
echo ============================================================
echo.

REM Show running containers
docker-compose ps

echo.
echo [INFO] To view logs: docker-compose logs -f [service-name]
echo [INFO] To stop all: docker-compose down
echo.
pause
