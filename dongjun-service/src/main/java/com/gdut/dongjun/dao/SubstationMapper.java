package com.gdut.dongjun.dao;


import java.util.List;

import com.gdut.dongjun.dao.base.SinglePrimaryKeyBaseMapper;
import com.gdut.dongjun.po.Substation;

public interface SubstationMapper extends SinglePrimaryKeyBaseMapper<Substation> {

	List<String> selectIdByCompanyId(String companyId);
	
	int deleteByIds(List<String> list);
}