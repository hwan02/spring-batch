package com.konai.batch.config.step;

import com.konai.batch.config.listener.EventNotificationListener;
import com.konai.batch.core.enumeration.ClrServiceCode;
import com.konai.batch.core.repository.IRepository;
import com.konai.batch.core.service.impl.CustomOneItemReader;
import com.konai.batch.core.service.impl.InitTasklet;
import com.konai.batch.core.service.processor.CommonProcessor;
import com.konai.batch.core.vo.TblAggrCodeVo;
import com.konai.batch.core.vo.TblClearingAggrDVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisPagingItemReader;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EnableBatchProcessing
@Configuration
@Slf4j
public class FeeBatchConfig {

	@Autowired
	IRepository						repository;

    @Autowired
    private SqlSessionFactory		sqlSessionFactory;

    @Autowired
    private StepBuilderFactory		stepBuilderFactory;


	@Value("${chunk.commit.unit:50}")
	private int	CHUNK_COUNT;

    @Bean
    @StepScope
    public ItemReader<TblClearingAggrDVo> readerRsFee(@Value("#{jobParameters}") Map<String, Object> key) {
        MyBatisPagingItemReader<TblClearingAggrDVo> reader = new MyBatisPagingItemReader<>();
        Map<String, Object> parameterValues = new HashMap<>();
        parameterValues.putAll(key);

        reader.setSqlSessionFactory(sqlSessionFactory);
        reader.setParameterValues(parameterValues);
        reader.setQueryId("fee.selectRsFee");
        reader.setPageSize(CHUNK_COUNT);

        try {
            reader.afterPropertiesSet();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new CustomOneItemReader<>(reader);
    }

    @Bean
    public Step stepRsFee(EventNotificationListener listener
            , @Qualifier("readerRsFee") ItemReader<TblClearingAggrDVo> reader
            , @Qualifier("writerAggrD") ItemWriter<TblClearingAggrDVo> writer
            ) {

    	log.warn("RS 수수료[{}]", ClrServiceCode.RS_FEE.getCode());

    	return stepBuilderFactory.get("stepRsFee").listener(listener)
                .<TblClearingAggrDVo, TblClearingAggrDVo>chunk(CHUNK_COUNT)
                .reader(reader)
                .processor(rsFeeProcessor())
                .writer(writer)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public Step stepInitRsFee() {
        InitTasklet initTasklet = new InitTasklet(
                String.format("%s", ClrServiceCode.RS_FEE.getCode()), repository);
        return stepBuilderFactory.get("stepInitRsFee")
                .tasklet(initTasklet)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public CommonProcessor rsFeeProcessor() {

        List<TblAggrCodeVo> codeList = repository.selectAggrCodeList(ClrServiceCode.RS_FEE.getCode());

        CommonProcessor processor = new CommonProcessor();
        processor.setCodeList(codeList);

        return processor;
    }

}
