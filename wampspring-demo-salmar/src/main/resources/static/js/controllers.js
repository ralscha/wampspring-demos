'use strict';

/* Controllers */

angular.module('springChat.controllers', ['toaster'])
	.controller('ChatController', ['$scope', '$location', '$interval', 'toaster', function($scope, $location, $interval, toaster) {
		  
		var typing = undefined;
		var wampSession;
		
		$scope.username     = '';
		$scope.sendTo       = 'everyone';
		$scope.participants = [];
		$scope.messages     = [];
		$scope.newMessage   = ''; 
		  
		$scope.sendMessage = function() {
			if($scope.sendTo != "everyone") {
				wampSession.call('publishPrivateMessage', $scope.newMessage, $scope.sendTo);
			}
			else {			
				wampSession.call('publishMessage', $scope.newMessage);
			}
			
			$scope.newMessage = '';
		};
		
		$scope.startTyping = function() {
			// Don't send notification if we are still typing or we are typing a private message
	        if (angular.isDefined(typing) || $scope.sendTo != "everyone") return;
	        
	        typing = $interval(function() {
	                $scope.stopTyping();
	            }, 500);
	        
	        wampSession.publish("/chat.typing", {username: $scope.username, typing: true});
		};
		
		$scope.stopTyping = function() {
			if (angular.isDefined(typing)) {
		        $interval.cancel(typing);
		        typing = undefined;
		        
		        wampSession.publish("/chat.typing", {username: $scope.username, typing: false});
			}
		};
		
		$scope.privateSending = function(username) {
				$scope.sendTo = (username != $scope.sendTo) ? username : 'everyone';
		};
			
		var initWampClient = function() {
			var serverPathUrl = window.location.pathname.substring(0, window.location.pathname.lastIndexOf('/') + 1);
			var wsURL;

			if (window.location.search === '?websocket') {
				wsURL = (window.location.protocol == "https:" ? "wss://" : "ws://") + window.location.host + serverPathUrl + "ws";
			}
			else {
				ab._construct = function(url, protocols) {
					return new SockJS(url);
				};
				wsURL = serverPathUrl + 'wamp';
			}
			
			ab.connect(wsURL, function(session) {				
				wampSession = session;
				wampSession.call('getUsername').then(function(username) {
					$scope.$apply(function () {
						$scope.username = username;
					});
				});
				
				wampSession.call('retrieveParticipants').then(function(participants) {
					$scope.$apply(function () {
						$scope.participants = participants;
					});
				});
				  
				wampSession.subscribe("/chat.login", function(topic, username) {
					$scope.$apply(function () {
						$scope.participants.unshift({username: username, typing : false});
					});
				});
		        	 
				wampSession.subscribe("/chat.logout", function(topic, username) {
					$scope.$apply(function () {
						for(var index in $scope.participants) {
							if($scope.participants[index].username == username) {
								$scope.participants.splice(index, 1);
							}
						}
					});
		        });
		        	 
				wampSession.subscribe("/chat.typing", function(topic, parsed) {
					$scope.$apply(function () {
						if(parsed.username == $scope.username) return;
					  					
						for(var index in $scope.participants) {
							var participant = $scope.participants[index];
							  
							if(participant.username == parsed.username) {
								$scope.participants[index].typing = parsed.typing;
							}
					  	} 
					});
				});
		        	 
				wampSession.subscribe("/chat", function(topic, message) {
					$scope.$apply(function () {
						$scope.messages.unshift(message);
					});
		        });
				  
//				wampSession.subscribe("/user/queue/chat.message", function(message) {
//					var parsed = JSON.parse(message.body);
//					parsed.priv = true;
//					$scope.messages.unshift(parsed);
//		        });
				  
//				wampSession.subscribe("/user/queue/errors", function(message) {
//					toaster.pop('error', "Error", message.body);
//		        });
					
				

			}, function(code, reason) {
				toaster.pop('error', 'Error', 'Connection error ' + reason);
			}, {
				skipSubprotocolCheck: true
			});

		};
		  
		initWampClient();
	}]);
	