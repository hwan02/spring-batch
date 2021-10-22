package com.konai.batch.config;

import com.konai.batch.config.listener.EventNotificationListener;
import com.konai.batch.config.listener.ParametersValidator;
import com.konai.batch.core.enumeration.DeciderCode;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class JobConfig {

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	private JobExecutionDecider decider;

	@Bean
	public Job job(
			@Qualifier("stepInit") Step stepInit
			, @Qualifier("stepInitUserRegistration") Step stepInitUserRegistration
			, @Qualifier("stepUserRegistration") Step stepUserRegistration
			, @Qualifier("stepInitDiscount") Step stepInitDiscount
			, @Qualifier("stepDiscount") Step stepDiscount

			, @Qualifier("stepInitRsFee") Step stepInitRsFee
			, @Qualifier("stepRsFee") Step stepRsFee

			, @Qualifier("stepInitCamsIssue") Step stepInitCamsIssue
			, @Qualifier("stepCamsIssue") Step stepCamsIssue

			, ParametersValidator jobParametersValidator
			, EventNotificationListener eventListener) {

		Flow userFlow = new FlowBuilder<SimpleFlow>("userFlow")
				.start(stepInitUserRegistration)
					.on("STOPPED")
					.end()
				.from(stepInitUserRegistration)
					.on("COMPLETED")
					.to(stepUserRegistration)
				.build();

		Flow discFlow = new FlowBuilder<SimpleFlow>("discFlow")
				.start(stepInitDiscount)
					.on("STOPPED")
					.end()
				.from(stepInitDiscount)
					.on("COMPLETED")
					.to(stepDiscount)
				.build();

		Flow rsFeeFlow = new FlowBuilder<SimpleFlow>("rsFeeFlow")
				.start(stepInitRsFee)
					.on("STOPPED")
					.end()
				.from(stepInitRsFee)
					.on("COMPLETED")
					.to(stepRsFee)
				.build();

		Flow camsIssueFlow = new FlowBuilder<SimpleFlow>("camsIssueFlow")
				.start(stepInitCamsIssue)
					.on("STOPPED")
					.end()
				.from(stepInitCamsIssue)
					.on("COMPLETED")
					.to(stepCamsIssue)
				.build();

		return jobBuilderFactory.get("clr_aggr_batch")
				.incrementer(new RunIdIncrementer())
				.listener(eventListener)
				.validator(jobParametersValidator)
				.start(stepInit)
				.next(decider)
					.on(DeciderCode.ALL.getCode())
					.to(userFlow).next(discFlow)
					.next(rsFeeFlow)
					.next(camsIssueFlow)
				.end()
				.listener(eventListener)
				.build();

	}

}