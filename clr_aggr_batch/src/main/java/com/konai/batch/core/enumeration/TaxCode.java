/*
 * Copyright (c) 2017. KONA I Co.Ltd. All rights reserved.
 * This program is a property of KONA I. you can not redistribute it and/or modify it
 * without any permission of KONA I.
 */

package com.konai.batch.core.enumeration;

public enum TaxCode {

    LEVY("Y", "과세")
	, FREE("N", "면세")

    ;

    private String code;
    private String msg;

    private TaxCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
		return msg;
	}

    public static TaxCode find(String name) {
        for (TaxCode type : TaxCode.values()) {
            if (type.name().equalsIgnoreCase(name)) {
                return type;
            }
        }
        return null;
    }

}
