package com.gdut.dongjun.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.gdut.dongjun.dao.SubstationMapper;
import com.gdut.dongjun.dao.base.impl.SinglePrimaryKeyBaseDAOImpl;
import com.gdut.dongjun.po.Substation;


/**
 * 
 * @author	Sherlock-lee
 * @date	2015年11月14日 下午4:57:00
 * @see 	TODO
 * @since   1.0
 */
@Repository
public class SubstationDAOImpl extends SinglePrimaryKeyBaseDAOImpl<Substation> implements
        SubstationMapper {

	@Override
	public List<String> selectIdByCompanyId(String companyId) {
		return template.selectList(getNamespace("selectIdByCompanyId"), companyId);
	}

	@Override
	public int deleteByIds(List<String> list) {
		return template.delete(getNamespace("deleteByIds"), list);
	}

	
}
