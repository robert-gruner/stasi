# STASI - Super Terrific App Spotting Individuals
## Android Client App
* can be found in folder /app
* make sure you place a resources files in the res/values folder which configures Pusher like:
```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="PUSHER_API_TOKEN">YOUR_TOP_SECRET_TOKEN</string>
    <string name="LOCAL_PUSHER_URL">URL_OF_THE_LOCAL_NODE_SERVER</string>
    <string name="LOCAL_PUSHER_PORT">PORT_OF_THE_LOCAL_NODE_SERVER</string>
</resources>
```
* also make sure to have a valid Google Maps API token also available via resources file like:
```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="GOOGLE_MAPS_API_TOKEN">YOUR_TOP_SECRET_GOOGLE_TOKEN</string>
</resources>
```

## Local Node Server
* can be found in folder /server
* prepare the app with "npm install" inside the server folder
* make sure you place a config file inside the server folder like:
```json
{
  "app_id": "PUSHER_APP_ID",
  "key": "PUSHER_APP_KEY",
  "secret": "PUSHER_APP_SECRET",
  "cluster": "PUSHER_CLUSTER",
  "port": "PORT_OF_THE_LOCAL_NODE_SERVER"
}
```
* start it with command "npm start" inside the server folder