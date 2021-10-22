/*
 * Copyright (c) 2017. KONA I Co.Ltd. All rights reserved.
 * This program is a property of KONA I. you can not redistribute it and/or modify it
 * without any permission of KONA I.
 */

package com.konai.batch.core.enumeration;


/**
 *
 * <pre>
 * com.konai.batch.core.enumeration
 * DeciderCode.java
 * </pre>
 *
 * @Author	: eslee
 * @Date	: 2018. 8. 31.
 * @Description 	:
 */
public enum DeciderCode {

    ALL("ALL", "ALL")

    ;


    private String code;
    private String targetTable;

   	public String getCode() {
        return code;
    }

    private DeciderCode(String code, String targetTable) {
        this.code = code;
        this.targetTable = targetTable;
    }

    public String getTargetTable() {
		return targetTable;
	}

	public void setTargetTable(String targetTable) {
		this.targetTable = targetTable;
	}

    public static DeciderCode find(String name) {
        for (DeciderCode type : DeciderCode.values()) {
            if (type.name().equalsIgnoreCase(name)) {
                return type;
            }
        }
        return null;
    }

}
