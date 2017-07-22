var dj = {
  Router: function(level) {
    this.level = level > 0 ? level : 1
    this.start = function(obj) {
      var fn = location.hash.split('#')[this.level]
      try {
        fn = ( fn ? fn : '' )
        obj[fn]()
      } catch (err) { 
        console.log('没有对应的路由哦')
      }
      window.onhashchange = function() {
        var url = location.hash
        if(url) {
          url = url.split('#')[this.level]
        } else {
          url = ''
        }
        for(var route in obj) {
          if(route == url) {
            obj[route]()
            return
          }
        }
      }.bind(this)
    }
  },
  explainScript: function(node) {
    var script = document.createElement('script');
    var t = document.createTextNode(node.getElementsByTagName('script')[0].text);
    script.appendChild(t)
    node.appendChild(script)
    node.removeChild(node.getElementsByTagName('script')[0])
  },
  inserCmp: function(url, node, fn) {
    var that = this
    if(fetch) {
      fetch(url).then(function(res) {
        return res.text() // 把返回的数据转化为字符串数据
      }).then(function(data) {
        try {
          node.innerHTML = data
          //that.componentIn()
          if(node.getElementsByTagName('script').length != 0) {
            that.explainScript(node) // 把组件中script标签的内容进行渲染解析，
          }
        } catch(err) {
          throw new Error("请传元素节点")
        }
        if(fn) {
          fn()
        }
      })
    } else {
      var xhr = new XMLHttpRequest()
      xhr.open("GET", url, true);
      xhr.onload = function(e) {
        node.innerHTML = this.responseText
      }
      xhr.send()
      fn()
    }
  },
  _cmp: [],
  createComponent: function(tagName, o) {
    var check = this._cmp.filter(function(item) {
      return item.tag == tagName
    })
    if(check.length != 0) return 0
    var co = {}
    co.tag = tagName
    co.template = o.template ? o.template : null // template的优先级最高，如果只有templateUrl则会把其内容转化为template保存下来，
    co.templateUrl = o.templateUrl ? o.templateUrl : null // 避免之后再次使用该组件时，再一次进行请求
    if(co.templateUrl && co.template) {
      console.error('请填入template或templateUrl');
    }
    this._cmp.push(co)
  },
  componentIn: function() { // 自定义的组件标签进行渲染
    var that = this
    this._cmp.forEach(function(data, index) {
      var tag = document.body.getElementsByTagName(data.tag)
      var len = tag.length
      if(len == 0) return 0
      if(data.template) {
        for(var i = 0 ; i < len; i++) {
          tag[0].outerHTML = data.template // getElementsByTagName返回的是动态的htmlcollection
        }
      } else {
        that.loadHtml(data.templateUrl, function(data) {
          that._cmp[index].template = data // 把组件的内容缓存起来，下次运用时不需要再次请求文件
          var parent = tag[0].parentNode   // tag 父节点
          for(var i = 0 ; i < len; i++) {
            if(i == 0) {
              var div = document.createElement('div')  //虚拟一个dom节点，不进行真实的渲染，用它来获得script
              div.innerHTML = data
              var script = document.createElement('script');
              var t = document.createTextNode(div.getElementsByTagName('script')[0].text);
              script.appendChild(t)
              // parent.appendChild(script)           // 在outerHTML前加入脚本，对于需要立即获取元素并
                                                      // 运行的脚本不适用
              div = null
            }
            data = data.replace(/<script.*?>(\s*.*\s*)*<\/script>()/ig,'')  // 去除没用的<script>
            tag[0].outerHTML = data // getElementsByTagName返回的是动态的htmlcollection
          }
          parent.appendChild(script)
        })
      }
    })
  },
  loadHtml: function(url, fn) {
    if(fetch) {
      fetch(url).then(function(res) {
        return res.text()
      }).then(function(data) {
        var o = new Object()
        fn.call(o, data)
      })
    } else {
      var xhr = new XMLHttpRequest()
      xhr.open("GET", url, true);
      xhr.onload = function(e) {
        var o = new Object()
        fn.call(o, this.responseText)
      }
      xhr.send()
    }
  },
  loadFile: function(url, type, callback) {
    if(type == 'image') {
      if(url instanceof Array) {
        url.forEach(function(n, index) {
          if(index == (url.length - 1) ) {
            this.imageLoad(item, callback)
          } else {
            this.imageLoad(item)
          }
        })
      }
    } else if(type == 'js') {
      this.jsLoad(url, callback)  //如果想一组组的JS文件加载，自己写，和上面的思路相似
    }
  },
  imageLoad: function(url, fn) {
    var img = new Image();
    img.src = url;
    img.addEventListener('load', function() {
      if(fn) {
        fn();
      }
    })
  },
  _jsArray: [],
  jsLoad: function(url, fn) {
    if(this._jsArray.indexOf(url) != -1) {
       // do nothing
       fn()
    } else {
      this._jsArray.push(url)
      var script = document.createElement('script');
      script.src = url;
      script.addEventListener('load', function() {
        if(fn) {
          fn()
        }
      })
      document.body.appendChild(script)
    }
  },
  linkHistory: [],
  cssLoad: function(url, cb){
    if(this.linkHistory.indexOf(url) != -1) {
      if(cb) {
        cb()
      }
      return
    }
    this.linkHistory.push(url)
    var link = document.createElement('link')
    link.type = 'text/css'
    link.rel = 'stylesheet'
    if(cb) {
      link.addEventListener('load', function() {
        cb()
      })
    }
    link.href = url
    document.getElementsByTagName('head')[0].appendChild(link)
  }
}

