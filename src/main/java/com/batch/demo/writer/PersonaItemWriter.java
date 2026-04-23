package com.batch.demo.writer;

import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.Chunk;
import java.util.List;
import org.springframework.stereotype.Component;

import com.batch.demo.domain.Persona;
import com.batch.demo.repository.PersonaRepository;

@Component
public class PersonaItemWriter implements ItemWriter<Persona> {
	
	private final PersonaRepository repository;

    public PersonaItemWriter(PersonaRepository repository) {
        this.repository = repository;
    }

    @Override
    public void write(Chunk<? extends Persona> chunk) {
        repository.saveAll(chunk.getItems());
    }

}
