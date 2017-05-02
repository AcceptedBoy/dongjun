package com.gdut.dongjun.domain.dao;

import java.util.List;

import com.gdut.dongjun.domain.dao.base.SinglePrimaryKeyBaseMapper;
import com.gdut.dongjun.domain.po.PlatformGroup;

/**
 * Created by symon on 16-10-18.
 */
public interface PlatformGroupMapper extends SinglePrimaryKeyBaseMapper<PlatformGroup> {

	List<PlatformGroup> fuzzySearch(String string);
}
