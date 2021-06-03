package com.eventoapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//import org.springframework.data.repository.CrudRepository;
import com.eventoapp.eventoapp.models.Evento;

public interface EventoRepository extends JpaRepository<Evento, String> {
	Evento findByCodigo(long codigo);

}
