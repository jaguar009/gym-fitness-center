# Gym Fitness Center Migrado

Sistema web para la gestión de miembros, membresías, pagos, asistencia, empleados y clases del gimnasio Gym Fitness Center.

## Resultado de la migración

La versión migrada reemplaza el proyecto Eclipse basado en Servlets, JSP y JDBC por una aplicación Maven con Spring Boot, Spring MVC, Spring Data JPA, Hibernate, Spring Security, Thymeleaf y JasperReports. La base de datos objetivo de esta instalación es SQL Server Express (`.\SQLEXPRESS`). El proyecto original queda intacto en la carpeta `Documentos_Originales\Gym_FitnessCenter`.

## Requisitos cubiertos

| Requisito del plan | Implementación |
|---|---|
| Conexión a base de datos | SQL Server mediante Spring Data JPA y el script `sql/gymfitnessdb.sql` |
| Roles diferentes | Roles `ADMIN` y `RECEPCION` con Spring Security |
| Sesiones | Sesión HTTP administrada por Spring Security, fijación de sesión renovada y cierre de sesión |
| Tres mantenimientos | Miembros, membresías, pagos, asistencia, clases y empleados |
| Dos consultas | Miembros por DNI/nombre/estado y pagos por periodo/método |
| Dos reportes | Reporte de miembros y reporte de pagos en PDF con JasperReports |
| Caso transaccional | Inscripción de un miembro a una clase con `@Transactional`, validación de duplicados y control de cupos |
| GitHub Actions | Flujo `.github/workflows/azure-deploy.yml` |
| Azure | Plantilla `infra/azure-webapp.bicep` y flujo de despliegue preparado para Azure App Service + Azure SQL |

El despliegue real en Azure requiere una suscripción, un Web App y los secretos de GitHub. El código queda listo para ese paso, pero no se inventa una URL ni se afirma un despliegue que necesita credenciales externas.

## Ejecución local

### Opción A SQL Server local (recomendada)

1. Instala Java 21, Maven 3.9 o superior y SQL Server Express.
2. Verifica que el servicio `SQL Server (SQLEXPRESS)` esté iniciado.
3. Ejecuta `sql/gymfitnessdb.sql` con SQL Server Management Studio o `sqlcmd` usando autenticación de Windows:

```powershell
sqlcmd -S ".\SQLEXPRESS" -E -i ".\sql\gymfitnessdb.sql"
```

4. Define las credenciales del usuario SQL que tenga permisos sobre gymfitnessdb:

```powershell
$env:DB_URL = 'jdbc:sqlserver://localhost;instanceName=SQLEXPRESS;databaseName=gymfitnessdb;encrypt=false;trustServerCertificate=true'
$env:DB_USER = 'gymapp'
$env:DB_PASSWORD = 'tu_contraseña_de_gymapp'

```

Cada integrante debe usar sus propias credenciales locales. No se deben subir contraseñas al repositorio.

5. Inicia la aplicación:

```powershell
mvn spring-boot:run
```

6. Abre `http://localhost:8080/login`.

Credenciales iniciales configurables por variables de entorno:

- `admin` / `Admin123!`
- `recepcion` / `Recepcion123!`

Cambia las contraseñas antes de publicar el sistema.

### Ejecución desde Eclipse

Puedes clonar este repositorio y abrirlo en cualquier workspace de Eclipse. No dependas de una ruta fija del equipo del autor.

1. Abre Eclipse y selecciona cualquier workspace.
2. Importa la carpeta clonada con `File > Import > Maven > Existing Maven Projects`.
3. En `Run > Run Configurations`, crea una ejecución Java/Spring Boot para `pe.cibertec.gym.GymFitnessCenterApplication`.
4. En la pestaña `Environment`, define `DB_URL`, `DB_USER` y `DB_PASSWORD` con los valores de tu SQL Server local.
5. Ejecuta la clase principal y abre `http://localhost:8080/login`.

El perfil de ejecución usa `DDL_AUTO=validate` por defecto: la base debe existir y coincidir con `sql/gymfitnessdb.sql`.

### Opción B Docker Compose (alternativa)

```powershell
$env:MSSQL_SA_PASSWORD = 'TuClaveSegura#2026'
mvn clean package -DskipTests
docker compose up -d sqlserver
sqlcmd -S 'localhost,1433' -U sa -P $env:MSSQL_SA_PASSWORD -i '.\sql\gymfitnessdb.sql'
docker compose up --build app
```

Abre `http://localhost:8080/login`.

## Funcionalidades

- Panel con total de miembros, miembros activos, clases activas, asistencia e ingresos.
- CRUD de miembros, membresías, pagos, clases y empleados.
- Registro de asistencias.
- Inscripción transaccional a clases con control de cupos y duplicados.
- Consulta de miembros por DNI, nombre y estado.
- Consulta de pagos por rango de fechas y método.
- Exportación de ambos resultados a PDF mediante los archivos `.jrxml` ubicados en `src/main/resources/jasper`.
- Restricción de empleados y operaciones administrativas al rol `ADMIN`.

## Despliegue en Azure

1. Crea un grupo de recursos y un Web App Linux con Java 21, o despliega `infra/azure-webapp.bicep`.
2. Configura en Azure las variables `DB_URL`, `DB_USER`, `DB_PASSWORD`, `APP_ADMIN_PASSWORD` y `APP_RECEPTION_PASSWORD`. Para Azure SQL usa una URL JDBC `jdbc:sqlserver://<servidor>.database.windows.net:1433;databaseName=gymfitnessdb;encrypt=true;trustServerCertificate=false;loginTimeout=30`.
3. En GitHub agrega `AZURE_WEBAPP_NAME` y `AZURE_WEBAPP_PUBLISH_PROFILE` como secretos del repositorio.
4. Publica en la rama `main`. El flujo compila el JAR y lo despliega en Azure.

La base de datos debe estar disponible desde Azure mediante Azure SQL Database o un SQL Server accesible. No se deben guardar contraseñas en el repositorio.

## Estructura

```text
src/main/java/pe/cibertec/gym
  config          Seguridad e inicialización de usuarios
  controller      Controladores Spring MVC
  model           Entidades JPA
  repository      Consultas Spring Data JPA
  report          Filas de reportes
  service         Lógica transaccional y JasperReports
src/main/resources
  jasper          Plantillas JRXML
  templates       Vistas Thymeleaf
  static/css      Estilos
sql               Modelo y datos de prueba
infra              Infraestructura Azure
.github/workflows  Integración continua y despliegue
```

## Revisión técnica

La matriz detallada de cumplimiento y las acciones externas pendientes está en `REQUISITOS_CUMPLIMIENTO.md`.

Antes de entregar se debe ejecutar:

```powershell
mvn clean verify
```

Después, probar manualmente los dos roles, los seis mantenimientos, las dos consultas, las dos descargas PDF y el rechazo de una inscripción duplicada o sin cupos. La evidencia de esas pruebas debe incorporarse como capturas en los anexos del informe.
