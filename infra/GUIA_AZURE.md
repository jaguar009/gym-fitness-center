# Etapa de publicación en Azure

Estado del 3 de octubre de 2026: la cuenta institucional entra al portal, pero no tiene suscripciones accesibles. No hay un despliegue de este proyecto acreditado.

## Cuando exista una suscripción con permisos

1. Elegir región y plan de App Service disponibles en la suscripción. La plantilla Bicep actual propone B1, que utiliza recursos facturables; revisar la tarifa o el crédito educativo antes de crear recursos.
2. Crear una Azure SQL Database accesible desde la aplicación y ejecutar `sql/gymfitnessdb_azure.sql` en esa base. Ese archivo contiene las tablas, datos de demostración y migración de cupos, sin `CREATE DATABASE` ni `USE`. Para SQL Server local se conserva `sql/gymfitnessdb.sql`.
3. Configurar App Service para Java 21 y el JAR ejecutable.
4. Agregar las variables `SPRING_PROFILES_ACTIVE=azure`, `DB_URL`, `DB_USER`, `DB_PASSWORD`, `APP_ADMIN_PASSWORD` y `APP_RECEPTION_PASSWORD` en App Service. La URL JDBC de Azure debe usar `encrypt=true;trustServerCertificate=false`, el host real y la base real.
5. Configurar en GitHub `AZURE_WEBAPP_NAME` y `AZURE_WEBAPP_PUBLISH_PROFILE`. Si la política institucional exige identidad federada, adaptar el workflow a OIDC antes de desplegar. No guardar credenciales en archivos del proyecto.
6. Ejecutar el workflow y conservar la URL pública, captura del paso de despliegue y captura del sistema en Azure.

La compilación de GitHub Actions genera un artefacto JAR aunque Azure esté pendiente. El estado verde de compilación y la omisión del paso de despliegue no acreditan publicación en Azure.
