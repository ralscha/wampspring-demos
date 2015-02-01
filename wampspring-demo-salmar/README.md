wampspring port of the [spring-websocket-chat](https://github.com/salmar/spring-websocket-chat) sample application
=====================

Chat application using AngularJS, Spring 4 WebSockets, wampspring (WAMP over SockJs)

## Features
- User login
- Chat message broadcasting and private messages (filtering profanities)
- Presence tracking sending notifications when users join / leave
- Broadcast notifications when users are typing
- WebSockets stats exposed at /stats
- WebSocket security

## Running the app

mvn spring-boot:run
