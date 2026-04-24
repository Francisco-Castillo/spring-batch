package com.batch.demo.processor;


import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.batch.demo.domain.Persona;
import com.batch.demo.dto.PersonaExcelDTO;

@Component
public class PersonaItemProcessor implements ItemProcessor<PersonaExcelDTO, Persona>{

	@Override
	public Persona process(PersonaExcelDTO item) throws Exception {
		 if (item.getEmail() == null || item.getEmail().isBlank()) {
		        throw new IllegalArgumentException("Email inválido");
		    }

		    if (!item.getEmail().contains("@")) {
		        throw new IllegalArgumentException("Email mal formado");
		    }

		    Persona persona = new Persona();
		    persona.setNombre(item.getNombre());
		    persona.setEdad(item.getEdad());
		    persona.setEmail(item.getEmail());

		    return persona;
	}

}
