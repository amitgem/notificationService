Universal notification service 
===========================
Any integrated application/system should be able to post an event for distribution and mark any undistributed event as irrelevant, so that it is no longer displayed to the users 

Events should be targeted both to individual users or to groups or roles. 

Universal notification service uses the events for creation and distribution of alerts, notifications, warnings, confirmations, simple approval requests etc. 

Multi channel - the events should be distributed using multiple channels - through any of integrated services' web UIs, email, company instant messenger, SMS etc. 

Authentication
==============
The REST API is protected by an API-key authentication filter. Configure
producer clients with the `notification.api.keys` system property or the
`NOTIFICATION_API_KEYS` environment variable:

`clientId:apiKey[:role1,role2];anotherClient:anotherKey`

When roles are omitted, the client is granted `EVENT_PRODUCER`. Requests must
include `X-Client-Id` and `X-API-Key` headers. Authenticated producer requests
can create and update events under `/api/event`.


Docker
======
Build the WAR and Docker image:

`./mvnw -Pdocker package`

The Maven `docker` profile builds a Tomcat-based image named
`notification-service:1.0-SNAPSHOT`. Run it with:

`docker run --rm -p 8080:8080 notification-service:1.0-SNAPSHOT`

The service is deployed as the Tomcat root web application, so the REST API is
available under `http://localhost:8080/api/...`.


Technology limitations
=======================
- Use Java. 
- Whole solution to be deployed internally. 
- Support for (almost) zero-downtime upgrades.
- Use any opensource tools/libraries/frameworks
