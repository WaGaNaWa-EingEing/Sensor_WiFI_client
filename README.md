# Sensor_WiFI_client

WiFi_client application repo for turm_project.

### Releases

An APk for android is available on over android 7.0 

### Development

An application that determines the current floor number based on the mac address of the currently measured WiFi RSSI values with a predefined mac address list, sends the current floor information and RSSI value array measured at the current location to the server, and displays the predicted location of the response from the server.

Data Send protocal : HTTP POST(application/json)

Sending JSON Data TYPE Example : 

{ "Floor": 5,  "macs": [ -92,    -95,    0,    -89,    -86,    -63,    -70,    -54,    -63,    -69,    -63,    -77,    -71,    0,    -80,    -73,    0,    0,    0,    0,    0,    0,  ~~~~~~~~~]}

Server respond JSON Data TYPE Example : 

{ "Result": "[510]"}

### Building

The project was created using Android Studio, so we recommend building using Android Studio.
