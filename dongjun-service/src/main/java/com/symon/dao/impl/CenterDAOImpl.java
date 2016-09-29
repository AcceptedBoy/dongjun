package com.symon.dao.impl;

import com.symon.dao.CenterMapper;
import com.symon.dao.base.impl.SinglePrimaryKeyBaseDAOImpl;
import com.symon.po.Center;
import org.springframework.stereotype.Repository;

/**
 * Created by symon on 16-9-28.
 */
@Repository
public class CenterDAOImpl extends SinglePrimaryKeyBaseDAOImpl<Center>
    implements CenterMapper {
}
