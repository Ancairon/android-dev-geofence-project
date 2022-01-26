# androidDev-geofence-project
This is a project made for the 2021-22 class on AndroidDev at Harokopio University of Athens, Department of Informatics and Telematics.

It is made to share data with https://github.com/Ancairon/android-dev-geofenceproject-consumer.
This app requests the location of the user and lets him set circular regions in his map. When he walks inside one such region(geofence),
then the action (Enter/Exit) is saved in a RoomDatabase. It also has a ContentProvider to share data with the consumer app, which displays these actions. 

> *Note, a working Google Maps API key is needed to work, you need to replace it in the manifest. Check [here](https://console.cloud.google.com/) for more info*
