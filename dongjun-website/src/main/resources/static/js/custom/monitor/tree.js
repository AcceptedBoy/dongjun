var treeModel = function() {
	var checkNodes = [];
	return {
		get: function() {
			return checkNodes;
		},
		addCheck: function(node) {
			checkNodes.push({
				node: node,
				tag: 0
			});
			// console.log(checkNodes);
		},
		delCheck: function(node) {
			for(var i = 0; i < checkNodes.length; i++) {
				if(node.id == checkNodes[i].node.id) {
					checkNodes.splice(i, 1);
					break;
				}
			}
			// console.log(checkNodes);
		},
		clearTag: function() {
			if(checkNodes.length != 0) {
				console.log('clear')
				for(var i = 0; i<checkNodes.length; i++) {
				  checkNodes[i].tag = 0;
				}

			}
		},
	};
}();
var treeSet = function() {
	var zTree = null
	var last_value;
	var isLoad = false;
	var holdTree = [];
	var resultSetting = function () {
		var setting;
		return setting = {
			callback: {
				onClick : treeSet.zTreeOnClick,// 添加节点点击事件回调函数
				onCheck: treeSet.zTreeOnCheck,
				beforeCheck: treeSet.zTreeBeforeCheck
			},
			view: {
				showLine : false,
				fontCss : treeSet.getFontCss
			},
			check: {
				enable: true,
				chkboxType: {'Y':'', 'N':''}
			}
		}
	}
	return {
		getTree: function() {
			if(zTree) {
				return zTree;
			} else {
				return false;
			}
		},
		set: function(ztree) {
			// console.log(ztree)
			if(ztree && typeof ztree == 'object') {
				zTree = ztree;
			} else {
				return false;
			}
		},
		getLoadState: function() {
			return isLoad;
		},
		setLoadState: function(state) {
			if(typeof state == 'boolean') {
				isLoad = state;
			}
		},
		data_filter: function(treeId, parentNode, childNodes) {
			if (!childNodes)
				return null;
			for (var i = 0, l = childNodes.length; i < l; i++) {
				childNodes[i].name = childNodes[i].name.replace(/\.n/g, '.');
			}
			return childNodes;
		},
		update: function(nodes, highlight) {
			var zTree = $.fn.zTree.getZTreeObj("treeDemo")
			if(!(nodes instanceof Array)){
				nodes.highlight = highlight;
				zTree.updateNode(nodes)		// 调用库函数，高亮显示
				return
			}
			for (var i = 0, l = nodes.length; i < l; i++) {
				nodes[i].highlight = highlight;
				zTree.updateNode(nodes[i]);// 调用库函数，高亮显示
			}
		},
		hideCheck: function() {
			if(zTree) {
				var node = zTree.getNodes();
				var nodes = zTree.transformToArray(node);
				for(var i = 0; i<nodes.length; i++) {
					nodes[i].nocheck = true;
					if(!nodes[i].isParent && nodes[i].getParentNode().nocheck == true) {
						nodes[i].getParentNode().nocheck = false;
					}
					zTree.updateNode(nodes[i]);
				}
			} else {
				return false;
			}
		},
		// 用于树搜索的方法
		zTreeSearch: function(value) {
			var zTree = $.fn.zTree.getZTreeObj("treeDemo");
			var nodeList;
			if(value.length === 0) {
				return
			}
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
			// Edit 2017年6月22日16:14:16
			if(nodeList.length === 0) {
				nodeList = zTree.getNodesByParamFuzzy("address", last_value);
			}
			treeSet.filterRes(nodeList)
			nodeList = treeSet.changeRes(nodeList)
			treeSet.zTreeSearchRes(nodeList, false, value)
//			treeSet.update(nodeList, true);
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
				treeSet.hideCheck()
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
				treeSet.hideCheck()
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
					url : "switch_tree",// 数据源
					// url: 'http://127.0.0.1:8080/templates/monitor/loadTree',
					autoParam : [ "id", "name=n", "level=lv" ],
					otherParam : {
						"type" : zTreeNodeType,// 0低压 1高压 2管测
					},
					dataFilter : treeSet.data_filter,// 添加节点名称过滤器
				},
				callback: {
					onAsyncSuccess : treeSet.zTreeOnAsyncSuccess,// 添加动态加载成功回调函数
					onClick : treeSet.zTreeOnClick,// 添加节点点击事件回调函数
					onCheck: treeSet.zTreeOnCheck,
					beforeCheck: treeSet.zTreeBeforeCheck
				},
				view: {
					showLine : false,
					fontCss : treeSet.getFontCss
				},
				check: {
					enable: true,
					chkboxType: {'Y':'', 'N':''}
				}
			};
			return setting;
		},
		zTreeBeforeCheck: function(treeId, treeNode) {
			var node = treeNode
			if(!node.checked && zTree) {
				if(zTree.getCheckedNodes().length >= 7) {
					return false
				}
			}
		},
		zTreeOnAsyncSuccess: function(event, treeId, Tnode, msg) {
			// 缓存起树上的数据用于返回时使用
			holdTree = JSON.parse(msg)
			if(!isLoad) {
				var locationId = djMap.Model.location('switchId')
				if(locationId != -1) {
					var locateNode = zTree.getNodesByParamFuzzy("id", locationId)[0]
					// console.log(locateNode)
					if(!!locateNode){
						djMap.Model.location({
							lng: locateNode.longitude,
							lat: locateNode.latitude
						})						
					}
					
				}

				djMap.Control.init({
					longitude: 108.386287,
					latitude: 22.82277,
				})
				isLoad = true
			}
			treeSet.hideCheck();
		},
		zTreeOnClick: function(event, treeId, treeNode) {
			// var node = treeNode;
			// var parentNode, childNodes, parentId, locateNode
			// if(!treeNode.isParent){
			// 	parentNode = treeNode.getParentNode()
			// 	locateNode = treeNode
			// } else {
			// 	parentNode = treeNode
			// }
			// childNodes = parentNode.children
			// parentId = parentNode.id
			// console.log(parentNode.name)
			// djMap.Control.treeNodeClick('add', parentId, childNodes, locateNode, parentNode.name)
			var node = treeNode;
			var parent = {};
			if(!node.isParent) {
				parent = node.getParentNode();
			}
			if(parent && parent.isChoose || node.isChoose) {
				/*if(!node.isParent) {
					djMap.Control.treeNodeClick('move', node);
				} else {
					djMap.Control.treeNodeClick('move', node.children[0])
					
				}*/
				treeSet.flyMove(node)
				return false;
			} else {
				if(zTree.getCheckedNodes().length >= 7) {
					treeSet.flyMove(node)
					return false
				}
				if(!node.isParent) {
//					if(node.getParentNode()) {
						var nodes = node.getParentNode().children
						djMap.Control.treeNodeClick('add', parent.id, nodes, node, parent.name);
						parent.isChoose = true;
						parent.checked = true;
						zTree.updateNode(parent);
//					} else {
//						djMap.Control.treeNodeClick('add', parent.id, nodes, node, parent.name);
//					}
				} else {
					var nodes = node.children;
					if(!nodes[0].isParent) {
						djMap.Control.treeNodeClick('add', node.id, nodes, nodes[0], node.name);
						node.isChoose = true;
						node.checked = true;
						zTree.updateNode(node);
					}
				}
			}
		},
		flyMove: function(node) {
			// console.log('走你')
			if(!node.isParent) {
				djMap.Control.treeNodeClick('move', node);
			} else {
				djMap.Control.treeNodeClick('move', node.children[0])
				
			}
		},
		zTreeOnCheck: function(event, treeId, treeNode) {
			var parentNode, childNodes, parentId, locateNode
			if(!treeNode.isParent){
				parentNode = treeNode.getParentNode()
				locateNode = treeNode
			} else {
				parentNode = treeNode
			}
			parentId = parentNode.id
			if(treeNode.checked) {
				treeModel.addCheck(parentNode);
				childNodes = parentNode.children
				djMap.Control.treeNodeClick('add', parentId, childNodes, locateNode, parentNode.name)
			}
			else {
				treeModel.delCheck(parentNode);
				djMap.Control.treeNodeClick('del', parentId)
			}
		},
		// init这个方法没有被调用过
		init: function() {
			zTree = $.fn.zTree.init($("#treeDemo"), this.setTree(0));
			$("#searchNode").click(function() {
				treeSet.zTreeSearch($("#search_node_key").val());
			});
			$("#zTree_node_type").change(function() {
				$.fn.zTree.getZTreeObj("treeDemo").destroy();
				$.fn.zTree.init($("#treeDemo"), treeSet.setTree(this.value));
			});
		}
	};
}();
// treeSet.init();