package com.jovisco.batchsample;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BatchSampleConfiguration {

@Bean
  public Job job(JobRepository jobRepository) {
    return new BatchSampleJob(jobRepository);
  }
}
