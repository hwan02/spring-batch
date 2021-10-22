package com.konai.batch.config;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeciderConfig {
	@Bean
	public JobExecutionDecider jobExecutionDecider() {

		return new JobExecutionDecider() {

			@Override
			public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {

				String decider = jobExecution.getJobParameters().getString("decider");

				if( decider == null ) {
					return new FlowExecutionStatus("ALL");
				}

				return new FlowExecutionStatus(decider.toUpperCase());
			}

		};
	}
}
