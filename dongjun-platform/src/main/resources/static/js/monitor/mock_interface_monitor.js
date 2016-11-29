var Random = Mock.Random

Mock.mock('http://127.0.0.1:8080/templates/monitor/load', function(){
	return Random.cname()
})
// loadtree
Mock.mock('http://127.0.0.1:8080/templates/monitor/loadTree', function(op) {
    return [{
        "id": "003",
        "name": "思阳所",
        "open": true,
        "children": [{
            "id": "09",
            "name": "35KV三华变电站10KV叫安924线",
            "open": true,
            "children": [{
                "id": "1",
                "name": "1",
                "open": true,
                "children": null ,
                "parentName": "35KV三华变电站10KV叫安924线",
                "lineId": "09",
                "longitude": "1.0",
                "latitude": "1.0",
                "type": 0,
                "address": "1",
                "showName": ""
            }, {
                "id": "2",
                "name": "2",
                "open": true,
                "children": null ,
                "parentName": "35KV三华变电站10KV叫安924线",
                "lineId": "09",
                "longitude": "1.0",
                "latitude": "1.0",
                "type": 0,
                "address": "2",
                "showName": ""
            }, {
                "id": "3",
                "name": "3",
                "open": true,
                "children": null ,
                "parentName": "35KV三华变电站10KV叫安924线",
                "lineId": "09",
                "longitude": "1.0",
                "latitude": "1.0",
                "type": 0,
                "address": "3",
                "showName": ""
            }],
            "parentName": "思阳所",
            "lineId": null ,
            "longitude": null ,
            "latitude": null ,
            "type": 0,
            "address": null ,
            "showName": null
        }],
        "parentName": null ,
        "lineId": null ,
        "longitude": null ,
        "latitude": null ,
        "type": 0,
        "address": null ,
        "showName": null
    }];
});