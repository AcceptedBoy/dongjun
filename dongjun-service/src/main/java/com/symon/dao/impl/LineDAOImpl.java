package com.symon.dao.impl;

import java.util.List;

import com.symon.dao.LineMapper;
import com.symon.dao.base.impl.SinglePrimaryKeyBaseDAOImpl;
import com.symon.po.Line;
import org.springframework.stereotype.Repository;


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
}
