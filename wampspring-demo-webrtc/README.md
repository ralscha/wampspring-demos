To start a WebRTC connection there need to be two users connected to the chat. In Chrome you could start two browser instances
and connect with two different names to the chat.  
<br>
One user selectes the other user. If both clients are WebRTC capable the button "Start Peer-to-Peer Connection" becomes enabled.
If everything goes right you should see your video on the left and the video from the other user on the right side.
<br>  
The SDP and ICE messages between the two peers are exchanged with WAMP. As soon as the connection is established it's a peer to peer connection that 
no longer needs a server. For more information about WebRTC see the article on <a href="http://www.html5rocks.com/en/tutorials/webrtc/basics/">html5rocks</a>.
<br>
The peer-to-peer part should also work over the internet even if both users are behind a NAT/Firewall. The application uses a STUN server (stun.stunprotocol.org) for external ip and port discovery.
But it may not work everywhere and everytime. When NAT 'hole punching' fails it needs a TURN server to relay the traffic, but I haven't found a free and public available TURN server that I could use for this demo.