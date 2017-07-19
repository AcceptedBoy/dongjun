package com.gdut.dongjun.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.gdut.dongjun.dao.LineMapper;
import com.gdut.dongjun.dao.base.impl.SinglePrimaryKeyBaseDAOImpl;
import com.gdut.dongjun.po.Line;

/**
 * 
 * @author	Sherlock-lee
 * @date	2015年11月14日 下午4:56:57
 * @see 	TODO
 * @since   1.0
 */
@Repository
public class LineDAOImpl extends SinglePrimaryKeyBaseDAOImpl<Line> implements
		LineMapper {

	@Override
	public List<Line> selectAll() {
		// TODO Auto-generated method stub
		return template.selectList("selectAll");
	}

	@Override
	public Line getLineBySwitchId(String switchId) {
		// TODO Auto-generated method stub
		return template.selectOne("getLineBySwitchId", switchId);
	}

	@Override
	public List<Line> selectByKeyWord(String keyWord) {
		// TODO Auto-generated method stub
		return template.selectList("selectByKeyWord", keyWord+"%");
	}

	@Override
	public List<String> selectId() {
		return template.selectList("selectId");
	}

	@Override
	public List<String> selectIdBySubstationIds(List<String> list) {
		return template.selectList("selectIdBySubstationIds", list);
	}

	@Override
	public int deleteByIds(List<String> list) {
		return template.delete(getNamespace("deleteByIds"), list);
	}
	
}
