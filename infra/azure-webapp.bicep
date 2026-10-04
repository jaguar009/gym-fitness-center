param appName string
param location string = resourceGroup().location
param linuxFxVersion string = 'JAVA|21-java21'
@allowed([
  'F1'
  'B1'
])
param pricingTier string = 'F1'

resource appServicePlan 'Microsoft.Web/serverfarms@2023-12-01' = {
  name: '${appName}-plan'
  location: location
  sku: {
    name: pricingTier
    tier: pricingTier == 'F1' ? 'Free' : 'Basic'
  }
  kind: 'linux'
  properties: {
    reserved: true
  }
}

resource webApp 'Microsoft.Web/sites@2023-12-01' = {
  name: appName
  location: location
  kind: 'app,linux'
  identity: {
    type: 'SystemAssigned'
  }
  properties: {
    serverFarmId: appServicePlan.id
    httpsOnly: true
    siteConfig: {
      linuxFxVersion: linuxFxVersion
      alwaysOn: pricingTier != 'F1'
      minTlsVersion: '1.2'
      ftpsState: 'Disabled'
      appSettings: [
        { name: 'SPRING_PROFILES_ACTIVE', value: 'azure' }
        { name: 'DDL_AUTO', value: 'validate' }
        { name: 'JAVA_OPTS', value: '-Xms64m -Xmx384m -Djava.awt.headless=true' }
        { name: 'WEBSITES_CONTAINER_START_TIME_LIMIT', value: '600' }
      ]
    }
  }
}

output url string = 'https://${webApp.properties.defaultHostName}'
