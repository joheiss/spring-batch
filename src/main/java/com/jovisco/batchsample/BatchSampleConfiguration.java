package com.jovisco.batchsample;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.DefaultJobParametersValidator;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.support.JdbcTransactionManager;

import javax.sql.DataSource;
@Configuration
public class BatchSampleConfiguration {

@Bean
  public Job job(JobRepository jobRepository, Step step1, Step step2) {
    // return new BatchSampleJob(jobRepository);

    // prepare validator
    String[] requiredParameters = { "input.file" };
    String[] optionalParameters = { "file.format" };
    var defaultJobParametersValidator = new DefaultJobParametersValidator(requiredParameters, optionalParameters);
    defaultJobParametersValidator.afterPropertiesSet();

    return new JobBuilder("SampleBatchJob", jobRepository)
        // .validator(defaultJobParametersValidator)
        .start(step1)
        .next(step2)
        .build();
  }

  @Bean
  public Step step1(JobRepository jobRepository, JdbcTransactionManager transactionManager) {
    return new StepBuilder("filePreparation", jobRepository)
        .tasklet(new FilePreparationTasklet(), transactionManager)
        .build();
  }

  @Bean
  public FlatFileItemReader<BillingData> billinDataFileReader() {
    return new FlatFileItemReaderBuilder<BillingData>()
      .name("billingDataFileReader")
      .resource(new FileSystemResource("staging/billing_2023_01.csv"))
      .delimited()
      .names("dataYear", "dataMonth", "accountId", "phoneNumber", "dataUsage", "callDuration", "smsCount")
      .targetType(BillingData.class)
      .build();
  }

  @Bean
  public JdbcBatchItemWriter<BillingData> billingDataTableWriter(DataSource dataSource) {
    
    var sql = "insert into BILLING_DATA values (:dataYear, :dataMonth, :accountId, :phoneNumber, :dataUsage, :callDuration, :smsCount)";

    return new JdbcBatchItemWriterBuilder<BillingData>()
      .dataSource(dataSource)
      .sql(sql)
      .beanMapped()
      .build();
  }

  @Bean
  public Step step2(
    JobRepository jobRepository, 
    JdbcTransactionManager transactionManager, 
    ItemReader<BillingData> billingDataFileReader,
    ItemWriter<BillingData> billingDataTableWriter
  ) {
    return new StepBuilder("fileIngestion", jobRepository)
      .<BillingData, BillingData>chunk(100, transactionManager)
      .reader(billingDataFileReader)
      .writer(billingDataTableWriter)
      .build();
  }
}
