package com.gdut.dongjun.dao.impl;

import org.springframework.stereotype.Repository;

import com.gdut.dongjun.dao.CompanyMapper;
import com.gdut.dongjun.dao.base.impl.SinglePrimaryKeyBaseDAOImpl;
import com.gdut.dongjun.po.Company;

@Repository
public class CompanyDAOImpl extends SinglePrimaryKeyBaseDAOImpl<Company>
implements CompanyMapper {

}
