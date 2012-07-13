teamlab-api
===========

A VERY simple REST client for teamlab api with few dependencies (HttpClient + Xstream)

The aim is to validate the TeamLab api (http://api.teamlab.com/) :
 - connect to the api and manage authentication
 - list all events
 - put an event
 - test it with a jetty server

You can use the TeamLabApi after filling the constructor paramaters.
 
 ```java
        TeamLabApi api = new TeamLabApi("http://TEAMLAB_URL",
                                        "TEAMLAB_ACCOUNT",
                                        "TEAMLAB_PASSWORD",
                                        new ProxyInformation("PROXY_HOST", 80, "PROXY_USER", "PROXY_PASSWORD")
        );
  ```