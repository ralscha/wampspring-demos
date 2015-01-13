var path = window.location.pathname.substring(0, window.location.pathname.lastIndexOf('/')+1);
var wsURL;

if (window.location.search === '?websocket') {
	wsURL = (window.location.protocol == "https:" ? "wss://" : "ws://") + window.location.host + path + "../ws";    
}
else {
	ab._construct = function(url, protocols) {
		return new SockJS(url);
	};	
	wsURL = path + '../wamp';
}