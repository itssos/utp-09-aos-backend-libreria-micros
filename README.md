
<p align="center">
  <img src="https://i0.wp.com/www.suma.pe/wp-content/uploads/2021/11/UTP-Logo.jpg?fit=728%2C158" alt="UTP Logo" width="400"/>
</p>

# 📚 Librería Jesus Amigo

Proyecto desarrollado para el noveno ciclo de la **Universidad Tecnológica del Perú (UTP)**, diseñado como sistema distribuido con **microservicios**, **API Gateway** y **Eureka** para gestión de una librería llamada **Jesus Amigo**.

## 🚀 Arquitectura

- **Spring Cloud Eureka Server**: Descubrimiento de servicios.
- **API Gateway**: Encargado del enrutamiento y seguridad.
- **Microservicios independientes**:
  - `micro-auth`: Autenticación (login).
  - `micro-users`: Usuarios, roles y permisos.
  - `micro-products`: Productos, inventario, reportes, ventas.
- **Common Modules**:
  - `common-exception-handler`: Manejo centralizado de errores.
  - `common-dto`: Transferencia de datos.

## 📑 Configuración destacada

- **API Gateway** centraliza el acceso a microservicios y maneja JWT con `secret` propio.
- Cada microservicio se registra en **Eureka** automáticamente.
- Comunicación balanceada con `lb://` vía Spring Cloud Gateway.

## 🏗 Repositorios del proyecto

- `eureka-server/` : Descubrimiento de servicios
- `api-gateway/` : Proxy inverso y seguridad
- `micro-auth/`, `micro-users/`, `micro-products/` : Servicios de dominio
- `common-dto/`, `common-exception-handler/` : Utilidades compartidas

---

## 🔗 Endpoints

### 🔐 Autenticación
- `POST /api/auth/login`

### 👤 Usuarios, Roles y Permisos
- `GET /api/users/short`
- `GET /api/users/{id}`
- `PUT /api/users/{id}`
- `DELETE /api/users/{id}`
- `GET /api/roles/{id}`
- `PUT /api/roles/{id}`
- `DELETE /api/roles/{id}`
- `GET /api/permissions/{id}`
- `GET /api/permissions/name/{name}`
- `POST /api/roles/{roleId}/permissions/{permissionName}`
- `DELETE /api/roles/{roleId}/permissions/{permissionName}`

### 📦 Productos, Inventario y Reportes
- `GET /api/products/public`
- `GET /api/products/{id}`
- `PUT /api/products/{id}`
- `DELETE /api/products/{id}`
- `PATCH /api/products/{id}/active`
- `GET /api/authors/{id}`
- `PUT /api/authors/{id}`
- `DELETE /api/authors/{id}`
- `GET /api/categories/{id}`
- `PUT /api/categories/{id}`
- `DELETE /api/categories/{id}`
- `GET /api/editorials/{id}`
- `PUT /api/editorials/{id}`
- `DELETE /api/editorials/{id}`
- `POST /api/inventory/recharge`
- `POST /api/inventory/decrease`
- `GET /api/inventory/product/{productId}/stock`
- `PUT /api/stock-movements/{id}`
- `GET /api/stock-movements/{id}`
- `GET /api/reports/products/top-sold`
- `GET /api/reports/products/low-stock`
- `GET /api/reports/products/sales`
- `GET /api/sales/{id}`

🚀 Desarrollado por el equipo del noveno ciclo de **UTP - Jesus Amigo**.
