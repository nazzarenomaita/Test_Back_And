package it.corso.dao;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.corso.model.Utente;

public interface UtenteDao extends CrudRepository<Utente, Integer>{

	boolean existsByEmail(String mail);
	Optional<Utente> findByEmail(String mail);
}

