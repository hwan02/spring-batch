package com.konai.batch.config.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


@Component
public class ParametersValidator implements JobParametersValidator{

	private static final Logger log = LoggerFactory.getLogger(ParametersValidator.class);

	@Override
	public void validate(JobParameters parameters)
			throws JobParametersInvalidException {

		if ( StringUtils.isEmpty(parameters.getString("clrDt")) ){
			log.error("Parameter not exist. parameter: clrDt[null]");
			throw new JobParametersInvalidException("Parameter not exist. Parameter: clrDt[null]");
		}

		if ( StringUtils.isEmpty(parameters.getString("decider")) ){
			log.error("Parameter not exist. parameter: decider[null]");;
			throw new JobParametersInvalidException("Parameter not exist. Parameter: decider[null]");
		}

	}

}