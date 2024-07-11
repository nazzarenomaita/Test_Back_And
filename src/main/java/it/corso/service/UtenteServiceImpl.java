package it.corso.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.corso.dao.CorsoDao;
import it.corso.dao.RuoloDao;
import it.corso.dao.UtenteDao;
import it.corso.dto.CorsoDto;
import it.corso.dto.UtenteDto;
import it.corso.dto.UtenteLoginDto;
import it.corso.dto.UtenteRuoloDto;
import it.corso.dto.UtenteUpdateDto;
import it.corso.model.Corso;
import it.corso.model.Ruolo;
import it.corso.model.Utente;

import jakarta.validation.Valid;

@Service
public class UtenteServiceImpl implements UtenteService {

	@Autowired
	private UtenteDao utenteDao;

	@Autowired
	private RuoloDao ruoloDao;

	@Autowired
	private CorsoDao corsoDao;
	
	private ModelMapper modelMapper = new ModelMapper();

	public void registraUtente(Utente utente) {

        utenteDao.save(utente);
		
	}

	@Override
	public boolean existsUtenteByMail(String mail) {
		
		return utenteDao.existsByEmail(mail);
	}

	@Override
	public List<UtenteDto> getUtenti() {
		
		List<Utente> users = (List<Utente>) utenteDao.findAll();
		List<UtenteDto> usersDto = new ArrayList<>();
		
		users.forEach(u -> usersDto.add(modelMapper.map(u, UtenteDto.class)));
		
		return usersDto;
	}

	@Override
	public UtenteDto getUtenteDtoByEmail(String email) {
		
		Optional<Utente> optionalUser = utenteDao.findByEmail(email);
		
		if (optionalUser.isEmpty()) {
			
			return null;
			
		}
		
		Utente user = optionalUser.get();
		return modelMapper.map(user, UtenteDto.class);
	}

	@Override
	public Utente getUtenteByEmail(String email) {
		
		Optional<Utente> userOptiion = utenteDao.findByEmail(email);
		
		if (userOptiion.isEmpty()) {
			
			return null;
			
		}
		
		return userOptiion.get();
	}


	@Override
	public Utente loginUtente(UtenteLoginDto utenteDto) {
		Utente utente = getUtenteByEmail(utenteDto.getEmail());
		if (utente.getPassword().equals(utenteDto.getPassword())) {
			return utente;
		}
		return null;
	}

	@Override
	public void updateUtente(@Valid UtenteUpdateDto user) {
		
		Optional<Utente> optionalUser = utenteDao.findByEmail(user.getEmail());
		
		if(optionalUser.isPresent()) {
			
			Utente newUser = optionalUser.get();
			
			newUser.setCognome(user.getCognome());
			newUser.setNome(user.getNome());
			newUser.setEmail(user.getEmail());
			newUser.setPassword(user.getPassword());
			
			List<UtenteRuoloDto> ruoliDto = user.getRuoli();

			List <Ruolo> ruoli = new ArrayList<>();
			
			ruoliDto.forEach(r->{
				Optional<Ruolo> ruoloOptional = ruoloDao.findById(r.getId());
				if (ruoloOptional.isPresent()) {
					ruoli.add(ruoloOptional.get());
				}
			});
			
			newUser.setRuoli(ruoli);
			
			List<CorsoDto> corsiDto = user.getCorsi();

			List <Corso> corsi = new ArrayList<>();
			
			corsiDto.forEach(c->{
				Optional<Corso> corsoOptional = corsoDao.findById(c.getId());
				if (corsoOptional.isPresent()) {
					corsi.add(corsoOptional.get());
				}
			});
			
			newUser.setCorsi(corsi);
			
			utenteDao.save(newUser);
			
		}
	}

	@Override
	public void cancellaUtenteByEmail(String email) {
		
		Optional<Utente> userOptiion = utenteDao.findByEmail(email);
		
		utenteDao.delete(userOptiion.get());
	}
}