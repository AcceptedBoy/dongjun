package com.symon.service;

import com.symon.po.Substation;
import com.symon.service.base.BaseService;

import java.util.List;


/**
 * @Title: UserService.java
 * @Package com.gdut.dongjun.service.system
 * @Description: TODO
 * @author Sherlock-lee
 * @date 2015年7月24日 下午2:34:11
 * @version V1.0
 */
public interface SubstationService extends BaseService<Substation> {

	/**
	 * 
	 * @Title: selectByCompanyId
	 * @Description: TODO
	 * @param @param company_id
	 * @param @return
	 * @return List<Substation>
	 * @throws
	 */
	public List<Substation> selectByCompanyId(String companyId);
}
