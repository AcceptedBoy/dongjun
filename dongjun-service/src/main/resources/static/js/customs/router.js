;(function () {
	var contentDom = document.getElementById('content-container')

	var router = new dj.Router()
	router.start({
		'Test1': Test1,
		'Test2': Test2
	})

	function Test1 () {
		dj.inserCmp('./login/login.html', contentDom)
	}

	function Test2 () {
		$(contentDom).html('test2')
	}


})()

