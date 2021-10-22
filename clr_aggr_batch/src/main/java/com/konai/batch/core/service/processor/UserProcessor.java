package com.konai.batch.core.service.processor;

import com.konai.batch.core.enumeration.*;
import com.konai.batch.core.repository.IRepository;
import com.konai.batch.core.service.CommonService;
import com.konai.batch.core.vo.TblAggrCodeVo;
import com.konai.batch.core.vo.TblClearingAggrDVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class UserProcessor implements ItemProcessor<TblClearingAggrDVo, TblClearingAggrDVo> {

	@Autowired
	private CommonService service;

	@Autowired
	private IRepository repository;

	@Override
	public TblClearingAggrDVo process(TblClearingAggrDVo item) throws Exception {

		TblClearingAggrDVo vo = new TblClearingAggrDVo();
		BeanUtils.copyProperties(item, vo);

		// TODO: 웰컴 회원 가입 수수료
		/**
		 	- 회원 1인당 20원 :: feeType = F / feeUnitAmt = 20
		 	- VAT 포함 :: vatYn = Y
		 	- 과세 :: taxYn = Y
		 	- 월 단위 :: stlGenCd = M
		 	- 익월 25일 입금 :: stlGenCd = 25
		*/

		Map<String, String> condition = new HashMap<>();
		condition.put("clrSvcNo", ClrServiceCode.USER_REGISTRATION.getCode());
		condition.put("aspId", item.getAspId());

		TblAggrCodeVo userFeeVo = repository.selectAggrCode(condition);
		if( userFeeVo == null ) {
			return null;
		}
		/*vo.setVatYn(VatCode.INCLUDE.getCode());
		vo.setTaxYn(TaxCode.LEVY.getCode());
		vo.setFeeType(FeeTypeCode.FIXED.getCode());
		vo.setFeeUnitAmt(new BigDecimal(20));
		vo.setRoundCd(0);
		*/

		vo = service.commonCalculateFee(vo, userFeeVo);

		/*
		vo.setMbrCd(MemberCode.WELCOME.getCode());
		vo.setClrSvcNo(ClrServiceCode.USER_REGISTRATION.getCode());
		vo.setClrSvcNm(ClrServiceCode.USER_REGISTRATION.getMsg());
		vo.setMajCd(MajorCode.ACT.getCode());
		vo.setMajNm(MajorCode.ACT.getMsg());
		vo.setMidCd(MiddleCode.USER.getCode());
		vo.setMidNm(MiddleCode.USER.getMsg());
		vo.setMinCd(MinorCode.USER_REGISTRATION.getCode());
		vo.setMinNm(MinorCode.USER_REGISTRATION.getMsg());

		*/

		return vo;
	}







}
