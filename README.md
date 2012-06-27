teamlab-api
===========

A simple REST client for teamlab api with few dependencies (HttpClient + Xstream)

The aim is to validate the TeamLab api (http://api.teamlab.com/) :
 - connect to the api and manage authentication
 - list all events
 - put an event

This is done in the unit test TeamLabApiTest (after filling the constructor paramaters)
 
 ```java
    TeamLabApi api = new TeamLabApi("http://{TEAMLAB_INSTANCE_URL}", {TEAMLAB_USER_NAME}, {TEAMLAB_PASSWORD});
 ```