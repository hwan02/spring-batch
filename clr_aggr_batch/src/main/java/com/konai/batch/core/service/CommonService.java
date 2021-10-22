/*
 * Copyright (c) 2017. KONA I Co.Ltd. All rights reserved.
 * This program is a property of KONA I. you can not redistribute it and/or modify it
 * without any permission of KONA I.
 */

package com.konai.batch.core.service;

import com.konai.batch.common.Utils;
import com.konai.batch.core.enumeration.*;
import com.konai.batch.core.repository.IRepository;
import com.konai.batch.core.vo.TblAggrCodeVo;
import com.konai.batch.core.vo.TblClearingAggrDVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class CommonService {

	private static BigDecimal SUPPLY_RATE = new BigDecimal(1.1);
	private static BigDecimal VAT_RATE = new BigDecimal(0.1);


	@Autowired
	IRepository repository;

	// common에 날짜 계산 넣기
	// vat 포함 여부에 따라서, 과세 여부에 따라서 공급가액 부가세액 구하는거 넣기.


		/*
			1. 면세 / vat 는 그냥 무시
			   공급가액 = 수수료합계 , 부가세 0원


			2. 과세 / vat 포함 / 수수료 절사 정책.. 도..
			   계산된 금액 = 수수료합계
			   공급가액 = round(input * 1.1, 0) -- 몇째자리에서 처리할 지 있어야 하나??? 아오.귀찬항..
			   부가세액 = input - 공급가액

			3. 과세 / vat 별도 / 수수료 절사 정책
			   계산된 금액 = 공급가액
			   부가세액 = 공급가액 * 0.1
			   수수료합계 = input + 부가세액



		*
		*
		*
		* */



	public TblClearingAggrDVo commonCalculateFee(TblClearingAggrDVo vo, TblAggrCodeVo codeVo) {

		vo.setClrSvcNo(codeVo.getClrSvcNo());
		vo.setClrSvcNm(codeVo.getClrSvcNm());
		vo.setMajCd(codeVo.getMajCd());
		vo.setMajNm(codeVo.getMajNm());
		vo.setMidCd(codeVo.getMidCd());
		vo.setMidNm(codeVo.getMidNm());
		vo.setMinCd(codeVo.getMinCd());
		vo.setMinNm(codeVo.getMinNm());

		vo.setMbrCd(codeVo.getMbrCd());
		vo.setVatYn(codeVo.getVatYn());
		vo.setTaxYn(codeVo.getTaxYn());
		vo.setRoundCd(codeVo.getRoundCd());
		vo.setFeeType(codeVo.getFeeType());
		vo.setStlGenCd(codeVo.getStlGenCd());


		if ( StringUtils.isEmpty(vo.getStlDt()) ) {
			vo.setStlDt( getStlDt(vo.getClrDt(), vo.getStlGenCd(), vo.getAspId()) );
		}

		if( codeVo.getFeeType().equalsIgnoreCase(FeeTypeCode.FIXED.getCode()) ) {
			vo.setFeeUnitAmt(codeVo.getFeeUnit());
		} else {
			vo.setFeeRate(codeVo.getFeeUnit());
		}

		if( vo.getFeeAmt().compareTo(BigDecimal.ZERO) == 0) {
		    return commonCalculateFee(vo);
        }

		return vo;
	}



	// 금액 혹은 건수,  수수료율 혹은 금액, 절사정책.. 을 이용해서 공급가액을 구하는거라고 생각해볼까?
	public TblClearingAggrDVo commonCalculateFee(TblClearingAggrDVo vo) {

		// 아 우선 금액을 구하고..
		// 정책에 따라서 나머지를 구해볼까?

		BigDecimal feeAmt;// = BigDecimal.ZERO;
		BigDecimal supAmt;// = BigDecimal.ZERO;
		BigDecimal vatAmt;// = BigDecimal.ZERO;

		BigDecimal calFee;// = BigDecimal.ZERO;

        int roundCode;// = 0;

        if (vo.getRoundCd() == 1) {
            roundCode = BigDecimal.ROUND_UP;
        } else if (vo.getRoundCd() == 0) {
            roundCode = BigDecimal.ROUND_HALF_UP;
        } else {
            roundCode = BigDecimal.ROUND_DOWN;
        }

        // 건수나, 무슨 베이스인지 모르잖아?? 아 rate 기반이면 amt, fix 면
        if (FeeTypeCode.FIXED.getCode().equalsIgnoreCase(vo.getFeeType())) {

            calFee = vo.getTrCnt().multiply(vo.getFeeUnitAmt());

        } else {
            // 절사 정책 가지구 여기서 처리..
            calFee = vo.getTrAmt().multiply(vo.getFeeRate().divide(new BigDecimal(100))).setScale(0, roundCode);
        }

        // 기준 // F/R이냐, // rate 냐, amt냐 // 라운드 정책


        if (TaxCode.FREE.getCode().equalsIgnoreCase(vo.getTaxYn())) {
            // 면세
            supAmt = calFee;
            vatAmt = BigDecimal.ZERO;
            feeAmt = calFee;

        } else {
            // 과세

            if (VatCode.INCLUDE.getCode().equalsIgnoreCase(vo.getVatYn())) {
                // 부가세 포함
                feeAmt = calFee;
                supAmt = calFee.divide(SUPPLY_RATE, 0, BigDecimal.ROUND_HALF_UP);
                vatAmt = calFee.subtract(supAmt);

            } else {
                // 부가세 별도.
                supAmt = calFee;
                vatAmt = calFee.multiply(VAT_RATE).setScale(0, BigDecimal.ROUND_DOWN);
                feeAmt = supAmt.add(vatAmt).setScale(0, roundCode);
            }
        }

		vo.setFeeSupAmt(supAmt);
		vo.setFeeVatAmt(vatAmt);
		vo.setFeeAmt(feeAmt);

		return vo;
	}


	public String getWorkingDay(String date, int offset, String aspId) {

		if( offset <= 0 ) {
			return date;
		}

		Map<String, Object> condition = new HashMap<>();
		condition.put("date", date);
		condition.put("offset", offset);
		condition.put("aspId", aspId);

	    return repository.getWorkingDay(condition);
    }

    public String getEveryDay(String date, int offset, String aspId) {

		if( offset <= 0 ) {
			return date;
		}

		Map<String, Object> condition = new HashMap<>();
		condition.put("date", date);
		condition.put("offset", offset);
		condition.put("aspId", aspId);

		return repository.getEveryDay(condition);
    }

    public String getSpecificDay(String date, int offset, String aspId) {

		if( offset <= 0 ) {
			return date;
		}

		Map<String, Object> condition = new HashMap<>();
		condition.put("date", date);
		condition.put("offset", offset);
		condition.put("aspId", aspId);

		return repository.getSpecificDay(condition);
	}



	public String getStlDt(String clrDt, String stlCode, String aspId) {

		String stlDt = clrDt;

		String basePeriodCode = stlCode.substring(0, 2);
		int baseOffset = Integer.parseInt(stlCode.substring(2, 4));
		String baseDayType = stlCode.substring(4, 6);

		String calDate;
		int calOffset;

		switch( DayPeriodBaseCode.find(basePeriodCode) ) {
			case MS :

				calDate = Utils.addMonthFirstDay(clrDt, 1);
				calOffset = baseOffset - DayOffsetCode.OFFSET_01.getCode();
				stlDt = getSpecificDay(calDate, calOffset, aspId);

			break;

			case MO :

				calDate = Utils.addMonthFirstDay(clrDt, baseOffset);
				calOffset = 0;

				if( DayTypeCode.WORKING_DAY.getCode().equalsIgnoreCase(baseDayType) ) {
					stlDt = getWorkingDay(calDate, calOffset, aspId);
				} else {
					stlDt = getEveryDay(calDate, calOffset - DayOffsetCode.OFFSET_01.getCode(), aspId);
				}

			break;

			case DO :

				if( DayTypeCode.WORKING_DAY.getCode().equalsIgnoreCase(baseDayType) ) {
					stlDt = getWorkingDay(clrDt, baseOffset, aspId);
				} else {
					stlDt = getEveryDay(clrDt, baseOffset, aspId);
				}
			break;

			default:

				if( DayTypeCode.WORKING_DAY.getCode().equalsIgnoreCase(baseDayType) ) {
					stlDt = getWorkingDay(clrDt, baseOffset, aspId);
				} else {
					stlDt = getEveryDay(clrDt, baseOffset - DayOffsetCode.OFFSET_01.getCode(), aspId);
				}

				break;

		}

		return StringUtils.isEmpty(stlDt)? clrDt : stlDt ;
	}



}
