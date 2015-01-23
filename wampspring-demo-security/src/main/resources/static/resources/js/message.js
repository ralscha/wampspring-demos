function ApplicationModel(session) {
	var self = this;

	self.friends = ko.observableArray();
	self.username = ko.observable();
	self.conversation = ko.observable(new ImConversationModel(session, this.username));
	self.notifications = ko.observableArray();

	self.connect = function() {

//		console.log('Connected ' + frame);
//		self.username(frame.headers['user-name']);

		// self.friendSignin({"username": "luke"});

		session.subscribe("/user/queue/errors", function(message) {
			self.pushNotification("Error " + message.body);
		});
		
		session.call("/users").then(function(friends) {
			for (var i = 0; i < friends.length; i++) {
				self.friendSignin({
					"username": friends[i]
				});
			}
		}, function(error, desc) {
			console.log('call /users: ', desc);
		});

		session.subscribe("/signin", function(topic, friend) {
			self.friendSignin(new ImFriend({
				"username": friend
			}));
		});
		session.subscribe("/signout", function(topic, friend) {
			self.friendSignout(new ImFriend({
				"username": friend
			}));
		});
		session.subscribe("/messages", function(topic, message) {
			self.conversation().receiveMessage(message);
		});

	}

	self.pushNotification = function(text) {
		self.notifications.push({
			notification: text
		});
		if (self.notifications().length > 5) {
			self.notifications.shift();
		}
	}

	self.logout = function() {
		session.disconnect();
		window.location.href = "../logout.html";
	}

	self.friendSignin = function(friend) {
		self.friends.push(friend);
	}

	self.friendSignout = function(friend) {
		var r = self.friends.remove(function(item) {
			item.username == friend.username
		});
		self.friends(r);
	}
}

function ImFriend(data) {
	var self = this;

	self.username = data.username;
}

function ImConversationModel(session, from) {
	var self = this;
	self.session = session;
	self.from = from;
	self.to = ko.observable(new ImFriend('null'));
	self.draft = ko.observable('')

	self.messages = ko.observableArray();

	self.receiveMessage = function(message) {
		var elem = $('#chat');
		var isFromSelf = self.from() == message.from;
		var isFromTo = self.to().username == message.from;
		if (!(isFromTo || isFromSelf)) {
			self.chat(new ImFriend({
				"username": message.from
			}))
		}

		var atBottom = (elem[0].scrollHeight - elem.scrollTop() == elem.outerHeight());

		self.messages.push(new ImModel(message));

		if (atBottom)
			elem.scrollTop(elem[0].scrollHeight);
	};

	self.chat = function(to) {
		self.to(to);
		self.draft('');
		self.messages.removeAll()
		$('#trade-dialog').modal();
	}

	self.send = function() {
		var data = {
			"created": new Date(),
			"from": self.from(),
			"to": self.to().username,
			"message": self.draft()
		};
		session.call("/im", data);

		self.draft('');
	}
};

function ImModel(data) {
	var self = this;

	self.created = new Date(data.created);
	self.to = data.to;
	self.message = data.message;
	self.from = data.from;
	self.messageFormatted = ko.computed(function() {
		return self.created.getHours() + ":" + self.created.getMinutes() + ":" + self.created.getSeconds() + " - " + self.from + " - " + self.message;
	})
};

