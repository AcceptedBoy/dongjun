package com.gdut.dongjun.domain.dao.impl;

import org.springframework.stereotype.Repository;

import com.gdut.dongjun.domain.dao.TemperatureModuleMapper;
import com.gdut.dongjun.domain.dao.base.impl.DelTagHolderDAOImpl;
import com.gdut.dongjun.domain.po.TemperatureModule;

@Repository
public class TemperatureModuleDAOImpl extends
DelTagHolderDAOImpl<TemperatureModule>
implements TemperatureModuleMapper {

}
