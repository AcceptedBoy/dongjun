var Random = Mock.Random;
var localhost = 'http://127.0.0.1:8080/templates'

Mock.mock(`${localhost}/isLogin`, function(op) {
	console.log(`请求数据: ${op.body}`)
	console.log(`请求数据: ${op.type}`)
	var datas = []
	return {
		data: true
	}
})

Mock.mock(`${localhost}/switch/list`, function(op) {
	console.log(`请求数据: ${op.body}`)
	console.log(`请求数据: ${op.type}`)
	var datas = [];
	for(var i = 0; i < 40; i++) {
		var data = {}
		var name = Random.name()
		data.deviceNumber = Random.integer(1, 100)
		data.name = name
		data.showName = name
		data.id = 'id' + i
		data.lineId = Random.integer(1, 20)
		data.address = Random.city()
		data.longitude = Random.float(0, 180, 2, 2)
		data.latitude = Random.float(0, 180, 2, 2)
		data.simNumber = Random.integer(100, 200)
		data.inlineIndex = data.id
		data.onlineTime = Random.date('MM-dd')
		data.available = Random.boolean()
		data.expireTime = Random.date('MM-dd')
		var num = Random.integer(1, 10)
		var status = num >= 8 ? '-1' : num >= 5 ? '00' : '01'
		data.status = status
		datas.push(data)
	}
	return {
		text: datas
	}
})

