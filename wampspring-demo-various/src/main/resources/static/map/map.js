function initialize() {
	var mapOptions = {
		center: new google.maps.LatLng(46.947922, 7.444608),
		zoom: 14,
		mapTypeId: google.maps.MapTypeId.ROADMAP
	};
	var map = new google.maps.Map(document.getElementById("map_canvas"), mapOptions);

	var blueCar = null;
	var redCar = null;

	google.maps.event.addListener(map, "rightclick", function(event) {
		var lat = event.latLng.lat();
		var lng = event.latLng.lng();
		console.log("Lat=" + lat + "; Lng=" + lng);
	});

	var moveCar = function(carColour, latlng) {
		var car = carColour === 'red' ? redCar : blueCar;
		
		if (!car) {
			car = new google.maps.Marker({
				position: new google.maps.LatLng(latlng.lat, latlng.lng),
				icon: 'car_'+carColour+'.png',
				map: map
			});
			if (carColour === 'red') {
				redCar = car;
			}
			else {
				blueCar = car;
			}
		}
		else {
			car.setPosition(new google.maps.LatLng(latlng.lat, latlng.lng));
		}
	}
	
	ab.connect(wsURL, function(session) {
		session.subscribe("/map/blue", function(topic, latlng) {
			moveCar('blue', latlng);
		});
		session.subscribe("/map/red", function(topic, latlng) {
			moveCar('red', latlng);
		});
	}, function(code, reason) {
		console.log("Connection lost (" + code + ":" + reason + ")");
	}, {
		skipSubprotocolCheck: true
	});	

}
