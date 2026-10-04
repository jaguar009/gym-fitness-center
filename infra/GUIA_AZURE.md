# Etapa de publicación en Azure

Actualización del 4 de octubre de 2026: Azure for Students está activo. App Service Linux F1 y Azure SQL con cuota gratuita y pausa al agotarla están desplegados en West US. Se observó el acceso administrativo público, el panel, dos consultas y la descarga de ambos reportes PDF. El arranque registró conexión Hikari, validación JPA y Tomcat en 18,457 segundos. Esta observación no sustituye la validación funcional completa.

## Recursos del despliegue

- Grupo: `rg-gym-fitness-center`.
- Plan: `asp-gym-fitness-center-free`, Free F1.
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
5. GitHub utiliza las variables `AZURE_RESOURCE_GROUP`, `AZURE_WEBAPP_NAME`, `AZURE_CLIENT_ID`, `AZURE_TENANT_ID` y `AZURE_SUBSCRIPTION_ID`. La identidad `id-gym-github-deploy` confía en OIDC de las ramas main y codex/gym-etapas-local del repositorio. Tiene Website Contributor únicamente en este App Service, sin permisos SQL. El workflow no usa publish profile ni un secreto de Azure. Los subjects de este repositorio incluyen los IDs inmutables de propietario y repositorio: `repo:jaguar009@21321460/gym-fitness-center@1373366398:ref:refs/heads/main` y el mismo prefijo terminado en `refs/heads/codex/gym-etapas-local`.
6. Ejecutar el workflow y conservar la URL pública, captura del paso de despliegue y captura del sistema en Azure.

El firewall SQL permite individualmente las 25 IP de `possibleOutboundIpAddresses` del App Service. Una IP posible diferente de las siete IP actuales produjo el error 40615 durante el primer arranque; se corrigieron las reglas. No existe una regla general para todos los servicios Azure. Las reglas temporales del equipo se eliminaron. Si cambian las IP al mover o cambiar el plan, actualizar estas reglas. La política SQL Proxy utiliza el puerto 1433 y conserva el cifrado TLS.

Para publicar desde CLI: `az webapp deploy -g rg-gym-fitness-center -n gym-fitness-jaguar009-20261003 --src-path target/gym-fitness-center-1.0.0.jar --type jar --enable-kudu-warmup false --track-status false`. Se desactivó la espera previa de Kudu porque se demoró sin registrar una implementación. El intento posterior registró OneDeploy exitoso.

El workflow detiene la aplicación antes de reemplazar el JAR y la inicia después, evitando que el proceso anterior conserve clases del archivo reemplazado. Esto implica una breve interrupción durante cada actualización. Java SE proporciona PORT y la plataforma establece el puerto 80; el perfil local conserva 8080. El límite de arranque es 600 segundos para admitir el despertar de SQL serverless.

La compilación omite pruebas por instrucción del incremento. Un build con deploy omitido no acredita publicación. La ejecución [37180075394](https://github.com/jaguar009/gym-fitness-center/actions/runs/37180075394) compiló y desplegó el commit fae9eb7. La evidencia del paquete identifica también la ejecución final y su commit. Las contraseñas de bootstrap solo crean cuentas nuevas; no cambian usuarios existentes.

Fuentes: [oferta gratuita Azure SQL](https://learn.microsoft.com/en-us/azure/azure-sql/database/free-offer?view=azuresql), [Java SE en App Service](https://learn.microsoft.com/en-us/azure/app-service/configure-language-java), [identidad JDBC](https://learn.microsoft.com/en-us/sql/connect/jdbc/connecting-using-azure-active-directory-authentication?view=sql-server-ver17), [OIDC con identidad administrada](https://learn.microsoft.com/en-us/entra/workload-id/workload-identity-federation-create-trust-user-assigned-managed-identity).
