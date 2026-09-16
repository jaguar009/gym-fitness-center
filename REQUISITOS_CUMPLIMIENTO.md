# Matriz de cumplimiento del proyecto

Curso: `PROG04691 LENGUAJE DE PROGRAMACIÓN II`  
Sistema: `Gym Fitness Center`  
Versión: migración Maven + Spring Boot

Esta matriz separa lo que ya está implementado en el código de lo que necesita una acción externa del equipo (cuenta GitHub, suscripción Azure o capturas de la defensa).

## Requisitos técnicos

| Requisito | Estado | Evidencia | Acción pendiente |
|---|---|---|---|
| Java y Maven | Cumplido | `pom.xml`, Java 21, `mvn clean verify` | Ninguna |
| Spring Boot / Spring MVC | Cumplido | `GymFitnessCenterApplication.java`, controladores y vistas | Ninguna |
| JPA / Hibernate | Cumplido | `model/*.java`, `repository/*.java` | Ejecutar contra SQL Server en la demostración |
| Conexión a SQL Server | Preparado | `sql/gymfitnessdb.sql`, `application.properties` | Definir `DB_URL`, `DB_USER` y `DB_PASSWORD` |
| Roles y sesiones | Cumplido | `config/SecurityConfig.java` | Capturar login ADMIN y RECEPCION |
| Tres mantenimientos | Cumplido | Miembros, membresías, pagos, asistencia, clases y empleados | Capturar al menos tres |
| Dos consultas | Cumplido | `MemberRepository.search`, `PaymentRepository.search` | Capturar filtros y resultados |
| Dos reportes | Cumplido | `ReportService.java`, `jasper/*.jrxml` | Descargar y anexar los dos PDF |
| Transacción | Cumplido | `service/EnrollmentService.java` con `@Transactional` | Capturar duplicado y falta de cupo |
| Pruebas automatizadas | Cumplido | `src/test/java`, 4 pruebas aprobadas | Conservar salida de `mvn clean verify` |
| Docker | Preparado | `Dockerfile`, `docker-compose.yml` | Ejecutar si se usará en la defensa |
| GitHub Actions | Preparado | `.github/workflows/azure-deploy.yml` | Subir el proyecto a GitHub y ejecutar workflow |
| Azure | Preparado | `infra/azure-webapp.bicep` | Crear recursos, configurar Azure SQL, secretos y registrar URL HTTPS |

## Requisitos documentales

| Sección | Estado | Ubicación |
|---|---|---|
| Carátula e índice | Cumplido | `Plan_Proyecto_Gym_Fitness_Center.docx` |
| Resumen e introducción | Cumplido | Informe, secciones 1 y 2 |
| Diagnóstico SEPTE | Cumplido | Informe, sección 3; incluye factores social, económico y tecnológico |
| Objetivos SMART | Cumplido | Informe, sección 4 |
| Justificación, alcance y beneficiarios | Cumplido | Informe, secciones 5 y 6 |
| Productos y entregables | Cumplido | Informe, sección 7 |
| Metodología y organización | Cumplido | Informe, sección 8 |
| Requisitos, actores y casos de uso | Cumplido | Informe, sección 9 |
| Diseño de datos, arquitectura y seguridad | Cumplido | Informe, secciones 10 y 11 |
| Mantenimientos, consultas, reportes y transacción | Cumplido | Informe, secciones 12 y 13 |
| Despliegue y CI/CD | Cumplido | Informe, sección 14 |
| Pruebas y matriz manual | Cumplido | Informe, sección 15 |
| Conclusiones y recomendaciones | Cumplido | Informe, secciones 16 y 17; máximo tres cada una |
| Glosario y bibliografía | Cumplido | Informe, secciones 18 y 19 |
| Anexos y guion de demo | Cumplido | Informe, sección 20 |
| Presentación PPTX | Cumplido | `Presentacion_Gym_Fitness_Center.pptx` |

## Criterio de cierre antes de la defensa

1. Ejecutar `sql/gymfitnessdb.sql` en SQL Server Express o levantar la alternativa SQL Server de Docker Compose.
2. Iniciar la aplicación y probar ambos roles.
3. Registrar capturas de tres mantenimientos, dos consultas, dos PDF y la transacción.
4. Subir el código a GitHub y conservar el resultado del workflow.
5. Desplegar en Azure, configurar la conexión de base de datos y añadir la URL HTTPS a la presentación y al informe.

No se incluyen credenciales reales, contraseñas de producción ni una URL inventada de Azure en el repositorio.
