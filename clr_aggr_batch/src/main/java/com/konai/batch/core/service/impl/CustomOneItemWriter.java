/*
 * Copyright (c) 2017. KONA I Co.Ltd. All rights reserved.
 * This program is a property of KONA I. you can not redistribute it and/or modify it
 * without any permission of KONA I.
 */

package com.konai.batch.core.service.impl;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.springframework.batch.item.ItemWriter;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class CustomOneItemWriter<T> implements ItemWriter<T> {

    MyBatisBatchItemWriter<T> writer;

    public CustomOneItemWriter(SqlSessionTemplate sqlSessionTemplate
            , SqlSessionFactory sqlSessionFactory
            , String queryId) {

        writer = new MyBatisBatchItemWriter<T>();

        writer.setStatementId(queryId);
        writer.setSqlSessionFactory(sqlSessionFactory);
        writer.setSqlSessionTemplate(sqlSessionTemplate);

    }

    public CustomOneItemWriter(MyBatisBatchItemWriter<T> writer) {
		this.writer = writer;

	}

    @Override
    public void write(List<? extends T> items) throws Exception {
        /*
		  processor에서 작업한 결과를 한 번에 write함.

		 */
        writer.write(items);

    }

}
