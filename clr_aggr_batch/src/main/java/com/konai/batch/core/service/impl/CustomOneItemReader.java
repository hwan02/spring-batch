/*
 * Copyright (c) 2017. KONA I Co.Ltd. All rights reserved.
 * This program is a property of KONA I. you can not redistribute it and/or modify it
 * without any permission of KONA I.
 */

package com.konai.batch.core.service.impl;

import org.mybatis.spring.batch.MyBatisPagingItemReader;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Component
@Scope("step")
public class CustomOneItemReader<T> implements ItemReader<T>{

	MyBatisPagingItemReader<T> reader;

	public CustomOneItemReader(MyBatisPagingItemReader<T> reader) {
		this.reader = reader;
	}

	@Override
	public T read() throws Exception, UnexpectedInputException, ParseException,
			NonTransientResourceException {
		/* chunk 수만큼 read함. chunk가 10일 경우 10번씩 read하여 processor로 넘김.
		 read()한 결과가 null이라면 null을 리턴해야함.
		 바로 return reader.read()를 하게 되면 무한루프를 돌게 됨.
		 */

		T read = reader.read();


		return read;
	}


}
