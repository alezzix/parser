package com.ef.run;

import java.io.File;
import java.util.Date;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import com.ef.dto.DatesDto;
import com.ef.dto.IpDto;
import com.ef.dto.LogDto;
import com.ef.entity.IpComment;
import com.ef.entity.Log;
import com.ef.utils.DateUtil;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {


    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public DataSource dataSource;
    
    
    private final String DELIMITER = "|";
    
    @Bean
    public LogItemProcessor processorLog() {
        return new LogItemProcessor();
    }
    
    @Bean
    public IpCommentItemProcessor processorIpComment() {
        return new IpCommentItemProcessor();
    }
    
   
    @Bean(name="jobCompletionNotificationListenerFile")
    public JobCompletionNotificationListenerFile jobCompletionNotificationListenerFile() {
    	return new JobCompletionNotificationListenerFile();
    }

    @Value("${duration}")
	private String duration; 
    
    
    @Value("${startDate}")
	private String startDate; 

    @Value("${threshold}")
   	private String threshold; 
    
    @Value("${accesslog}")
   	private String accesslog; 
    
    private DatesDto datesDto;
    
    @Bean
    public FlatFileItemReader<LogDto> readerLog() {
    	    	
    	
        FlatFileItemReader<LogDto> reader = new FlatFileItemReader<LogDto>();
        
        File file = new File(accesslog);
        Resource rs = new FileSystemResource(file);
        
        reader.setResource(rs);
        reader.setLineMapper(new DefaultLineMapper<LogDto>() {{
            setLineTokenizer(new DelimitedLineTokenizer(DELIMITER) {{
                setNames(new String[] { "accessDate", "ip", "request", "accessStatus", "userAgent" });
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<LogDto>() {{
                setTargetType(LogDto.class);
            }});
        }});
        return reader;
    }
    
    
  
    @Bean
    public ItemReader<IpDto> readerParameters() {
    	
    	verifiyParameters();
    	
    	 final String QUERY_FIND_IPS =
                "SELECT ip , count(*) as count from log where access_date  BETWEEN '"+    	 
                DateUtil.dateToString(this.datesDto.getStartDate())	    	        
                + "' AND '"+                
                DateUtil.dateToString(this.datesDto.getFinishDate())
                + "' group by  ip "
                + " HAVING count >= "+this.threshold;
            
    	JdbcCursorItemReader<IpDto> databaseReader = new JdbcCursorItemReader<>();    
    	databaseReader.setDataSource(dataSource);
    	databaseReader.setSql(QUERY_FIND_IPS);
    	databaseReader.setRowMapper(new BeanPropertyRowMapper<>(IpDto.class));
    
       return databaseReader;
   }
    
    private void verifiyParameters() {
    	
    	
    	if(startDate.isEmpty()||duration.isEmpty() ||threshold.isEmpty() ){
    		 String errorMsg =
       	          "Error: Parameters are required ";
       	      throw new IllegalArgumentException(errorMsg);
    	}
    	
		Date dateStart;
		try {
			dateStart = DateUtil.PARAMETER_START_DATE.parse(startDate);			
		} catch (java.text.ParseException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		int hours = duration.equals("daily") ? 24 : 1;
	
	    Date endDate ;
	    endDate = DateUtil.addHours(dateStart, hours);
	    
	    
	    this.datesDto = new DatesDto(dateStart, endDate);
    
    	
	}



	@Bean
    public JdbcBatchItemWriter<Log> writerLogTable() {
        JdbcBatchItemWriter<Log> writer = new JdbcBatchItemWriter<Log>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Log>());
        writer.setSql("INSERT INTO log (access_date, ip,request,access_status,user_agent)VALUES (:accessDate, :ip, :request, :accessStatus, :userAgent)");
        writer.setDataSource(dataSource);
        return writer;
    }
	
  @Bean
  public JdbcBatchItemWriter<IpComment> writerIpCommentTable() {
  
     JdbcBatchItemWriter<IpComment> writer = new JdbcBatchItemWriter<IpComment>();
      writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<IpComment>());
     writer.setSql("INSERT INTO ip_comment (ip, ip_comment) VALUES (:ip, :comment)");
     writer.setDataSource(dataSource);
    
    
      return writer;
  }
    
    
    @Bean
    public Job createJob(JobCompletionNotificationListenerFile listener) {
    	return jobBuilderFactory.get("createJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1()).next(step2())
                .end()
                .build();
    }
   
    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<LogDto, Log> chunk(10)
                .reader(readerLog())
                .processor(processorLog())
                .writer(writerLogTable())
                .build();
    }
    
    @Bean
    public Step step2() {
        return stepBuilderFactory.get("step2")
                .<IpDto, IpComment> chunk(10)
                .reader(readerParameters())
               .processor(processorIpComment())
               .writer(writerIpCommentTable())
               .build();
   }
    
    
}
