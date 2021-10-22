/*
 * Copyright (c) 2017. KONA I Co.Ltd. All rights reserved.
 * This program is a property of KONA I. you can not redistribute it and/or modify it
 * without any permission of KONA I.
 */

package com.konai.batch.core.enumeration;

public enum ClrServiceCode {

    PRINCIPAL("100110001", "가맹점 이용 대금")
    , MCT_FEE("100110002", "가맹점 수수료")
    , MC_DISC_AMT("100110003", "가맹점 할인 부담금")
    , PG_DIFF("100110004", "PG 차액 수수료")
    , MCT_USE("100110005", "가맹점 이용 수수료")
    , KONA_DISC_AMT("100110006", "자사부담 할인 부담금")
    , THIRD_DISC_AMT("100110007", "제3자 할인 부담금")

    , VAN_FEE("100120021", "결제 중계 수수료")
    , PG_FEE("100120022", "충전 수수료")
    , BRK_FEE("100120023", "충전 중계 수수료")
    , RS_FEE("100120024", "RS 수수료")

    , EXPRESS_USE("200210201", "고속버스 결제 이용 수수료")
    , INTERCITY_USE("200210202", "시외버스 결제 이용 수수료")
    , TAXI_EBCARD("200210203", "택시대행업체 이용 수수료(이비)")
    , TAXI_TMONEY("200210204", "택시대행업체 이용 수수료(티머니)")
    , TRAFFIC_ETC("200210205", "교통수수료 기타")
    , PUBLIC_PARKING_TMONEY("200210206", "공영주차장 이용 수수료(티머니)")

    , NATIONAL_DUTY_TAX_TRAN("200220211", "국관세 납부 수수료")
    , INSURANCE_TRAN("200220212", "4대보험 납부 수수료")
    , NATIONAL_DUTY_TAX_USE("200220213", "국관세 이용 수수료")

    , ISP_AUTH("200230221", "ISP 수수료")
    , TOKEN_AUTH("200230222", "토큰 인증 수수료")
    , SMS_AUTH("200230223", "SMS 인증 수수료")
    , ARS_AUTH("200230224", "ARS 인증 수수료")
    , NAME_AUTH("200230225", "성명 인증 수수료")
    , REAL_NAME_AHTU("200230226", "실명 인증 수수료")
    , NAMED("200230227", "기명화 인증 수수료")

    , POST_DELIVERY("200240241", "우편 배송 수수료")
    , PERSONAL_POST_DELIVERY("200240242", "인편 배송 수수료")

    , USER_REGISTRATION("200250251", "회원 가입 수수료")
    , USER_CS("200250252", "민원 응대 및 처리 수수료")

    , SMS_TRAN_NOTIFY("200260261", "거래 발생 알림 메세지 SMS 발송 수수료")
    , LMS_TRAN_NOTIFY("200260262", "거래 발생 알림 메세지 LMS 발송 수수료")
    , MMS_TRAN_NOTIFY("200260263", "거래 발생 알림 메세지 MMS 발송 수수료")
    , SMS_INFORM_NOTIFY("200265261", "일반 안내 메세지 SMS 발송 수수료")
    , LMS_INFORM_NOTIFY("200265262", "일반 안내 메세지 LMS 발송 수수료")
    , MMS_INFORM_NOTIFY("200265263", "일반 안내 메세지 MMS 발송 수수료")


    , POST_USER_INFORM("200270271", "고객 안내용 정보 우편 발송 수수료")
    , EMAIL_USER_INFORM("200270272", "고객 안내용 정보 E-MAIL 발송 수수료")
    , POST_BILLING("200275271", "서비스 이용 명세서 우편 발송 수수료")
    , EMAIL_BILLING("200275272", "서비스 이용 명세서 E-MAIL 발송 수수료")

    , CARD_ISSUE("200900901", "카드 발급 수수료")

    ;

    private String code;
    private String msg;

    private ClrServiceCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
		return msg;
	}

    public static ClrServiceCode find(String name) {
        for (ClrServiceCode type : ClrServiceCode.values()) {
            if (type.name().equalsIgnoreCase(name)) {
                return type;
            }
        }
        return null;
    }

}
