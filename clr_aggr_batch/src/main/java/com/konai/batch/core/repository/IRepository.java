/*
 * Copyright (c) 2017. KONA I Co.Ltd. All rights reserved.
 * This program is a property of KONA I. you can not redistribute it and/or modify it
 * without any permission of KONA I.
 */

package com.konai.batch.core.repository;

import com.konai.batch.core.vo.TblAggrCodeVo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IRepository{

	int deleteList(HashMap<String, Object> condition);
	TblAggrCodeVo selectAggrCode(Map<String, String> condition);
	List<TblAggrCodeVo> selectAggrCodeList(String condition);

	String getWorkingDay(Map<String, Object> condition);
	String getEveryDay(Map<String, Object> condition);
	String getSpecificDay(Map<String, Object> condition);

}
