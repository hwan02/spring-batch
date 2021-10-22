/*
 * Copyright (c) 2017. KONA I Co.Ltd. All rights reserved.
 * This program is a property of KONA I. you can not redistribute it and/or modify it
 * without any permission of KONA I.
 */

package com.konai.batch.core.enumeration;

public enum MemberCode {

    WELCOME("001", "웰컴저축은행")
    , CU("002", "BGF 리테일")
    , PAYCO("003", "엔에이치엔페이코(주)")

    ;

    private String code;
    private String msg;

    private MemberCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
		return msg;
	}

    public static MemberCode find(String name) {
        for (MemberCode type : MemberCode.values()) {
            if (type.name().equalsIgnoreCase(name)) {
                return type;
            }
        }
        return null;
    }

}
