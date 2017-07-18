package com.gdut.dongjun.dao.impl;

import org.springframework.stereotype.Repository;

import com.gdut.dongjun.dao.UserMapper;
import com.gdut.dongjun.dao.base.impl.SinglePrimaryKeyBaseDAOImpl;
import com.gdut.dongjun.po.User;

@Repository
public class UserDAOImpl extends SinglePrimaryKeyBaseDAOImpl<User> implements
UserMapper {

}
