package com.jovisco.batchsample;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

@SpringBatchTest
@SpringBootTest
@ExtendWith(OutputCaptureExtension.class)
class BatchSampleApplicationTests {

	@Autowired
   	private JobLauncherTestUtils jobLauncherTestUtils;

	@Autowired	
   	private JobRepositoryTestUtils jobRepositoryTestUtils;

	@BeforeEach
	void setUp() {
    	this.jobRepositoryTestUtils.removeJobExecutions();
	}

	@Test
	void testJobExecution(CapturedOutput output) throws Exception {
		// given
		var jobParameters = jobLauncherTestUtils.getUniqueJobParametersBuilder()
			.addString("input.file", "/some/input/file4")
			.addString("file.format", "csv", false)
			.toJobParameters();

		// when
		var jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

		// then 
		assertTrue(output.getOut().contains("Execute Job:"));
		assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
	}
}
