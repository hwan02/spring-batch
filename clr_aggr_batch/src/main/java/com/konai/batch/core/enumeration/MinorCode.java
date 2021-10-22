/*
 * Copyright (c) 2017. KONA I Co.Ltd. All rights reserved.
 * This program is a property of KONA I. you can not redistribute it and/or modify it
 * without any permission of KONA I.
 */

package com.konai.batch.core.enumeration;

public enum MinorCode {


    PRINCIPAL("001", "가맹점 이용 대금")
    , MCT_FEE("002", "가맹점 수수료")
    , MC_DISC_AMT("003", "가맹점 할인 부담금")
    , PG_DIFF("004", "PG 차액 수수료")
    , MCT_USE("005", "가맹점 이용 수수료")
    , KONA_DISC_AMT("006", "자사 부담 할인 부담금")
    , THIRD_DISC_AMT("007", "제3자 할인 부담금")

    , VAN_FEE("021", "결제 중계 수수료")
    , PG_FEE("022", "충전 수수료")
    , BRK_FEE("023", "충전 중계 수수료")
    , RS_FEE("024", "RS 수수료")

    , EXPRESS_USE("201", "고속버스 결제 이용 수수료")
    , INTERCITY_USE("202", "시외버스 결제 이용 수수료")
    , TAXI_EBCARD("203", "택시대행업체 이용 수수료(이비)")
    , TAXI_TMONEY("204", "택시대행업체 이용 수수료(티머니)")
    , TRAFFIC_ETC("205", "교통수수료 기타")
    , PUBLIC_PARKING_TMONEY("206", "공영주차장 이용 수수료(티머니)")

    , NATIONAL_DUTY_TAX_TRAN("211", "국관세 납부 수수료")
    , INSURANCE_TRAN("212", "4대보험 납부 수수료")
    , NATIONAL_DUTY_TAX_USE("213", "국관세 이용 수수료")

    , ISP_AUTH("221", "ISP 수수료")
    , TOKEN_AUTH("222", "토큰 인증")
    , SMS_AUTH("223", "SMS 인증")
    , ARS_AUTH("224", "ARS 인증")
    , NAME_AUTH("225", "성명 인증")
    , REAL_NAME_AUTH("226", "실명 인증")
    , NAMED("227", "기명화 인증")

    , POST_DELIVERY("241", "우편 배송")
    , PERSONAL_POST_DELIVERY("242", "인편 배송")

    , USER_REGISTRATION("251", "회원 가입 수수료")
    , USER_CS("252", "민원 응대 및 처리 수수료")

    , SMS("261", "SMS")
    , LMS("262", "LMS")
    , MMS("263", "MMS")

    , POST("271", "우편")
    , EMAIL("272", "E-MAIL")

    , CARD_ISSUE("901", "카드 발급 수수료")

    ;

    private String code;
    private String msg;

    private MinorCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
		return msg;
	}

    public static MinorCode find(String name) {
        for (MinorCode type : MinorCode.values()) {
            if (type.name().equalsIgnoreCase(name)) {
                return type;
            }
        }
        return null;
    }

}
