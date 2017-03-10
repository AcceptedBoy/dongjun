package com.gdut.dongjun.service.aspect;

import javax.annotation.Resource;

import org.aspectj.lang.annotation.After;
import org.springframework.beans.factory.annotation.Autowired;

import com.gdut.dongjun.domain.po.TemperatureDevice;
import com.gdut.dongjun.service.CompanyService;
import com.gdut.dongjun.service.UserService;
import com.gdut.dongjun.service.cache.CacheService;
import com.gdut.dongjun.service.webservice.client.HardwareServiceClient;

/**
 * 利用Spring AOP更新缓存
 * 理解aop
 * <a>http://blog.csdn.net/wangpeng047/article/details/8556800</a>
 * @author Gordan_Deng
 * @date 2017年2月19日
 */
//@Aspect
//@Component
public class CacheAspect {

	@Resource(name = "EhServiceCache")
	private CacheService ehServiceCache;
	@Autowired
	private UserService userService;
	@Autowired
	private CompanyService companyService;
	@Autowired
	private HardwareServiceClient hardwareServiceClient;
	
//    @After("execution(* com.gdut.dongjun.service.base.impl.BaseServiceImpl+.deleteByPrimaryKey(String))"
//            + " && args(substationId) && target(com.gdut.dongjun.service.impl.SubstationServiceImpl)")
//    public void deleteSubstation(String substationId) {
//        if(commonSwitch.canService()) {
//            centorServiceClient.getService().deleteSubstation(substationId);
//        }
//    }

//    @After("execution(* com.gdut.dongjun.service.base.impl.BaseServiceImpl+.insert())"
//    		+ " && target(com.gdut.dongjun.service.impl.DeviceGroupMappingServiceImpl)")
//    public void updateCacheForInsertDeviceGroupMapping() {
//    	companyService.isModifiedChart(user.getCompanyId());
//    }
    
	/**
	 * 更改温度设备阈值时更新缓存
	 * @param device
	 */
    @After("execution(* com.gdut.dongjun.service.base.impl.BaseServiceImpl+.updateByPrimaryKey(*))"
    		+ " && target(com.gdut.dongjun.service.impl.TemperatureDeviceServiceImpl)")
    public void updateCacheForUpdateTemperatureDevice(TemperatureDevice device) {
    	hardwareServiceClient.getService().changeTemperatureDevice(device.getId());
    }
    
//    @After("execution(* com.gdut.dongjun.service.base.impl.BaseServiceImpl+.deleteByPrimaryKey(String))"
//    		+ " && target(com.gdut.dongjun.service.impl.TemperatureDeviceServiceImpl)")
//    public void updateCacheForDeleteTemperatureDevice(String id) {
//    	hardwareServiceClient.getService().changeTemperatureDevice(id);
//    }
	
	
    
}
