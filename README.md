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
 │   │   └─ com.jp.orpha.movilitzer_v2
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

## ⚡ Primera configuración (dev)

### Opción A — Auto-setup (recomendada)
1. Crea una app en Spotify Developers y obtén **Client ID** y **Client Secret**.
2. Copia `application.properties.example` a `src/main/resources/application.properties` y completa las claves (o exporta variables de entorno usando `.env.example`).
3. (Opcional) Carga datos base con `data.sql.example` (Venue + Playlist) o usa los endpoints admin.
4. Inicia la app: `mvn spring-boot:run`.
5. En Swagger:
   - `GET /api/v1/spotify/{venueId}/authorize` → copia la `authorizationUrl` y ábrela en el navegador.
   - `POST /api/v1/spotify/sync/{venueId}` → sincroniza canciones.
   - `GET /api/v1/public/venues/{code}/tracks` → verifica catálogo.
   - `POST /api/v1/public/venues/{venueId}/queue` → prueba Add to Queue (requiere dispositivo Premium activo).

### Opción B — Manual rápida
- Crea el Venue y la Playlist en **H2 Console** (`/h2-console`), usando las sentencias de `data.sql.example`.
- Autoriza y sincroniza desde Swagger como en la Opción A.

### Buenas prácticas
- No comitear `application.properties` (usa `.gitignore` + `application.properties.example`).
- Usa **127.0.0.1** en el redirect URI (no `localhost`).
- Asegura el `-parameters` del compilador en el `pom.xml`.
- Tokens de Spotify se refrescan automáticamente (proactivo y en 401).


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
