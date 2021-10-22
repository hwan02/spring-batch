/*
 * Copyright (c) 2017. KONA I Co.Ltd. All rights reserved.
 * This program is a property of KONA I. you can not redistribute it and/or modify it
 * without any permission of KONA I.
 */

package com.konai.batch.core.enumeration;

public enum DayOffsetCode {

    OFFSET_01(1, "01")
    , OFFSET_02(2, "02")
    , OFFSET_03(3, "03")
    , OFFSET_04(4, "04")
    , OFFSET_05(5, "05")
    , OFFSET_06(6, "06")
    , OFFSET_07(7, "07")
    , OFFSET_08(8, "08")
    , OFFSET_09(9, "09")
    , OFFSET_10(10, "10")
    , OFFSET_11(11, "11")
    , OFFSET_12(12, "12")
    , OFFSET_13(13, "13")
    , OFFSET_14(14, "14")
    , OFFSET_15(15, "15")
    , OFFSET_21(21, "21")

    ;

    private int code;
    private String msg;

    private DayOffsetCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
		return msg;
	}

    public static DayOffsetCode find(String name) {
        for (DayOffsetCode type : DayOffsetCode.values()) {
            if (type.name().equalsIgnoreCase(name)) {
                return type;
            }
        }
        return null;
    }

}
