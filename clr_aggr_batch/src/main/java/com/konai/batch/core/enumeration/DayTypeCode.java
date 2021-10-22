/*
 * Copyright (c) 2017. KONA I Co.Ltd. All rights reserved.
 * This program is a property of KONA I. you can not redistribute it and/or modify it
 * without any permission of KONA I.
 */

package com.konai.batch.core.enumeration;

public enum DayTypeCode {

	WORKING_DAY("WD", "영업일")
	, EVERY_DAY("ED", "모든일")

    ;

    private String code;
    private String msg;

    private DayTypeCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
		return msg;
	}

    public static DayTypeCode find(String name) {
        for (DayTypeCode type : DayTypeCode.values()) {
            if (type.name().equalsIgnoreCase(name)) {
                return type;
            }
        }
        return null;
    }

}
