# 东君项目


---

## 项目结构
目录|功能
----|----
`dongjun-hardware`| 设备报文硬件解析端 
`dongjun-platform`| 网页web端的平台版 
`dongjun-website`| 网页web端的企业版 
`dongjun-website-hardware`| 企业版的硬件解析端
`dongjun-service`| 部署在阿里云上的中央管理 
`dongjun-simulate`| 可模拟数据报文的发送 


## dongjun-service
这个模块是基于老师的需求：

* 老师要看到有多少个县城已经部署了我们的系统（很久很久以前的了）

* 县城上的设备添加后，要有一个平台来作为设备的验证，验证后设备才能够在系统使用监听

基于这两个需求，多了这个模块，可以部署在阿里云上，一旦以后`website`或者`hardware`部署后，就要通过cxf进行服务的交互，来实现上述的需求

> 注意：验证的功能是在前端实现的，前端使用nodejs技术，这个模块只是暴露服务而已。因为没有投入使用，所以缺少测试。

## dongjun-simulate
这个模块可以模拟设备的发送，具体实现是以日志为基础的，目前只嵌入了高压设备的模拟；一些模拟工作可以在这个模块做。

* 高压设备数据发送模拟启动所在的main为`com.gdut.dongjun.main.SimulateClient`



## 需要补充

文档尚缺，需要继续补充的都可以写进来，方便以后的开发





