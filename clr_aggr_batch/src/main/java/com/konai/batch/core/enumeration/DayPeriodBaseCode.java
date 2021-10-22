/*
 * Copyright (c) 2017. KONA I Co.Ltd. All rights reserved.
 * This program is a property of KONA I. you can not redistribute it and/or modify it
 * without any permission of KONA I.
 */

package com.konai.batch.core.enumeration;

public enum DayPeriodBaseCode {

    QY("QY", "분기별")
    , HY("HY", "반기별")
    , MS("MS", "익월 특정일")
    , MO("MO", "N월 후의 1일")
    , ME("ME", "해당 달의 마지막날")
    , M2("M2", "달에 두 번")
    , M4("M4", "달에 네 번")
    , DO("DO", "오늘로부터 N일 후")
    , DS("DS", "이번달의 특정 일자")
    , IM("IM", "즉시")
    ;

    private String code;
    private String msg;

    private DayPeriodBaseCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
		return msg;
	}

    public static DayPeriodBaseCode find(String name) {
        for (DayPeriodBaseCode type : DayPeriodBaseCode.values()) {
            if (type.name().equalsIgnoreCase(name)) {
                return type;
            }
        }
        return null;
    }

}
