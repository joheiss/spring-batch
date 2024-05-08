package com.jovisco.batchsample;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.repository.JobRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BatchSampleJob implements Job {

    private final static String JOB_NAME = "BatchSample";

    private final JobRepository jobRepository;

    @SuppressWarnings("null")
    @Override
    public void execute(JobExecution execution) {
        try {
            var jobParameters = execution.getJobParameters();
            var inputFile = jobParameters.getString("input.file");

            System.out.println("Execute Job: " + getName() + " with file " + inputFile);
            // throw new Exception("Unable to process batch job");
             
            execution.setStatus(BatchStatus.COMPLETED);
            execution.setExitStatus(ExitStatus.COMPLETED);
        } catch (Exception e) {
           execution.addFailureException(e);
           execution.setStatus(BatchStatus.COMPLETED);
           execution.setExitStatus(ExitStatus.FAILED.addExitDescription(e.getMessage()));
        } finally {
            jobRepository.update(execution);
        }
    }

    @SuppressWarnings("null")
    @Override
    public String getName() {
        return JOB_NAME;
    }

}
