package com.konai.batch.config.step;

import com.konai.batch.config.listener.EventNotificationListener;
import com.konai.batch.core.enumeration.ClrServiceCode;
import com.konai.batch.core.repository.IRepository;
import com.konai.batch.core.service.impl.CustomOneItemReader;
import com.konai.batch.core.service.impl.CustomOneItemWriter;
import com.konai.batch.core.service.impl.InitTasklet;
import com.konai.batch.core.service.processor.CommonProcessor;
import com.konai.batch.core.vo.TblAggrCodeVo;
import com.konai.batch.core.vo.TblClearingAggrDVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.mybatis.spring.batch.MyBatisPagingItemReader;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.repeat.RepeatStatus;
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
public class CardBatchConfig {

	@Autowired
	IRepository						repository;

    @Autowired
    private SqlSessionFactory		sqlSessionFactory;

    @Autowired
    private StepBuilderFactory		stepBuilderFactory;

	@Autowired
	private SqlSessionTemplate		sqlSessionTemplate;

	@Value("${chunk.commit.unit:50}")
	private int	CHUNK_COUNT;

    @Bean
    @StepScope
    public ItemReader<TblClearingAggrDVo> readerCamsIssue(@Value("#{jobParameters}") Map<String, Object> key) {
        MyBatisPagingItemReader<TblClearingAggrDVo> reader = new MyBatisPagingItemReader<>();
        Map<String, Object> parameterValues = new HashMap<>();
        parameterValues.putAll(key);

        reader.setSqlSessionFactory(sqlSessionFactory);
        reader.setParameterValues(parameterValues);
        reader.setQueryId("card.selectCamsIssueList");
        reader.setPageSize(CHUNK_COUNT);

        try {
            reader.afterPropertiesSet();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new CustomOneItemReader<>(reader);
    }

    @Bean
    public Step stepCamsIssue(EventNotificationListener listener
            , @Qualifier("readerCamsIssue") ItemReader<TblClearingAggrDVo> reader
            , @Qualifier("writerAggrD") ItemWriter<TblClearingAggrDVo> writer
            ) {

    	log.warn("카드 발급 수수료[{}]", String.format("%s,%s", ClrServiceCode.CARD_ISSUE.getCode(), ClrServiceCode.NAMED.getCode()));

    	return stepBuilderFactory.get("stepCamsIssue").listener(listener)
                .<TblClearingAggrDVo, TblClearingAggrDVo>chunk(CHUNK_COUNT)
                .reader(reader)
                .processor(camsIssueProcessor())
                .writer(writer)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public Step stepInitCamsIssue() {
        InitTasklet initTasklet = new InitTasklet(String.format("%s,%s", ClrServiceCode.CARD_ISSUE.getCode(), ClrServiceCode.NAMED.getCode()), repository);
        return stepBuilderFactory.get("stepInitCamsIssue")
                .tasklet(initTasklet)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public CommonProcessor camsIssueProcessor() {

        List<TblAggrCodeVo> codeList = new ArrayList<>();
        codeList.addAll(repository.selectAggrCodeList(ClrServiceCode.CARD_ISSUE.getCode()));
        codeList.addAll(repository.selectAggrCodeList(ClrServiceCode.NAMED.getCode()));

        CommonProcessor processor = new CommonProcessor();
        processor.setCodeList(codeList);

        return processor;
    }

}
