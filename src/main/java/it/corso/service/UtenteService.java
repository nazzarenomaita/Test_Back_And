package it.corso.service;

import java.util.List;

import it.corso.dto.UtenteDto;
import it.corso.dto.UtenteLoginDto;
import it.corso.dto.UtenteUpdateDto;
import it.corso.model.Utente;

import jakarta.validation.Valid;

public interface UtenteService {

	void registraUtente(Utente utente);
	List<UtenteDto> getUtenti();
	public boolean existsUtenteByMail (String mail);
	Utente getUtenteByEmail(String email);
	Utente loginUtente(UtenteLoginDto utenteDto);
	void updateUtente(@Valid UtenteUpdateDto user);
	UtenteDto getUtenteDtoByEmail(String email);
	void cancellaUtenteByEmail(String email);
}
