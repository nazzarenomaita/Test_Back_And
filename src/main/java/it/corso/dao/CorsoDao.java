package it.corso.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.corso.model.Categoria;
import it.corso.model.Corso;

public interface CorsoDao extends CrudRepository<Corso, Integer>{

	List<Corso> findByCategoria(Categoria categoria);

}

