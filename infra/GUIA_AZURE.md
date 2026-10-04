# Etapa de publicación en Azure

Actualización del 3 de octubre de 2026: Azure for Students está activo. Se crearon App Service Linux gratuito y Azure SQL con cuota gratuita y pausa al agotarla en West US. OneDeploy publicó el JAR. La guía del paquete registra la observación del arranque y la ejecución de Actions.

## Recursos del despliegue

- Grupo: `rg-gym-fitness-center`.
- Plan: `asp-gym-fitness-center-free`, Free F1 (la API lo identifica como LinuxFree U13).
- Aplicación: `gym-fitness-jaguar009-20261003`, Java 21 SE.
- URL: https://gym-fitness-jaguar009-20261003.azurewebsites.net
- SQL: `sql-gym-jaguar009-20261003-wus.database.windows.net`, base `gymfitnessdb`.
- Azure SQL: General Purpose Serverless, `useFreeLimit=true`, `freeLimitExhaustionBehavior=AutoPause`, máximo 32 GB y pausa por inactividad de 60 minutos.

Los recursos gratuitos tienen límites y arranque en frío. La pausa al agotar la oferta SQL evita continuar usando capacidad facturable. El plan F1 no tiene Always On. West US 2 rechazó F1 por cuota cero y SQL por disponibilidad; West US permitió ambos recursos.

## Reproducir o actualizar

1. Elegir una región permitida por la política y con capacidad. `azure-webapp.bicep` propone F1 por defecto. B1 es facturable y exige una decisión de presupuesto. `azure-sql-free.bicep` propone la oferta SQL gratuita con AutoPause.
2. Crear una Azure SQL Database accesible desde la aplicación y ejecutar `sql/gymfitnessdb_azure.sql` en esa base. Ese archivo contiene las tablas, datos de demostración y migración de cupos, sin `CREATE DATABASE` ni `USE`. Para SQL Server local se conserva `sql/gymfitnessdb.sql`.
3. Configurar App Service para Java 21 y el JAR ejecutable.
4. Activar la identidad administrada del App Service. Crear su usuario SQL con el client ID y `TYPE=E`; conceder CONNECT y SELECT, INSERT, UPDATE, DELETE sobre dbo. El servidor usa autenticación Microsoft Entra exclusiva. La conexión JDBC usa `encrypt=true;trustServerCertificate=false;authentication=ActiveDirectoryManagedIdentity;loginTimeout=90`. `DB_USER` y `DB_PASSWORD` quedan vacíos. Configurar `SPRING_PROFILES_ACTIVE=azure`, `DB_URL`, `APP_ADMIN_PASSWORD` y `APP_RECEPTION_PASSWORD` en App Service. Guardar las contraseñas iniciales de aplicación en un lugar privado, separado de Git y del ZIP.
5. GitHub utiliza las variables `AZURE_WEBAPP_NAME`, `AZURE_CLIENT_ID`, `AZURE_TENANT_ID` y `AZURE_SUBSCRIPTION_ID`. La identidad `id-gym-github-deploy` confía en OIDC de las ramas main y codex/gym-etapas-local del repositorio. Tiene Website Contributor únicamente en este App Service, sin permisos SQL. El workflow no usa publish profile ni un secreto de Azure.
6. Ejecutar el workflow y conservar la URL pública, captura del paso de despliegue y captura del sistema en Azure.

El firewall SQL permite las IP de salida del App Service. La regla temporal del equipo de inicialización se eliminó después de aplicar el esquema. Si cambian las IP de salida al mover o cambiar el plan, actualizar estas reglas.

Para publicar desde CLI: `az webapp deploy -g rg-gym-fitness-center -n gym-fitness-jaguar009-20261003 --src-path target/gym-fitness-center-1.0.0.jar --type jar --enable-kudu-warmup false --track-status false`. Se desactivó la espera previa de Kudu porque se demoró sin registrar una implementación. El intento posterior registró OneDeploy exitoso.

La compilación omite pruebas por instrucción del incremento. Un build con deploy omitido no acredita publicación. Revisar los dos jobs y la aplicación pública. Las contraseñas de bootstrap solo crean cuentas nuevas; no cambian usuarios existentes.

Fuentes: [oferta gratuita Azure SQL](https://learn.microsoft.com/en-us/azure/azure-sql/database/free-offer?view=azuresql), [Java SE en App Service](https://learn.microsoft.com/en-us/azure/app-service/configure-language-java), [identidad JDBC](https://learn.microsoft.com/en-us/sql/connect/jdbc/connecting-using-azure-active-directory-authentication?view=sql-server-ver17), [OIDC con identidad administrada](https://learn.microsoft.com/en-us/entra/workload-id/workload-identity-federation-create-trust-user-assigned-managed-identity).
