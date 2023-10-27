package com.IpldashboardApi.IpldashboardApi.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import com.IpldashboardApi.IpldashboardApi.entities.Match;
import com.IpldashboardApi.IpldashboardApi.repo.MatchRepo;
import com.IpldashboardApi.IpldashboardApi.request.InputDataSet;
import com.IpldashboardApi.IpldashboardApi.utils.MatchDataProcess;

import lombok.AllArgsConstructor;
@Configuration
@EnableBatchProcessing
@AllArgsConstructor
public class BatchConfig {
	
	private final  String [] fIELD_NAME = new  String[] {
			"ID","City","Date","Season","MatchNumber","Team1","Team2","Venue","TossWinner","TossDecision","SuperOver","WinningTeam","WonBy","Margin","method","Player_of_Match","Team1Players","Team2Players","Umpire1","Umpire2"
	};

	private JobBuilderFactory jobBuilderFactory;
	
	 private StepBuilderFactory stepBuilderFactory;
	 @Autowired
	 private MatchRepo ItemWriter;
		/*
		 * @StepScope
		 * 
		 * @Bean public FlatFileItemReader<InputDataSet> reader() {
		 * 
		 * return new FlatFileItemReaderBuilder<InputDataSet>().name("InputDataSet")
		 * .resource(new
		 * ClassPathResource("IPLMatches.csv")).delimited().names(fIELD_NAME)
		 * .fieldSetMapper(new BeanWrapperFieldSetMapper<InputDataSet>() { {
		 * setTargetType(InputDataSet.class); } }).build();
		 * 
		 * }
		 */
	 @Bean
	 @StepScope
	 public FlatFileItemReader<InputDataSet> reader() {
	    
	     FlatFileItemReader<InputDataSet> itemReader = new FlatFileItemReader<>();
	     itemReader.setResource(new ClassPathResource("IPLMatches.csv")); // Replace with the correct file name and path
	     itemReader.setLinesToSkip(1); // Skip the header line
	     
	     // Configure line mapper
	     DefaultLineMapper<InputDataSet> lineMapper = new DefaultLineMapper<>();
	     
	     // Configure line tokenizer
	     DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
	     tokenizer.setDelimiter(",");
	     tokenizer.setNames(fIELD_NAME);
	     
	     // Configure field set mapper
	     BeanWrapperFieldSetMapper<InputDataSet> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
	     fieldSetMapper.setTargetType(InputDataSet.class);
	     
	     lineMapper.setLineTokenizer(tokenizer);
	     lineMapper.setFieldSetMapper(fieldSetMapper);
	     
	     itemReader.setLineMapper(lineMapper);
	     
	     return itemReader;
	 }
	

	private LineMapper<InputDataSet> lineMapper() {
		DefaultLineMapper<InputDataSet> lineMapper= new DefaultLineMapper<InputDataSet>();
		
		
		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
		lineTokenizer.setDelimiter(",");
		lineTokenizer.setStrict(false);
		lineTokenizer.setNames(fIELD_NAME);
		BeanWrapperFieldSetMapper<InputDataSet > fieldSetMapper = new BeanWrapperFieldSetMapper<>();
		fieldSetMapper.setTargetType(InputDataSet.class);
		lineMapper.setLineTokenizer(lineTokenizer);
		lineMapper.setFieldSetMapper(fieldSetMapper);
		
		return lineMapper;
		

	}
	 @Bean
	 public MatchDataProcess processor() {
		 return new MatchDataProcess();
	 }
	 @Bean
		public  RepositoryItemWriter<Match> writer(){
			RepositoryItemWriter<Match> writer =new  RepositoryItemWriter<>();
			writer.setRepository(ItemWriter);
			writer.setMethodName("save");
			return writer;
		}
	 @Bean
		public Step step() {
			return stepBuilderFactory.get("csv-step").<InputDataSet,Match>chunk(900).reader(reader()).processor(processor()).writer(writer()).taskExecutor(taskExecutor()).build();
		}
		
		@Bean
		public Job runJob() {
			return jobBuilderFactory.get("matchData").flow(step()).end().build();
		}
		@Bean
		public  TaskExecutor taskExecutor() {
			SimpleAsyncTaskExecutor asyncTaskExecutor =  new SimpleAsyncTaskExecutor();
			asyncTaskExecutor.setConcurrencyLimit(100);
			return asyncTaskExecutor;
		}
}
