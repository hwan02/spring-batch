/*
 * Copyright (c) 2017. KONA I Co.Ltd. All rights reserved.
 * This program is a property of KONA I. you can not redistribute it and/or modify it
 * without any permission of KONA I.
 */

package com.konai.batch.core.repository.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.konai.batch.core.vo.TblAggrCodeVo;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.konai.batch.core.repository.IRepository;

@Repository
public class RepositoryImpl implements IRepository {

	@Autowired
	SqlSessionTemplate sqlSessionTemplate;

	@Override
	public int deleteList(HashMap<String, Object> condition) {
		return sqlSessionTemplate.delete("common.deleteTable", condition);
	}

	@Override
	public TblAggrCodeVo selectAggrCode(Map<String, String> condition) {
		return sqlSessionTemplate.selectOne("common.selectAggrCode", condition);
	}

	@Override
	public List<TblAggrCodeVo> selectAggrCodeList(String condition) {
		return sqlSessionTemplate.selectList("common.selectAggrCodeList", condition);
	}

	@Override
	public String getWorkingDay(Map<String, Object> condition) {
		return sqlSessionTemplate.selectOne("common.getWorkingDay", condition);
	}

	@Override
	public String getEveryDay(Map<String, Object> condition) {
		return sqlSessionTemplate.selectOne("common.getEveryDay", condition);
	}

	@Override
	public String getSpecificDay(Map<String, Object> condition) {
		return sqlSessionTemplate.selectOne("common.getSpecificDay", condition);
	}

}
