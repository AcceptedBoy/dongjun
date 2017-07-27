var chart = {};

(function() {

function inheritObject(o) {
  function F() {}
  F.prototype = o
  return new F()
}
	/*
	这个模块只有图表建类，需要用原型继承
	 */
function inheritPrototype(subClass, superClass) {
  var p = inheritObject(superClass.prototype)
  p.constructor = subClass
  subClass.prototype = p
}

	/**
	 * [Chart 图表的基类，一个图表包含：折线图，折线图的tab和数据表格，数据表格的tab这些信息]
	 * @param {[type]}  chart     [图表实例]
	 * @param {[type]}  tablename [数据表格的id]
	 * @param {Boolean} isActive  [是否正在显示这个图表类]
	 * @param {[type]}  idConfig  [一些tab的id]
	 */
function Chart(op) {
  this.chart = echarts.init(document.getElementById(op.chart))
  this._chartname = op.chart
  this._tablename = op.tablename
  this._tableTle = op.tableTitle || '',
  this._xFormatter = op.xFormatter
  this._isActive = op.isActive
  this._tableId = op.idConfig.tableId || ''
  this._tabId = op.idConfig.tabId || ''
  this._chartTabId = op.idConfig.chartTabId || ''
  this._excelUrl = op.excelUrl || ''
  this._excelBtn = op.excelBtn || ''
  chartNodes.addChart(this)
}

/**
 * [getChartTabId 返回id，下面那个方法一样]
 * @return {[type]} [description]
 */
Chart.prototype.getChartTabId = function() {
	return this._chartTabId
}
 
Chart.prototype.changeFormatter = function(op) {
	console.log('changeFormatter', op)
  op.yAxis[0].axisLabel = {
    formatter: '{value}' + (this._xFormatter.split('/')[1] || '')
  }
}

Chart.prototype.setUnit = function() {
	$('#' + this._chartname).append('<h4 style="position:absolute; top:0; left:8%">' + this._xFormatter + '</h4>')
}

Chart.prototype.getTabId = function() {
	return this._tabId
}

	/**
	 * [clearChart 用于清空折线图，就是显示空折线图样式，但是里面没数据，如果不这样做，连基本的XY轴都显示不出来]
	 * @return {[type]} [description]
	 */
Chart.prototype.clearChart = function() {
	if(this.chart) {
		this.chart.setOption({
						"title": {
                "text": ""
            },
            "tooltip": {
                "trigger": "axis"
            },
							"grid": {
								"bottom": '8%'
							},
            "legend": {
                "data": []
            },
            "toolbox": {
                "feature": {
                    "saveAsImage": {}
                }
            },
            "grid": {
                "buttom": "",
                "left": "",
                "containLabel": true,
                "right": ""
            },
            "xAxis": [{
                "type": "category",
                "boundaryGap": false,
                "data": []
            }],
            "yAxis": [{
                "type": "value"
            }],
            "series":[]
		}, true)
	}
}

	/**
	 * [handleClick 用于设备树的点击，把当前显示的设备id保存到chartNode，调用本实例的showChart]
	 * @param  {[type]} event    [description]
	 * @param  {[type]} treeId   [description]
	 * @param  {[type]} treeNode [description]
	 * @return {[type]}          [description]
	 */
Chart.prototype.handleClick = function(event, treeId, treeNode) {
		var bgtime = $('#begin_search_date').val()
  var endtime = $('#end_search_date').val()
		chartNodes.setData({
    id: treeNode.id,
    name: treeNode.name
  })
		this.showChart(treeNode.id, treeNode.name, bgtime, endtime)
}

	/**
	 * [setActive 设置可见和不可见，这个主要是用于看折线图时，切换tab于切换数据表格的tab同步]
	 * @param {Boolean} isActive [description]
	 */
Chart.prototype.setActive = function(isActive) {
		if(isActive) {
			$('#' + this._tabId).closest('li').addClass('active')
			$('#' + this._tableId).addClass('active')
		} else {
			$('#' + this._tabId).closest('li').removeClass('active')
			$('#' + this._tableId).removeClass('active')
		}
  this._isActive = isActive
}

	/**
	 * [bindClick 在看数据表格的时候，切换tab与切换折线图的tab同步起来。（这里用了直接出发折线图的tab的点击事件来做同步）]
	 * @return {[type]} [description]
	 */
	Chart.prototype.bindClick = function() {
		$('#' + this._chartTabId).click()
	}

Chart.prototype.getActive = function(isActive) {
  return this._isActive
}

Chart.prototype.showChart = function() {
  console.log('showChart')
}

Chart.prototype.showTable = function() {
  console.log('showTable')
}

Chart.prototype.createTable = function() {
  console.log('createTable')
}

Chart.prototype.setExcel = function(id, bgtime, endtime) {
  $('.' + this._excelBtn).attr('href', this._excelUrl + '?' + 'monitorId=' + id + '&' + 'beginDate=' + bgtime + '&' + 'endDate=' + endtime)
}
	/**
	 * [TemChart 温度表格的构造函数]
	 * @param {[type]}  chart     [description]
	 * @param {[type]}  tablename [description]
	 * @param {Boolean} isActive  [description]
	 * @param {[type]}  idConfig  [description]
	 */
function TemChart(op) {
  Chart.call(this, op)
}

	/**
	 * [EleChart 继承Chart的电能表表格的构造函数，因为电能表的表格比较多，每一个数据的url不一样，
	 * 所以扩展了实例的属性，多加一个url]
	 * @param {[type]}  chart     [description]
	 * @param {[type]}  tablename [description]
	 * @param {Boolean} isActive  [description]
	 * @param {[type]}  idConfig  [description]
	 * @param {[type]}  url       [description]
	 */
function EleChart(op, url) {
  Chart.call(this, op)
		this.url = url
}

	/*
	调用父类构造函数之后，继承Chart的原型。
	 */
inheritPrototype(TemChart, Chart)
inheritPrototype(EleChart, Chart)


	/**
	 * [TemChart 开始重写TemChart的Prototype]
	 * 这个方法用于向后台请求获取折线图的数据，渲染到折线图上，
	 * 然后调用本实例的showTable渲染数据表格数据
	 */
TemChart.prototype.showChart = function(id, name, bgtime, endtime) {
  var self = this
  if(!(bgtime && endtime)) {
    bgtime = $('#begin_search_date').val()
    endtime = $('#end_search_date').val()
  }
  if(name && id) {
    chartView.showLoading(self.chart)
    $.ajax({
      url: '/dongjun/chart/temperature',
      type: 'GET',
      data: {
        monitorId: id,
        beginDate: bgtime,
        endDate: endtime
      },
      success: function(res) {
        if(res) {
          chartView.setChart(res, name)
          self.changeFormatter(res)
          self.chart.setOption(res, true)
          self.setUnit()
          self.showTable()
          self.setExcel(id, bgtime, endtime)
        } else {
          alert('没有数据')
          self.clearChart()
						self.showTable()
        }
      },
				complete: function() {
					chartView.hideLoading(self.chart)
				}
    })
  } else {
    self.clearChart()
			self.showTable()
  }
}

	/**
	 * [createTable 从这个实例的表格里面获取数据，
	 * 构造出数据表格需要的数据]
	 * @return {[type]} [description]
	 */
TemChart.prototype.createTable = function() {
  var op = this.chart.getOption()
  if(op.series[0]) {
    var arr = [];
	// for(var j = 0; j<op.xAxis[0].data.length; j++) {
      // for(var i = 0; i<op.series.length; i++) {
    for(var j = op.xAxis[0].data.length-1; j>=0; j--) {
      for(var i = op.series.length-1; i>=0; i--) {
        arr.push({
          "name": op.legend[0].data[i],
          "time": op.xAxis[0].data[j],
          "temperatrue": op.series[0+i].data[j]
        });
      }
    }
    return arr;
  } else {
    return false
  }
}

	/**
	 * [showTable 使用从createTable里面返回的数据，渲染到数据表格里面]
	 * @return {[type]} [description]
	 */
TemChart.prototype.showTable = function() {
  var data = this.createTable()
  $('.'+this._tableTle).text(chartNodes.getName())
  var self = this
  data = data ? data : []
  var tablename = this._tablename
  $('#' + tablename).DataTable({
    "destroy": true,// destroy之后才能重新加载
    "ordering": false,
    "info":     false,
    "columns" : [{
      "data" : "name"
    }, {
      "data" : "time"
    }, {
      "title": self._xFormatter,
      "data" : "temperatrue",
      "render": function(data, type, row) {
    	  if(data) {
    		  return data + (self._xFormatter.split('/')[1] || '')
    	  } else {
    		  return '<span style="color: red">没有数据</span>'
    	  }
      }
	}],
    "data": data
  });
}


	/**
	 * [EleChart 开始重写EleChart的Prototype]
	 */
	EleChart.prototype.showChart = function(id, name, bgtime, endtime) {
		var self = this
		if(!(bgtime && endtime)) {
			bgtime = $('#begin_search_date').val()
    		endtime = $('#end_search_date').val()
		}
		if(id && name) {
			chartView.showLoading(self.chart)
			// ajax
			$.ajax({
				url: this.url,
				type: 'GET',
      data: {
        monitorId: id,
        beginDate: bgtime,
        endDate: endtime
      },
				success: function(res) {
					if(res) {
						chartView.setChart(res, name)
						self.changeFormatter(res)
          self.chart.setOption(res, true)
          self.setUnit()
          self.showTable()
          self.setExcel(id, bgtime, endtime)
					} else {
						alert('没有数据')
						self.clearChart()
						self.showTable()
					}
				},
				complete: function() {
					chartView.hideLoading(self.chart)
				}
			})
		} else {
			self.clearChart()
			self.showTable()
		}
	}

	EleChart.prototype.createTable = function() {
		var op = this.chart.getOption()
		if(op.series[0]) {
			var arr = []
			for(var j = op.xAxis[0].data.length-1; j>=0; j--) {
	          for(var i = op.series.length-1; i>=0; i--) {
	            arr.push({
	              "name": op.legend[0].data[i],
	              "time": op.xAxis[0].data[j],
	              "eleData": op.series[0+i].data[j]
	            });
	          }
	        }
			return arr
		} else {
			return false
		}
	}

	EleChart.prototype.showTable = function(data) {
		var data = this.createTable()
		$('.'+this._tableTle).text(chartNodes.getName())
		var self = this
		data = data ? data : []
		var tablename = this._tablename
		$('#' + tablename).DataTable({
			"destroy": true,// destroy之后才能重新加载
			"ordering": false,
			"info":     false,
			"columns" : [{
				"data" : "name"
			}, {
				"data" : "time"
			}, {
				"title": self._xFormatter,
				"data" : "eleData",
				"render": function(data, type, row) {
					if(data) {
	        		  return data + (self._xFormatter.split('/')[1] || '')
	        	  } else {
	        		  return '<span style="color: red">没有数据</span>'
	        	  }
				}
			}],
			"data": data
		});
	}

var chartView = function() {
  return {
    handleChange: function() {
      if($('#lineBar').css('display') == 'none') {
        $('#dataBar').hide();
        $('#dataChart').hide();
        $('#lineBar').show();
        $('#lineChart').show();
      } else {
        $('#lineBar').hide();
        $('#lineChart').hide();
        $('#dataBar').show();
        $('#dataChart').show();
      }
    },
    changeIcon: function(treeObj) {
      var node = treeObj.getNodes();
      var nodes = treeObj.transformToArray(node)
      for(var i = 0; i<nodes.length; i++) {
        if(!nodes[i].children) {
          nodes[i].iconSkin = 'IconTem'
          treeObj.updateNode(nodes[i]);
        }
      }
    },
			setTime: function(time) {
				$('#begin_search_date').val(time.bg)
				$('#end_search_date').val(time.end)
			},
    /**
     * [changeChart 隐藏温度显示电压的或者反之]
     * @param  {[type]} toWhat [切去哪里]
     * @return {[type]}        [description]
     */
    changeChart: function(toWhat) {
      if(toWhat == 'temperature') {
        $('#electricBar').hide();
        $('#electricContent').hide();
        $('#temBar').show();
        $('#temContent').show();
      }
      else {
        $('#electricBar').show();
        $('#electricContent').show();
        $('#temBar').hide();
        $('#temContent').hide();
      }
    },
    showLoading: function(chart) {
    	chart.hideLoading()
    	chart.showLoading({
    		effect: 'ring'
    	})
    },
    hideLoading: function(chart) {
    	chart.hideLoading()
    },
			seekTime: function(dataArr) {
				var time = ''
				for(var i = 0; i < dataArr.length; i++) {
					if(dataArr[i].data) {
						time = dataArr[i].name
						break
					}
				}
				return time
			},
			setChart: function(op, text) {
				var self = this
				op.grid = {
					bottom: '8%'
				}
				op.title = {
        text: text,
        x: 'center',
        backgroundColor: 'white',
        borderColor: 'black',
        padding: 13,
        textStyle: {
          color: 'black',
          fontSize: 23
        }
      },
				op.tooltip.formatter = function(data) {
					var time = self.seekTime(data)
					var html = time
					data.forEach(function(item) {
						html += '<br/>' + item.seriesName + ' : ' + item.data
					})
					return html
				}
				op.tooltip.padding = 10
				op.legend.orient = 'vertical'
      op.legend.x = 'right'
      op.legend.y = '25'
			}
  };
}();

var chartController = function() {
  return {
    /**
     * [initListener 为表格模块的东西添加事件]
     * @return {[type]} [description]
     */
    initListener: function() {
      chartView.changeIcon($.fn.zTree.getZTreeObj("treeDemo"))
      chartView.changeChart()
      chartManager.clearChart()
      $("#search_btn").click(function(event) {
        chartManager.showChart()
      })
      $('#dataBar').click(function(event) {
        var targetId = event.target.id;
					chartManager.bindClick(targetId)
      });
      // 使折线图的tab和表格的tab同步
      $('#lineBar').click(function(event) {
        var targetId = event.target.id
					chartManager.setActive(targetId)
        chartManager.showChart()
      });
    }
  };
}();
/**
 * [chartNodes 用来装已选节点的数组]
 * @return {[type]} [description]
 */
var chartNodes = function() {
	var id = ''
    var name = ''
		var chartArr = []

    return {
		addChart: function(obj) {
			chartArr.push(obj)
		},
			setActive: function(targetId) {
				for(var i = 0; i < chartArr.length; i++) {
					if(targetId == chartArr[i].getChartTabId()) {
						chartArr[i].setActive(true)
					} else {
						chartArr[i].setActive(false)
					}
				}
			},
			getChart: function() {
				return chartArr
			},
			getActive: function() {
				// console.log(chartArr)
				var activeChart = null
				for(var i = 0; i < chartArr.length; i++) {
					if(chartArr[i].getActive()) {
						activeChart = chartArr[i]
						break
					}
				}
				return activeChart
			},
		setData: function(option) {
			id = option.id
			name = option.name
		},
		getId: function() {
			if(id) {
				return id
			}
		},
		getName: function() {
			if(name) {
				return name
			}
		},
			getNow: function() {
				var d = new Date()
				return {
					h: d.getHours(),
					m: d.getMinutes()
				}
			},
	}
 }()
/**
* [chartManager 管理表格的行为]
* @return {[type]} [瞎分的...]
*/
var chartManager = function() {

  var temperature = new TemChart({
	  chart: 'temperature',
	  tablename: 't_data_list',
	  tableTitle: 'tTableTle',
	  xFormatter: '温度/°C',
	  excelUrl: '/dongjun/chart/excel/temperature',
	  excelBtn: 'tex',
	  isActive: true,
	  idConfig: {
			tabId: 'showTExcal',
			tableId: 'excalChartT',
			chartTabId: 'showTchart'
		}
  })

  var voltageChart = new EleChart({
	  chart: 'VoltageChart',
	  tablename: 'v_data_list',
	  tableTitle: 'vTableTle',
	  xFormatter: '电压/V',
	  excelUrl: '/dongjun/chart/excel/voltage',
	  excelBtn: 'vex',
	  isActive: false,
	  idConfig: {
			tabId: 'showVExcal',
			tableId: 'excalChartV',
			chartTabId: 'showVchart'
		}
  }, '/dongjun/chart/electronic_voltage')

  var currentChart = new EleChart({
	  chart: 'current',
	  tablename: 'c_data_list',
	  tableTitle: 'cTableTle',
	  xFormatter: '电流/A',
	  excelUrl: '/dongjun/chart/excel/current',
	  excelBtn: 'cex',
	  isActive: false,
	  idConfig: {
			tabId: 'showCExcal',
			tableId: 'excalChartC',
			chartTabId: 'showCchart'
		}
  }, '/dongjun/chart/electronic_current')

  var powerChart = new EleChart({
	  chart: 'power',
	  tablename: 'p_data_list',
	  tableTitle: 'pTableTle',
	  xFormatter: '千瓦/kW',
	  excelUrl: '/dongjun/chart/excel/power',
	  excelBtn: 'pex',
	  isActive: false,
	  idConfig: {
			tabId: 'showPExcal',
			tableId: 'excalChartP',
			chartTabId: 'showPchart'
		}
  }, '/dongjun/chart/electronic_power')

  // 给excel按钮加点击事件
  /* $('.tex').click(function() {
	  var bgtime = $('#begin_search_date').val()
		  var endtime = $('#end_search_date').val()
		  chartManager.downloadExcel(bgtime, endtime, '/dongjun/chart/excel/temperature')
  })
  
  $('.vex').click(function() {
	  var bgtime = $('#begin_search_date').val()
		  var endtime = $('#end_search_date').val()
		  chartManager.downloadExcel(bgtime, endtime, '/dongjun/chart/excel/voltage')
  })
  
  $('.cex').click(function() {
	  var bgtime = $('#begin_search_date').val()
		  var endtime = $('#end_search_date').val()
		  chartManager.downloadExcel(bgtime, endtime, '/dongjun/chart/excel/current')
  })
  
  $('.pex').click(function() {
	  var bgtime = $('#begin_search_date').val()
		  var endtime = $('#end_search_date').val()
		  chartManager.downloadExcel(bgtime, endtime, '/dongjun/chart/excel/power')
  }) */
  
  return {
			refresh: function() {
				chartManager.initail_datetimepicker()
				chartManager.showChart()
			},
    setActive: function(chartId) {
				var data = chartNodes.getChart()
				for(var i = 0; i < data.length; i++) {
					if(chartId == data[i].getChartTabId()) {
						data[i].setActive(true)
					} else {
						data[i].setActive(false)
					}
				}
    },
			bindClick: function(tabId) {
				var data = chartNodes.getChart()
				for(var i = 0; i < data.length; i++) {
					if(tabId == data[i].getTabId()) {
						data[i].bindClick()
					}
				}
			},
			/* downloadExcel: function(bg, end, url) {
				if(chartNodes.getId()) {
				  var id = chartNodes.getId()
				} else {
				  alert('请选择设备')
				  return null
				}
				$.ajax({
					url: url,
					data: {
					  monitorId: id,
					  beginDate: bg,
					  endDate: end,
					}
				})
			}, */
    showChart: function() {
				chartNodes.getActive().showChart(chartNodes.getId(), chartNodes.getName())
    },
    /**
     * [clearChart 可以理解为清空表格]
     * @param  {[type]} chart [目标表格]
     * @return {[type]}       [description]
     */
    clearChart: function(chart) {
				chartNodes.getActive().clearChart()
    },
			newHandleClick: function(event, treeId, treeNode) {
				if(!treeNode.children) {
					chartNodes.getActive().handleClick(event, treeId, treeNode)
				}
      },
    /**
     * [initail_datetimepicker 初始化时间表单]
     * @return {[type]} []
     */
    initail_datetimepicker: function() {
      var timer = new Date();
      $('.form_date').datetimepicker({
        language : 'zh-CN',
        weekStart : 1,
        todayBtn : 1,
        autoclose : true,
        todayHighlight : 1,
        startView : 2,
        minView : 0,
        forceParse : 0,
        format: "yyyy-mm-dd hh:ii",
        minuteStep: 5
      });
      var year = timer.getFullYear()
      var month = timer.getMonth() + 1
      var lastMonth = timer.getMonth()
      var today = timer.getDate()
      var yesterday = today - 1
      yesterday = yesterday.toString()
      var hour = timer.getHours()
      hour = hour>=10 ? hour : '0'+hour
      var minute = timer.getMinutes()
      minute = minute >= 10 ? minute : '0'+minute
      if(Number(hour) >= 12) {
        var lastHour = Number(hour) - 12
        lastHour = lastHour>=10 ? lastHour : '0'+lastHour
        chartView.setTime({
        	bg: year + '-' + month + '-' + today + ' ' + lastHour + ':' + minute,
        	end: year + '-' + month + '-' + today + ' ' + hour + ':' + minute
        })
      } else {
        if(yesterday == 9) {
          yesterday = '0' + yesterday
          lastMonth = lastMonth + 1
        }
        else if((yesterday>0)&&(yesterday<9)) {
          today = '0' + today
          yesterday = '0' + yesterday
          lastMonth = lastMonth + 1
        }
        else if(today == 1) {
          switch(month) {
            case 1:
            case 2:
            case 4:
            case 6:
            case 8:
            case 9:
            case 11: yesterday = '31';break;
            case 5:
            case 7:
            case 10:
            case 12: yesterday = '30';break;
            case 3: yesterday = '28';break;
          }
          today = '0' + today;
        }
        else {
          lastMonth = lastMonth + 1;
        }
        var lastHour = 24 - 12 + Number(hour)
        lastHour = lastHour>=10 ? lastHour : '0'+lastHour;
        chartView.setTime({
        	bg: year + '-' + lastMonth + '-' + yesterday + ' ' + lastHour + ':' + minute,
        	end: year + '-' + month + '-' + today + ' ' + hour + ':' + minute
        })
      }

      timer = null;
    },

  };
}();

chart.chartView = chartView;
chart.chartManager = chartManager;
chart.chartController = chartController;
chartManager.initail_datetimepicker();
}());