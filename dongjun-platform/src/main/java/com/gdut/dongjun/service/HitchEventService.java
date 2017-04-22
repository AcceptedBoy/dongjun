package com.gdut.dongjun.service;

import com.gdut.dongjun.domain.vo.HitchEventVO;
import com.gdut.dongjun.dto.HitchEventDTO;

public interface HitchEventService {

	public HitchEventDTO wrapIntoDTO(HitchEventVO vo);
}
