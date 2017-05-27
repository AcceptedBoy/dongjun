package com.gdut.dongjun.service;

import com.gdut.dongjun.domain.dto.HitchEventDTO;
import com.gdut.dongjun.service.webservice.client.po.InfoEventDTO;
import com.gdut.dongjun.web.vo.HitchEventVO;
import com.gdut.dongjun.web.vo.InfoEventVO;

public interface RemoteEventService {

	public HitchEventVO wrapHitchVO(HitchEventDTO dto);

	public InfoEventVO wrapIntoVO(InfoEventDTO dto);
}
