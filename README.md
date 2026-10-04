# Gym Fitness Center

Aplicación del curso Lenguaje de Programación II con Java 21, Spring Boot 3.4.4, MVC, Thymeleaf, JPA/Hibernate, Spring Security, SQL Server y JasperReports.

## Etapas implementadas al 03/10/2026

1. Ejecución local conectada a SQL Server Express mediante autenticación de Windows; JAR y lanzador Eclipse incluidos.
2. Seis mantenimientos con validación, dos consultas, dos reportes PDF con gráficos e inscripción/cancelación con actualización transaccional de cupos y bloqueo de la clase.
3. Informe basado en la plantilla, presentación, capturas reales, guion y matriz de pendientes en el paquete de entrega.
4. Azure for Students activo, App Service gratuito F1 y Azure SQL gratuito con AutoPause creados. JAR publicado con OneDeploy. Workflow con OIDC configurado para actualizar el despliegue.

La versión compila con `mvn -B package '-Dmaven.test.skip=true'`. En este incremento no se ejecutó la suite de pruebas. Las evidencias documentan inicio local y exportación de PDF; el recorrido completo de los CRUD y las pruebas de concurrencia/rollback están pendientes.

## Ejecución con SQL Server Express

Requisitos: Windows x64, JDK 21 o posterior, Maven 3.9 y SQL Server Express con TCP/IP disponible. En esta PC existen `.\SQLEXPRESS` y `gymfitnessdb`.

Instalación nueva con autenticación de Windows:

```powershell
sqlcmd -S '.\SQLEXPRESS' -E -b -i '.\sql\gymfitnessdb.sql'
```

Para actualizar una base anterior, ejecutar `sql/002_cupos_transaccionales.sql`. El script principal también contiene esa migración y puede volver a ejecutarse sin reiniciar los datos.

```powershell
mvn -B package '-Dmaven.test.skip=true'
.\ejecutar-local.ps1
```

Abrir http://localhost:8080/login. El perfil `local-windows` usa `integratedSecurity=true` y `native/windows-x64`; no necesita la contraseña de `gymapp`. La cuenta de Windows debe tener permisos en la base. La DLL oficial de Microsoft y su licencia están incluidas.

| Usuario de demostración | Contraseña inicial | Rol |
|---|---|---|
| admin | Admin123! | ADMIN |
| recepcion | Recepcion123! | RECEPCION |

Las variables APP_ADMIN_PASSWORD y APP_RECEPTION_PASSWORD se aplican al crear usuarios nuevos; no modifican cuentas existentes. Utilizar contraseñas propias al publicar.

## Eclipse

1. En esta PC, abrir el workspace `C:\Users\jagua\eclipse-workspace-gym-etapas`.
2. Importar **File > Import > Maven > Existing Maven Projects** y elegir la carpeta con `pom.xml`. El proyecto se llama `gym-fitness-center`.
3. Seleccionar un JDK compatible con Java 21 y esperar la resolución de Maven.
4. Seleccionar `GymFitnessCenter.launch` y **Run As > GymFitnessCenter**. Ya incluye el perfil local y la ruta a la DLL.
5. Abrir http://localhost:8080/login. Si el puerto está ocupado, detener la ejecución anterior o agregar `--server.port=8081` en los argumentos de programa.

Spring Boot incorpora Tomcat; no se requiere un servidor externo. La importación y el arranque desde Eclipse se realizaron en esta PC el 03/10/2026.

## Funciones

Miembros, membresías, pagos, asistencia, clases y empleados tienen alta, listado, edición y baja. Miembros, membresías, clases y empleados se desactivan para preservar relaciones históricas; pagos y asistencias permiten eliminación administrativa. ADMIN gestiona empleados y operaciones de baja; RECEPCION opera los módulos generales. Los permisos se aplican en el servidor y los formularios usan CSRF.

Las consultas filtran miembros por DNI/nombre/estado y pagos por fechas/método. Los PDF conservan sus filtros y agregan totales y gráficos. Los pagos de ejemplo son de febrero de 2025: consultar desde 2025-01-01 para ver tres registros y S/ 470.00. Son datos de demostración.

EnrollmentService usa @Transactional y PESSIMISTIC_WRITE sobre la clase. Valida estado, duplicidad y capacidad; guarda la inscripción y el contador cuposOcupados. La cancelación actualiza el estado y recalcula ese contador dentro de la misma transacción. La base conserva la restricción única clase/miembro.

## Azure y entrega final

Consultar `infra/GUIA_AZURE.md`. Ejecutar `sql/gymfitnessdb_azure.sql` sobre una Azure SQL Database ya creada. El perfil azure usa identidad administrada para SQL y contraseñas iniciales de aplicación externas. El workflow conserva el JAR y despliega con OIDC. Las plantillas proponen F1 y SQL con cuota gratuita y AutoPause.

URL creada: https://gym-fitness-jaguar009-20261003.azurewebsites.net. El paquete de actualización Azure conserva el resultado de despliegue, logs y capturas de la ejecución observada.

Pendientes: validación funcional completa con ambos roles, concurrencia y rollback, grabación del demo de 3 a 5 minutos y confirmación de coordinación/aportes del equipo. Ver `REQUISITOS_CUMPLIMIENTO.md`.
