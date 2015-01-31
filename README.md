A collection of sample applications built with 

   * [wampspring 1.1](https://github.com/ralscha/wampspring/)
   * [Spring Framework 4.1.x](http://projects.spring.io/spring-framework/)
   * [Spring Boot 1.2.x](http://projects.spring.io/spring-boot/)
   * [AutobahnJS](http://autobahn.ws/js/reference_wampv1.html)
   * [SockJS](https://github.com/sockjs/sockjs-client)


| Folder                    | Description   | Online  |
| ------------------------- |-------------- | --------|
| [wampspring-demo-various](https://github.com/ralscha/wampspring-demos/tree/master/wampspring-demo-various)   | Various smaller demos wrapped into one Spring Boot webapplication | https://demo.rasc.ch/wampspring-demo-various/ |
| [wampspring-demo-client](https://github.com/ralscha/wampspring-demos/tree/master/wampspring-demo-client)    | Java WAMP client that connects to a Spring Boot server. Client and server use wampspring. |  |
| [wampspring-demo-webrtc](https://github.com/ralscha/wampspring-demos/tree/master/wampspring-demo-webrtc)    | 					  An enhanced chat application with WebRTC.
					  <br>
					  To start a WebRTC connection there need to be two users connected to the chat. In Chrome you could also start two browser instances
					  and connect with two different names to the chat.  
					  <br>
					  Then one user selectes the other user. If both clients are WebRTC capable the button "Start Peer-to-Peer Connection" becomes enabled.
					  If everything goes right you should see your video on the left and the video from the other user on the right side.<br>  
					  The SDP and ICE messages between the two peers are exchanged with WAMP. As soon as the connection is established it's a peer to peer connection that 
					  no longer needs a server. For more information about WebRTC see the article on <a href="http://www.html5rocks.com/en/tutorials/webrtc/basics/">html5rocks</a>.
					  <br>
					  The peer-to-peer part should also work over the internet even if both users are behind a NAT/Firewall. The application uses a STUN server (stun.stunprotocol.org) for external ip and port discovery.
					  But it may not work everywhere and everytime. When NAT 'hole punching' fails it needs a TURN server to relay the traffic, but I haven't found a free and public available 
					  TURN server that I could use for this demo. | https://demo.rasc.ch/wampspring-demo-webrtc/ |
| [wampspring-demo-security](https://github.com/ralscha/wampspring-demos/tree/master/wampspring-demo-security)  | Chat application that demonstrates the integration of springwamp with [Spring Session](http://projects.spring.io/spring-session/) and [Spring Security](http://projects.spring.io/spring-security/). Port of the [websocket sample application](https://github.com/spring-projects/spring-session/tree/master/samples/websocket) from [Spring Session](http://projects.spring.io/spring-session/) |  https://demo.rasc.ch/wampspring-demo-security/ |
| [wampspring-demo-simple](https://github.com/ralscha/wampspring-demos/tree/master/wampspring-demo-simple)    | A very simple webapplication. Used as a sandbox for development and testing new features. |  |
