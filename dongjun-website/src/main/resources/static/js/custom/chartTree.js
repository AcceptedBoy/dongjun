(function() {
	var chartManager = chart.chartManager || null;
	var chartController = chart.chartController || null;

	var treeModel = function() {
		// 记录上一个节点
		var LastNode = -1;

		return {
			// 管理上一个点击的节点类型的方法
			getLast: function() {
				return LastNode;
			},
			setLast: function(node) {
				LastNode = node;
			}
		};
	}();

	var treeSet = function() {
		var last_value;
		
		var holdTree = [];
		var resultSetting = function () {
			var setting;
			return setting = {
				callback: {
					onClick : treeSet.zTreeOnClick,// 添加节点点击事件回调函数
					//onCheck: treeSet.zTreeOnCheck,
					// beforeCheck: treeSet.zTreeBeforeCheck
				},
				view: {
					showLine : false,
					fontCss : treeSet.getFontCss
				}
			}
		}
		
		return {
			data_filter: function(treeId, parentNode, childNodes) {
				if (!childNodes)
					return null;
				for (var i = 0, l = childNodes.length; i < l; i++) {
					childNodes[i].name = childNodes[i].name.replace(/\.n/g, '.');
				}
				return childNodes;
			},
			update: function(nodes, highlight) {
				var zTree = $.fn.zTree.getZTreeObj("treeDemo");

				for (var i = 0, l = nodes.length; i < l; i++) {
					nodes[i].highlight = highlight;
					zTree.updateNode(nodes[i]);// 调用库函数，高亮显示
				}
			},
			zTreeSearch: function(value) {
				var zTree = $.fn.zTree.getZTreeObj("treeDemo");
				var nodeList;
				if (last_value != null) {
					nodeList = zTree.getNodesByParamFuzzy("name", last_value);
					// Edit 2017年6月22日16:14:16
					if(nodeList.length === 0) {
						nodeList = zTree.getNodesByParamFuzzy("address", last_value);
					}
					this.update(nodeList, false);// 重置状态
				}
				last_value = value;
				nodeList = zTree.getNodesByParamFuzzy("name", value);
				if(nodeList.length === 0) {
					nodeList = zTree.getNodesByParamFuzzy("address", last_value);
				}
				// treeSet.update(nodeList, true);
				treeSet.filterRes(nodeList)
				nodeList = treeSet.changeRes(nodeList)
				treeSet.zTreeSearchRes(nodeList, false, value)
			},
			// 过滤搜索结果
			filterRes: function(res) {
				for(var i = 0; i < res.length; i++) {
					if(!res[i].children) {
						if(res[i].lineId) {
							for(var j = 0; j < res.length; j++) {
								if(res[i].parentName == res[j].name) {
									res[i].noNeed = true
								} else {
									res[i].needChange = true
								}
							}
						}
					}
				}
			},
			// 对做记号的数组项进行对应的修改
			changeRes: function(res) {
				var zTree = $.fn.zTree.getZTreeObj("treeDemo");
				var real = []
				var spNode
				for(var i = 0; i < res.length; i++) {
					if(!res[i].noNeed) {
						real.push(res[i])
					}
				}
				for(var j = 0; j < real.length; j++) {
					if(real[j].needChange) {
						spNode = zTree.getNodesByParamFuzzy("name", real[j].parentName);
						real.splice(j, 1, spNode[0])
					}
				}
				return real
			},
			// 用于改变zTree内容,写来给zTreeSearch
			zTreeSearchRes: function(result, isBack, val) {
				if(!isBack) {
					$('#searchTool').hide()
					$('#goBack').show()
					$.fn.zTree.getZTreeObj("treeDemo").destroy()
					$.fn.zTree.init($("#treeDemo"), resultSetting(), result)
					// treeSet.hideCheck()
					// 高亮显示
					var zTree = $.fn.zTree.getZTreeObj("treeDemo");
					var list = zTree.getNodesByParamFuzzy("name", last_value);
					// Edit 2017年6月22日16:14:16
					if(list.length === 0) {
						list = zTree.getNodesByParamFuzzy("address", last_value);
					}
					treeSet.update(list, true); // 调用函数进行高亮显示
				} else {
					$('#searchTool').show()
					$('#goBack').hide()
					$.fn.zTree.getZTreeObj("treeDemo").destroy()
					$.fn.zTree.init($("#treeDemo"), resultSetting(), holdTree)
					// treeSet.hideCheck()
				}
			},
			
			getFontCss: function(treeId, treeNode) {
				return (!!treeNode.highlight) ? {
						color : "#5C297F",
						"font-weight" : "bold"
					} : {
						color : "#999999",
						"font-weight" : "bold"
					};
			},
			setTree: function(zTreeNodeType) {
				var setting = {
					async: {
						enable : true,
						type: 'get',
						url : "/dongjun/switch_tree",// 数据源
						// url: 'http://127.0.0.1:8080/templates/manager/loadTree',
						autoParam : [ "id", "name=n", "level=lv" ],
						otherParam : {
						 	"type" : zTreeNodeType,// 0低压 1高压 2管测
						},
						dataFilter : treeSet.data_filter,// 添加节点名称过滤器
					},
					callback: {
						onAsyncSuccess : treeSet.zTreeOnAsyncSuccess,// 添加动态加载成功回调函数
						onClick: treeSet.zTreeOnClick
					},
					view: {
						showLine : false,
						fontCss : treeSet.getFontCss,
						nameIsHTML: true
					},
				};
				return setting;
			},
			zTreeOnAsyncSuccess: function(event, treeId, treeNode, msg) {
				chartController.initListener();
				$('#back').click(function() {
					treeSet.zTreeSearchRes(null, true)
				})
				holdTree = JSON.parse(msg)
			},
			zTreeOnClick: function(event, treeId, treeNode) {
				chartManager.newHandleClick(event, treeId, treeNode)
			},
			init: function() {
				$.fn.zTree.init($("#treeDemo"), this.setTree(1));

				$("#searchNode").click(function() {
					treeSet.zTreeSearch($("#search_node_key").val());
				});

				// 添加回车事件
				$(document).keydown(function(event) {
					var e = event || window.event;
					if(e.keyCode == 13 && !!$('#search_node_key').val()) {
						treeSet.zTreeSearch($("#search_node_key").val());
					}
				});
			}
		};
	}();
	
	treeSet.init();
}());