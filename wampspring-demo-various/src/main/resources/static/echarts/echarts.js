function getChartOption(name, threshold) {
	return {
		series: [ {
			startAngle: 180,
			endAngle: 0,
			center: [ '50%', '90%' ],
			radius: 100,
			min: 0,
			max: 30,
			name: 'Serie',
			type: 'gauge',
			splitNumber: 3,
			data: [ {
				value: 16,
				name: name
			} ],
			title: {
				show: true,
				offsetCenter: [ '-100%', '-90%' ],
				textStyle: {
					color: '#333',
					fontSize: 15
				}
			},
			axisLine: {
				lineStyle: {
					color: [ [ threshold, '#ff4500' ], [ 1, 'lightgreen' ] ],
					width: 8
				}
			},
			axisTick: {
				length: 11,
				lineStyle: {
					color: 'auto'
				}
			},
			splitLine: {
				length: 15,
				lineStyle: {
					color: 'auto'
				}
			},
			detail: {
				show: true,
				offsetCenter: [ '100%', '-100%' ],
				textStyle: {
					color: 'auto',
					fontSize: 25
				}
			}

		} ]

	};
}

var gauges = [ echarts.init(document.getElementById('chart1')).setOption(getChartOption('s1', 0.1)), 
               echarts.init(document.getElementById('chart2')).setOption(getChartOption('s2', 0.2)), 
               echarts.init(document.getElementById('chart3')).setOption(getChartOption('s3', 0.7)), 
               echarts.init(document.getElementById('chart4')).setOption(getChartOption('s4', 0.5)), 
               echarts.init(document.getElementById('chart5')).setOption(getChartOption('s5', 0.9)) ];

ab.connect(wsURL, function(session) {
	session.subscribe("echarts", function(topic, data) {
		for (var d = 0; d < 5; d++) {
			var o = gauges[d].getOption();
			o.series[0].data[0].value = data[d];
			gauges[d].setOption(o, true);
		}

	});
}, function(code, reason) {
	console.log("Connection lost (" + code + ":" + reason + ")");
}, {
	skipSubprotocolCheck: true
});
