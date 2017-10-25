$('#all_event').DataTable({
	'destroy': true,
	'ajax': {
		url: 'get_all_high_event_by_time'
	},
	'columns' : [ {
    'data': 'switchName'
  }, {
		'data' : "hitchTime"
	}, {
		'data' : "hitchReason"
	}, {
		'data' : "hitchPhase"
	}, {
		'data' : "changeType"
	}, {
		'data': "solvePeople"
	}, {
		'data': "solveTime"
	}, {
		'data': "solveWay"
  }],
  'deferRender': true,
  'order': [[ 1, 'desc' ]],
  'language': {
    'paginate': {
      'next': '下一页',
      'previous': '上一页'
    },
    'emptyTable': '找不到相关数据',
    'zeroRecords': '找不到相关数据'
  },
})