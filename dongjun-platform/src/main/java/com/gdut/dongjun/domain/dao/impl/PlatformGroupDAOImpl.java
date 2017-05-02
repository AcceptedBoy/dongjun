package com.gdut.dongjun.domain.dao.impl;

import com.gdut.dongjun.domain.dao.PlatformGroupMapper;
import com.gdut.dongjun.domain.dao.base.SinglePrimaryKeyBaseMapper;
import com.gdut.dongjun.domain.dao.base.impl.SinglePrimaryKeyBaseDAOImpl;
import com.gdut.dongjun.domain.po.PlatformGroup;

import java.util.List;

import org.springframework.stereotype.Repository;

/**
 * Created by symon on 16-10-18.
 */
@Repository
public class PlatformGroupDAOImpl extends SinglePrimaryKeyBaseDAOImpl<PlatformGroup>
    implements PlatformGroupMapper {

	@Override
	public List<PlatformGroup> fuzzySearch(String name) {
		return template.selectList(getNamespace("fuzzySearch"), name);
	}
}
