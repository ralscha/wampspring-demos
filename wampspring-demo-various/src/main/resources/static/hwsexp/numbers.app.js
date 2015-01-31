angular.module('numbers.app', [ 'd3.directives', 'numbers.directives' ]).controller('NumbersCtrl', [ '$scope', function($scope) {

	var absession;

	$scope.numbers = {};
	$scope.running = false;

	$scope.stop = function() {
		if (absession != null) {
			absession.close();
		}
		absession = null;
		$scope.running = false;
	};

	$scope.executeExpression = function() {
		if (absession != null)
			absession.close();

		ab.connect(wsURL, function(session) {
			absession = session;
			console.log("Connected to ", absession);
			$scope.running = true;

			absession.subscribe("hwsexp", function(topic, data) {
				$scope.$apply(function() {
					$scope.numbers = data;
				});
			});

		}, function(code, reason) {
			absession = null;
			console.log("Connection lost (" + reason + ")");
		}, {
			skipSubprotocolCheck: true
		});

	};

} ]);
