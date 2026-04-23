package com.batch.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.batch.demo.domain.Persona;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Long> {

}
