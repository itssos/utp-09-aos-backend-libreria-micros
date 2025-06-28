@echo off
setlocal

REM Crear carpeta de logs si no existe
if not exist logs (
  mkdir logs
)

echo Iniciando Eureka Server...
start "eureka-server" cmd /k ".\mvnw spring-boot:run -pl eureka-server"
timeout /t 5 >nul

echo Iniciando micro-users...
start "micro-users" cmd /k ".\mvnw spring-boot:run -pl micro-users"
timeout /t 5 >nul

echo Iniciando micro-products...
start "micro-products" cmd /k ".\mvnw spring-boot:run -pl micro-products"
timeout /t 5 >nul

echo Iniciando micro-auth...
start "micro-auth" cmd /k ".\mvnw spring-boot:run -pl micro-auth"
timeout /t 5 >nul

echo Iniciando API Gateway...
start "api-gateway" cmd /k ".\mvnw spring-boot:run -pl api-gateway"
timeout /t 5 >nul

echo ✅ Todos los microservicios han sido iniciados en nuevas consolas.
echo 📡 Puedes ver los logs en tiempo real en cada ventana abierta.

endlocal
pause
