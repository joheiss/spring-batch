package com.jovisco.batchsample;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Paths;

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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;

@SpringBatchTest
@SpringBootTest
@ExtendWith(OutputCaptureExtension.class)
class BatchSampleApplicationTests {

	@Autowired
   	private JobLauncherTestUtils jobLauncherTestUtils;

	@Autowired	
   	private JobRepositoryTestUtils jobRepositoryTestUtils;

	@Autowired
   	private JdbcTemplate jdbcTemplate;

	@BeforeEach
	void setUp() {
    	jobRepositoryTestUtils.removeJobExecutions();

		JdbcTestUtils.deleteFromTables(jdbcTemplate, "BILLING_DATA");
	}

	@Test
	void testJobExecution(CapturedOutput output) throws Exception {
		// given

		var jobParameters = new JobParametersBuilder()
			.addString("input.file", "input/billing-2023-01.csv")
			.addString("output.file", "staging/billing-report-2023-01.csv")
			.addString("data.year", "2023")
			.addString("data.month", "1")
			// .addJobParameter("data.year", 2023, Integer.class)
			// .addJobParameter("data.month", 1, Integer.class)
			.addString("file.format", "csv", false)
			.toJobParameters();

		// when
		var jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

		// then 
		assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
		assertTrue(Files.exists(Paths.get("staging", "billing-2023-01.csv")));
		assertEquals(1000, JdbcTestUtils.countRowsInTable(jdbcTemplate, "BILLING_DATA"));

		// ... output file exists & number of lines = 938 (with customized pricing / threshold 
		var billingReport = Paths.get("staging", "billing-report-2023-01.csv");
		assertTrue(Files.exists(billingReport));
		assertEquals(938, Files.lines(billingReport).count());
	}
}
