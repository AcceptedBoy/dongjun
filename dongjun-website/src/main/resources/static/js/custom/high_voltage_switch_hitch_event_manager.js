var myState = 'high';
$(document).ready(function() {
	/**
	 * 初始化列表
	 */
/*	$("#hitch_event_list").DataTable({  // 初始化表格
		"destroy": true,
		"ajax": {
			"url": 'get_high_voltage_hitch_event_by_switch_id?switchId=75ab61fafa814ce8a587eeb6a6693464'
		},
		"columns" : [ {
			"data" : "hitchTime"
		}, {
			"data" : "hitchReason"
		}, {
			"data" : "hitchPhase"
		}, {
			"data" : "changeType"
		}, {
			"data": "solvePeople"
		}, {
			"data": "solveTime"
		}, {
			"data": "solveWay"
		}],
		'language': {
      'paginate': {
        'next': '下一页',
        'previous': '上一页'
      },
      'emptyTable': '找不到相关数据',
      'zeroRecords': '找不到相关数据'
    },
	});*/

	var highVoltageTable = function() {
	  var table = null;
	  var url = 'get_high_voltage_hitch_event_by_switch_id';
	  return {
	    getTable: function(){
	      return table;
	    },
	    init: function(o) {
	      var _self = this;
	      table = $('#hitch_event_list').DataTable({
	        'destroy': true,// destroy之后才能重新加载
	        'ajax': {
	          'url': url,
	          'type': 'POST',
	          'data': o.data
	        },
	        'columns': [
	          {
	            'data': 'hitchTime',
	            'render': this.renderTime
	          },
	          {
	            'data': 'hitchReason',
	            'render': this.renderReason
	          },
	          {
	            'data': 'hitchPhase'
	          },
	          {
	            "data" : "changeType"
	          },
	          {
	            "data": "solvePeople"
	          },
	          {
	            "data": "solveTime"
	          },
	          {
	            "data": "solveWay"
	          }
	        ],
	        'language': {
	          'paginate': {
	            'next': '下一页',
	            'previous': '上一页'
	          },
	          'emptyTable': '找不到相关数据',
	          'zeroRecords': '找不到相关数据'
	        },
	      })
	    },
	    redraw: function(id) {
	    	if(!table) {
	    		this.init({
	    			data: {
	    				switchId: id == -1 ? undefined : id
	    			}
	    		})
	    		return
	    	}
	      if(id == -1){
	        // console.log('clear')
	        table.clear().draw()
	        return
	      }
	      // post
	      table.ajax.url(url);
	      var param = {
	        switchId: id
	      };
	      table.settings()[0].ajax.data = param; // param because the seleteManager, maybe i should get a better idea
	      table.ajax.reload();
	    },
	    renderReason(data) {
	      var reason = data;
	      switch(parseInt(data)) {
	        case 1: reason = '剩余电流'; break;
	        case 2: reason = '缺零'; break;
	        case 3: reason = '过载'; break;
	        case 4: reason = '短路'; break;
	        case 5: reason = '缺相'; break;
	        case 6: reason = '欠压'; break;
	        case 7: reason = '过压'; break;
	        case 8: reason += '接地'; break;
	        case 9: reason = '停电'; break;
	        case 10: reason = '定时试验'; break;
	        case 11: reason = '远程'; break;
	        case 12: reason = '闭锁'; break;
	        case 13: reason = '手动'; break;
	        case 14: reason = '互感器故障'; break;
	        case 15: reason = '合闸失败'; break;
	        case 16: reason = '设置失败'; break;
	      }
	      return reason;
	    },
	    renderTime(time) {
	      var date = new Date(time);
	      var y = date.getFullYear();
	      var mt = date.getMonth() + 1;
	      mt = mt < 10 ? ('0' + mt) : mt;
	      console.log(`month: ${m}`)
	      var d = date.getDate();
	      d = d < 10 ? ('0' + d) : d;
	      var h = date.getHours();
	      h = h < 10 ? ('0' + h) : h;
	      var m = date.getMinutes();
	      m = m < 10 ? ('0' + m) : m;
	      var s = date.getSeconds()
	      s = s < 10 ? ('0' + s) : s;
	      return y + '-' + mt + '-' + d + ' ' + h + ':' + m + ':' + s
	    }
	  }
	}()
	// highVoltageTable.init()

	window.afterLoadSwitch = function(switchData) {
		var switchId = -1
		if(switchData.length != 0) {
			switchId = switchData[0].id
		}
		highVoltageTable.redraw(switchId)
	}

	loadSubstationSet();

	/**
	 * 绑定线路的切换
	 */
	$(".lines").change(function() {
		loadSwitchListWithLineId(
			"high_voltage_switch_list_by_line_id",
			$(".lines").val());
	});

	$("#switchs").change(function() {
		highVoltageTable.redraw(this.value)
	});

	$('#searchType').change(function() {
		switch(this.value) {
			case '变电站': fuzzySearchHandler(0); break;
			case '线路': fuzzySearchHandler(1); break;
			case '设备': fuzzySearchHandler(2); break;
		}
	})
});

function loadEventListWithSwitchId(_url, switchId) {
	$('#hitch_event_list').DataTable(
			{
				"destroy" : true,// destroy之后才能重新加载
				"ajax" : _url + switchId,
				"columns" : [ {
					"data" : "hitchTime"
				}, {
					"data" : "hitchReason"
				}, {
					"data" : "hitchPhase"
				}, {
					"data" : "changeType"
				}, {
					"data": "solvePeople"
				}, {
					"data": "solveTime"
				}, {
					"data": "solveWay"
				}],
				'language': {
		            'paginate': {
		              'next': '下一页',
		              'previous': '上一页'
		            },
		            'emptyTable': '找不到相关数据',
		            'zeroRecords': '找不到相关数据'
		          },
			});
}
