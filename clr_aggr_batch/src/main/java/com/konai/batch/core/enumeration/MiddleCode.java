/*
 * Copyright (c) 2017. KONA I Co.Ltd. All rights reserved.
 * This program is a property of KONA I. you can not redistribute it and/or modify it
 * without any permission of KONA I.
 */

package com.konai.batch.core.enumeration;

public enum MiddleCode {

    MERCHANT("110", "가맹점")
    , AFFILIATE("120", "제휴처")
    , TRAFFIC("210", "교통")
    , TAX("220", "국/관세")
    , AUTH("230", "인증")
    , DELIVERY("240", "배송")
    , USER("250", "회원")
    , TRAN_MSG("260", "거래 승인 메시지")
    , INFO_MSG("265", "일반 안내 메시지")
    , USER_INFORM("270", "고객 안내장")
    , BILL_INFORM("275", "이용 명세서")
    , ISSUE("900", "발급")

    ;

    private String code;
    private String msg;

    private MiddleCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
		return msg;
	}

    public static MiddleCode find(String name) {
        for (MiddleCode type : MiddleCode.values()) {
            if (type.name().equalsIgnoreCase(name)) {
                return type;
            }
        }
        return null;
    }

}
