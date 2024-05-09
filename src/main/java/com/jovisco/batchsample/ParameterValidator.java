package com.jovisco.batchsample;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

public class ParameterValidator implements JobParametersValidator {

    @Override
    public void validate(@Nullable JobParameters parameters) throws JobParametersInvalidException {
        if (parameters == null) {
            throw new JobParametersInvalidException("All required parameters are missing");
        }
        
        var inputFile = parameters.getString("input.file");
        if (!StringUtils.hasText(inputFile)) {
            throw new JobParametersInvalidException("Required parameter 'input.file' is missing");
        }

        var outputFile = parameters.getString("output.file");
        if (!StringUtils.hasText(outputFile)) {
            throw new JobParametersInvalidException("Required parameter 'output.file' is missing");
        }

        var skipFile = parameters.getString("skip.file");
        if (!StringUtils.hasText(skipFile)) {
            throw new JobParametersInvalidException("Required parameter 'skip.file' is missing");
        }

         var year = parameters.getString("data.year");
        if (!StringUtils.hasText(year)) {
            throw new JobParametersInvalidException("Required parameter 'data.year' is missing");
        }

        var month = parameters.getString("data.month");
        if (!StringUtils.hasText(month)) {
            throw new JobParametersInvalidException("Required parameter 'data.month' is missing");
        }
    }
}
