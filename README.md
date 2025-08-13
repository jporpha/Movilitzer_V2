# 🚀 Movilitzer v2

## 📌 Descripción
**Movilitzer v2** es un sistema backend RESTful que convierte cualquier fiesta en un wurlitzer digital conectado a Spotify. 
Los locales o anfitriones configuran una playlist base y los usuarios, mediante un acceso público, pueden ver el catálogo y encolar canciones directamente en la lista de reproducción activa. 
Incluye control de votos por usuario, manejo de colas, y sincronización con la API de Spotify, con opción de cola interna para usos no comerciales.

## 🛠️ Tecnologías Utilizadas
- Java 17
- Spring Boot 3
- Spring Data JPA
- MapStruct
- Lombok
- Swagger/OpenAPI
- Base de datos: H2 (dev) / PostgreSQL (prod)
- API de Spotify

## 📂 Estructura del Proyecto
```
src
 ├─ main
 │   ├─ java
 │   │   └─ com.movilitzer.v2
 │   │       ├─ controller     # Controladores REST
 │   │       ├─ service        # Lógica de negocio
 │   │       ├─ repository     # Acceso a datos
 │   │       ├─ entity         # Entidades JPA
 │   │       ├─ dto            # Objetos de transferencia
 │   │       ├─ mapper         # MapStruct mappers
 │   │       └─ exception      # Manejo de errores
 │   └─ resources
 │       ├─ application.yml    # Configuración
 │       └─ data.sql           # Datos iniciales (opcional)
```

## ⚙️ Configuración del Proyecto
1. Clonar el repositorio:
   ```bash
   git clone https://github.com/usuario/movilitzer_v2.git
   cd movilitzer_v2
   ```
2. Configurar las variables de entorno o el archivo `application.yml`.
3. Ejecutar:
   ```bash
   mvn spring-boot:run
   ```

## 📖 Documentación API
Swagger estará disponible en:
```
http://localhost:8080/swagger-ui/index.html
```

## 🧪 Pruebas
Ejecutar pruebas unitarias:
```bash
mvn test
```

## 📜 Licencia
[MIT](LICENSE) - Libre para uso y modificación.
