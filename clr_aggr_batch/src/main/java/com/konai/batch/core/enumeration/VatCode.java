/*
 * Copyright (c) 2017. KONA I Co.Ltd. All rights reserved.
 * This program is a property of KONA I. you can not redistribute it and/or modify it
 * without any permission of KONA I.
 */

package com.konai.batch.core.enumeration;

public enum VatCode {

	INCLUDE("Y", "부가세 포함")
	, EXCLUDE("N", "부가세 별도")

    ;

    private String code;
    private String msg;

    private VatCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
		return msg;
	}

    public static VatCode find(String name) {
        for (VatCode type : VatCode.values()) {
            if (type.name().equalsIgnoreCase(name)) {
                return type;
            }
        }
        return null;
    }

}
