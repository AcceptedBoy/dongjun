;(function () {
  // Object.assign Polyfill
  if (typeof Object.assign != 'function') {
    Object.assign = function(target) {
      'use strict';
      if (target == null) {
        throw new TypeError('Cannot convert undefined or null to object');
      }
  
      target = Object(target);
      for (var index = 1; index < arguments.length; index++) {
        var source = arguments[index];
        if (source != null) {
          for (var key in source) {
            if (Object.prototype.hasOwnProperty.call(source, key)) {
              target[key] = source[key];
            }
          }
        }
      }
      return target;
    };
  }
  
  /**
   * 普通事件总线（观察者）
   * 通过Moduler.Bus获取
   */
  function Bus () {
    this.__message = {}
  }
  
  Bus.prototype = {
    constructor: Bus,
    on: function (type, fnc) {
      if(!this.__message[type]) {
        this.__message[type] = [fnc]
      } else {
        this.__message[type].push(fnc)
      }
    },
    emit: function (type, args) {
      if(!this.__message[type]) {
        console.warn('没有事件 '+ type)
        return
      }
      this.__message[type].forEach(function(fnc) {
        fnc(args, this.emit.bind(this))
      }, this)
    }
  }
  
  /**
   * Moduler内的事件观察者
   * 
   * @param {any} modules Moduler.modules
   * @param {any} extraObs 嵌套观察者, 如moduler里嵌套一个moduler，上层观察者往下传
   */
  function Observer (modules, extraObs) {
    this.__message = {
      // afterActive: []
    }
    this.__modules = modules
    this.extraObs = extraObs || null
  }

  Observer.prototype = {
    constructor: Observer,
    on: function (type, moduleName, fnc) {
      var ev = {
        m: moduleName,
        fnc: fnc
      }
      if(!this.__message[type]) {
        this.__message[type] = [ev]
      } else {
        this.__message[type].push(ev)
      }
      return this
    },
    emit: function(type, args) {
      if(!this.__message[type]) {
        if (!this.extraObs) {
          console.warn('没有事件 '+ type)
          return this
        } else {
          // 触发上层的moduler观察者
          return this.extraObs.emit.apply(this.extraObs, arguments)
        }
      }
      this.__message[type].forEach(function(ev) {
        ev.fnc(this.__modules[ev.m], args, this.emit.bind(this))
      }, this)
      return this
    },
    onObj: function (handlers, moduleName) {
      for(var event in handlers) {
        // if (event)
        this.on(event, moduleName, handlers[event])
      }
    }
  }

  /**
   * Moduler
   * 
   * @param {any} supObs 
   */
  function Moduler (supObs) {
    this.modules = {}
    this.__store = {}
    this.hook = {
      afterActive: {}
    }
    this.observer = new Observer(this.modules, supObs)
  }

  Moduler.prototype = {
    constructor: Moduler,
    store: function (name, data) {
      switch (arguments.length) {
        case 0: 
          return this.__store
        case 1: 
          return this.__store[name]
        case 2: 
          if (this.__store[name]) {
            Object.assign(this.__store[name], data)
          } else {
            this.__store[name] = data
          }
      }
    },
    /**
     * 添加module, 需init()
     * @param moduleSet {
     *                    name: 'name',
     *                    module (observer, store) { return Obj },
     *                    on: {
     *                      customEvent (moduleSelf, args, emit) {}
     *                    }
     *                  }
     */
    add: function (moduleSet) {
      var name = moduleSet.name
      this.modules[moduleSet.name] = moduleSet.module
      this.hook[name] = moduleSet.hook || {}
      this.observer.onObj(moduleSet.on, name)
    },
    /**
     * 加载添加的模块
     */
    init: function () {
      var modules = this.modules
      for (var m in modules) {
        // modules[m] = typeof modules[m] === 'function' ? modules[m](this.observer, this.__store) : modules[m]
        // if (modules[m] instanceof Moduler) {
        //   var M = modules[m]
        //   Moduler.__passEvs(this, M)
        //   M.init()
        // }
        modules[m] = this.__active(modules[m], m)
        
      }
    },
    /**
     * 添加后直接激活，在init后使用
     */
    module: function (moduleSet) {
      var name = moduleSet.name
      this.observer.onObj(moduleSet.on, name)
      this.hook[name] = moduleSet.hook || {}
      this.modules[name] = this.__active(moduleSet.module, name)
    },
    __active: function (m, mName) {
      m = typeof m === 'function' ? m(this.observer, this.__store, this.__modules) : m
      if (m instanceof Moduler) {
        Moduler.__passEvs(this, m)
        m.init()
      }
      var cb = this.hook[mName].afterActive
      cb && cb(m, this.observer, this.__store)
      return m
    },
    /**
     * 触发本模块的事件
     */
    emit: function (type, agrs) {
      this.observer.emit(type, agrs)
    }
  }

  /**
   * Moduler静态方法，用于传入observer
   */
  Moduler.__passEvs = function (sup, sub) {
    if (sup instanceof Moduler && sub instanceof Moduler) {
      sub.observer.extraObs = sup.observer
    }
  }
  
  /**
   * 事件总线  
   * let bus = new Moduler.Bus()
   * bus.on('event', (args, emit)=> {
   *  // do someting
   * })
   * 
   * bus.emit('event', { id: '001' })
   */
  Moduler.Bus = Bus
  
  window.Moduler = Moduler
}())