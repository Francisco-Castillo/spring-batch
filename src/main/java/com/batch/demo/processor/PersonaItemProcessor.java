package com.batch.demo.processor;


import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.batch.demo.domain.Persona;
import com.batch.demo.dto.PersonaExcelDTO;

@Component
public class PersonaItemProcessor implements ItemProcessor<PersonaExcelDTO, Persona>{

	@Override
	public Persona process(PersonaExcelDTO dto) throws Exception {
		 // Validaciones simples
        if (dto.getEdad() == null || dto.getEdad() < 0) {
            throw new IllegalArgumentException("Edad inválida");
        }

        if (!dto.getEmail().contains("@")) {
            throw new IllegalArgumentException("Email inválido");
        }

        Persona p = new Persona();
        p.setNombre(dto.getNombre());
        p.setEmail(dto.getEmail());
        p.setEdad(dto.getEdad());

        return p;
	}

}
