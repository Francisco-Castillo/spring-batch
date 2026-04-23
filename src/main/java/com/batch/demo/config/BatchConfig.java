package com.batch.demo.config;



import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.SkipListener;

import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;

import org.springframework.batch.core.repository.JobRepository;

import com.batch.demo.domain.Persona;
import com.batch.demo.dto.PersonaExcelDTO;
import com.batch.demo.processor.PersonaItemProcessor;
import com.batch.demo.reader.ExcelItemReader;
import com.batch.demo.writer.PersonaItemWriter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.SkipListener;

import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.repository.JobRepository;

import org.springframework.transaction.PlatformTransactionManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Aqui debemos gestionar el "Chunk" mas la tolerancia a fallos.
 */
@Configuration
@EnableBatchProcessing
public class BatchConfig {
	
    private static final Logger log = LoggerFactory.getLogger(BatchConfig.class);

	
	@Bean
    public Job importJob(JobRepository jobRepository, Step step) {
        return new JobBuilder("excel-import-job", jobRepository)
                .start(step)
                .build();
    }

    @Bean
    public Step step(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            ExcelItemReader reader,
            PersonaItemProcessor processor,
            PersonaItemWriter writer
    ) {
        return new StepBuilder("step-1", jobRepository)
                .<PersonaExcelDTO, Persona>chunk(5, transactionManager)
                
                .reader(reader)
                .processor(processor)
                .writer(writer)

                // Tolerancia a errores
                .faultTolerant()
                .skip(IllegalArgumentException.class)
                .skipLimit(100)

                .listener(skipListener())

                .build();
    }

    /**
     * SKIP LISTENER
     */
    @Bean
    public SkipListener<PersonaExcelDTO, Persona> skipListener() {
        return new SkipListener<>() {

            @Override
            public void onSkipInRead(Throwable t) {
                log.error("Error al leer registro: {}", t.getMessage(), t);
            }

            @Override
            public void onSkipInProcess(PersonaExcelDTO item, Throwable t) {
                log.warn("Registro inválido en procesamiento: {} - error: {}", item, t.getMessage());
            }

            @Override
            public void onSkipInWrite(Persona item, Throwable t) {
                log.error("Error al escribir registro: {} - error: {}", item, t.getMessage(), t);
            }
        };
    }

}
