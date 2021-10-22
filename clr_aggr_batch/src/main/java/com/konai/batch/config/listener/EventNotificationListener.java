package com.konai.batch.config.listener;

import com.konai.batch.common.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.annotation.*;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.stereotype.Component;

/**
 *
 * <pre>
 * com.konai.batch.config.listener
 * EventNotificationListener.java
 * </pre>
 *
 * @Author	: eslee
 * @Date	: 2018. 10. 30.
 * @Description 	:
 */
@Component
public class EventNotificationListener implements JobExecutionListener, StepListener{

	private static final Logger log = LoggerFactory.getLogger(EventNotificationListener.class);

	String uuid = "";
	long startTime ;
	long endTime ;

	@BeforeJob
	public void beforeJob(JobExecution jobExecution){
		uuid = Utils.getUID();
		startTime = System.currentTimeMillis();
		log.info("UUID[{}] Start JOB[{}] Status[{}] JobParameters[{}]"
					, uuid, jobExecution.getJobInstance().getJobName(),jobExecution.getStatus()
					, jobExecution.getJobParameters());
	}
	@AfterJob
	public void afterJob(JobExecution jobExecution){
		endTime = System.currentTimeMillis();

		log.info("UUID[{}] End JOB[{}] Status[{}] Elapsed[{}]ms", uuid, jobExecution.getJobInstance().getJobName(), jobExecution.getExitStatus().getExitCode(), endTime - startTime);

		if (jobExecution.getStatus() == BatchStatus.COMPLETED) {

		} else if (jobExecution.getStatus() == BatchStatus.FAILED) {
		   //job failure
		   System.exit(1);
		}
	}


	@BeforeStep
	public void beforeStep(StepExecution stepExecution) {
		log.info("UUID[{}] Start STEP#[{}] Status[{}] ExitStatus[{}]"
					, uuid, stepExecution.getStepName(), stepExecution.getStatus(), stepExecution.getExitStatus().getExitCode());
	}

	@AfterStep
	public ExitStatus afterStep(StepExecution stepExecution) {
		log.info("UUID[{}] End STEP#[{}] Status[{}] ExitStatus[{}] ReadCount[{}] WriteCount[{}]"
					, uuid, stepExecution.getStepName(), stepExecution.getStatus(), stepExecution.getExitStatus().getExitCode()
					, stepExecution.getReadCount(), stepExecution.getWriteCount());
		return stepExecution.getExitStatus();
	}


	@BeforeChunk
	public void beforeChunk(ChunkContext context) {
	}

	@AfterChunk
	public void afterChunk(ChunkContext context) {
		log.info("UUID[{}] End Chunk#STEP[{}]. Coummit Count[{}]", uuid, context.getStepContext().getStepName(), context.getStepContext().getStepExecution().getCommitCount());
	}

}