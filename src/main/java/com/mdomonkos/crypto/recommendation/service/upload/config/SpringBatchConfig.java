package com.mdomonkos.crypto.recommendation.service.upload.config;

import com.mdomonkos.crypto.recommendation.service.model.Crypto;
import com.mdomonkos.crypto.recommendation.service.upload.model.CryptoRaw;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import java.nio.charset.StandardCharsets;

@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {

  private static final int LINES_TO_SKIP = 1;

  @Bean
  public Job job(JobBuilderFactory jobBuilderFactory,
                 StepBuilderFactory stepBuilderFactory,
                 ItemReader<CryptoRaw> itemReader,
                 ItemProcessor<CryptoRaw, Crypto> itemProcessor,
                 ItemWriter<Crypto> itemWriter,
                 JobExecutionListener listener) {

    Step step = stepBuilderFactory.get("readCsvFileStep")
                                  .<CryptoRaw, Crypto>chunk(200)
                                  .reader(itemReader)
                                  .processor(itemProcessor)
                                  .writer(itemWriter)
                                  .build();

    Job job = jobBuilderFactory.get("readCsvFilesJob")
                               .incrementer(new RunIdIncrementer())
                               .flow(step)
                               .end()
                               .listener(listener)
                               .build();
    return job;
  }

  @Qualifier("cryptoReader")
  @Bean
  @StepScope
  public FlatFileItemReader<CryptoRaw> itemReader(@Value("#{jobParameters['fileLocation']}") String fileLocation) {

    FlatFileItemReader<CryptoRaw> reader = new FlatFileItemReader<>();
    reader.setName(("cvsReader"));
    reader.setLinesToSkip(LINES_TO_SKIP);
    reader.setEncoding(StandardCharsets.UTF_8.name());
    reader.setResource(new FileSystemResource(fileLocation));
    reader.setLineMapper(lineMapper());
    reader.open(new ExecutionContext());
    return reader;
  }

  @Bean
  public LineMapper<CryptoRaw> lineMapper() {
    DefaultLineMapper<CryptoRaw> defaultLineMapper = new DefaultLineMapper<>();
    DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();

    lineTokenizer.setDelimiter(",");
    lineTokenizer.setStrict(false);
    lineTokenizer.setNames(new String[] {"timestamp", "symbol", "price"});

    BeanWrapperFieldSetMapper<CryptoRaw> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
    fieldSetMapper.setTargetType(CryptoRaw.class);

    defaultLineMapper.setLineTokenizer(lineTokenizer);
    defaultLineMapper.setFieldSetMapper(fieldSetMapper);

    return defaultLineMapper;
  }
}
