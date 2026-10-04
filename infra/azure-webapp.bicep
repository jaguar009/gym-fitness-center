param appName string
param location string = resourceGroup().location
param linuxFxVersion string = 'Java|21-java21'

resource appServicePlan 'Microsoft.Web/serverfarms@2023-12-01' = {
  name: '${appName}-plan'
  location: location
  sku: {
    name: 'B1'
    tier: 'Basic'
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
  properties: {
    serverFarmId: appServicePlan.id
    httpsOnly: true
    siteConfig: {
      linuxFxVersion: linuxFxVersion
      alwaysOn: true
      appSettings: [
        { name: 'WEBSITES_PORT', value: '8080' }
        { name: 'SPRING_PROFILES_ACTIVE', value: 'azure' }
        { name: 'DDL_AUTO', value: 'validate' }
      ]
    }
  }
}

output url string = 'https://${webApp.properties.defaultHostName}'
