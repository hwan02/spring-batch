package com.konai.batch.core.service.impl;

import com.konai.batch.core.enumeration.DeciderCode;
import com.konai.batch.core.repository.IRepository;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.util.HashMap;

public class InitTasklet implements Tasklet {

	private static final Logger log = LoggerFactory.getLogger(InitTasklet.class);

	@Setter
	private IRepository repository;

	HashMap<String, Object> condition = new HashMap<>();
	private String clrSvcNo;

	public InitTasklet(String clrSvcNo) {
		this.clrSvcNo = clrSvcNo;
	}

	public InitTasklet(String clrSvcNo, IRepository repository) {
		this.clrSvcNo = clrSvcNo;
		this.repository = repository;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

		log.info("[jobExecution.getJobParameters()] : {}", chunkContext.getStepContext().getJobParameters().toString());

		String clrDt = (String) chunkContext.getStepContext().getJobParameters().getOrDefault("clrDt", "");
		String decider = (String) chunkContext.getStepContext().getJobParameters().getOrDefault("decider", "");

		DeciderCode deciderCode = DeciderCode.find(decider);

		if( deciderCode == null ) {

			log.error("##### Invalid decider code[{}] #####", decider);
			chunkContext.getStepContext().getStepExecution().setStatus(BatchStatus.FAILED);
			chunkContext.getStepContext().getStepExecution().setExitStatus(ExitStatus.STOPPED);
			contribution.setExitStatus(ExitStatus.STOPPED);

		} else {
			String[] tableList = clrSvcNo.split(",");

			condition.put("clrDt", clrDt);
			condition.put("clrSvcNoList",  tableList);
			repository.deleteList(condition);

		}

		return RepeatStatus.FINISHED;
	}

}
