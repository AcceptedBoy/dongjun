(function(){
	var router = new dj.Router()
	router.start({
		'': updateContainer.bind(null, 'low_voltage.html', loadLowVoltage),
		'low_voltage': updateContainer.bind(null, 'low_voltage.html', loadLowVoltage),
		'all_event': updateContainer.bind(null,'all_switch_event.html', hasChange)
	})

	/**
	 * 改变容器中的内容
	 * @param  {String} url 组件html
	 * @param  {Function} fnc 回调函数
	 * @return 
	 */
	function updateContainer(url, fnc){
		dj.inserCmp(url, $('.span10.container').get(0), fnc)
	}

	function low_voltage(){
		updateContainer('event/low_voltage.html', loadLowVoltage)
	}

	function loadLowVoltage(){
		dj.jsLoad('../js/event/low_voltage.js', function(){
			console.log('load')
		})
	}

	function hasChange(){
		console.log('change')
	}
})()