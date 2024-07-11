package it.corso.controller;

import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;

import it.corso.dto.CorsoDto;
import it.corso.dto.UtenteDto;
import it.corso.dto.UtenteUpdateDto;
import it.corso.service.CorsoService;
import it.corso.service.UtenteService;

import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("/utenti")
public class UtenteController {
	
	@Autowired
	private UtenteService utenteService;
	
	@Autowired
	private CorsoService corsoService;
	
	@GET
	@Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
	public Response getAllUtenti () {
		
		List<UtenteDto> utenti = utenteService.getUtenti();
		
		return Response.status(Status.OK).entity(utenti).build();
	}

    @GET
    @Path("/get/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUtenteByEmail(@PathParam("email") String email) {
    	if(email == null || email == "") {
            return Response.status(Status.BAD_REQUEST).build();
    	}
        UtenteDto utente = utenteService.getUtenteDtoByEmail(email);
        if (utente != null) {
            return Response.status(Status.OK).entity(utente).build();
        }
        return Response.status(Status.NOT_FOUND).build();
    }

	
	@DELETE
	@Path("/delete/{email}")
	public Response deleteUser (@PathParam("email") String email) {
		try {
			utenteService.cancellaUtenteByEmail(email);
			return Response.status(Status.OK)
					.build();
			} catch (Exception e) {
				return Response.status(Status.BAD_REQUEST)
						.build();
			}
	}
	
	@PUT
	@Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
	public Response updateUser (@Valid @RequestBody UtenteUpdateDto user) {
		try {
			if(!utenteService.existsUtenteByMail(user.getEmail())) {
				return Response.status(Status.BAD_REQUEST).entity("non trovato").build();
			}

	        String hashedPassword = DigestUtils.sha256Hex(user.getPassword());
	        user.setPassword(hashedPassword);
			utenteService.updateUtente(user);
			return Response.status(Status.OK).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity("errore").build();
		}
	}
	@PUT
	@Path("/submit/{mail}/{id_Corso}")
    @Consumes(MediaType.APPLICATION_JSON)
	public Response submit (@PathParam("mail") String mail, @PathParam ("id_Corso") int id) {
		try {
			if(!utenteService.existsUtenteByMail(mail) || !corsoService.existsCorsoById(id)) {
				return Response.status(Status.BAD_REQUEST).entity("non trovato").build();
			}
			UtenteDto user = utenteService.getUtenteDtoByEmail(mail);
			CorsoDto corso = corsoService.getCorsoById(id);
			UtenteUpdateDto newUser = new UtenteUpdateDto();
			
			newUser.setCognome(user.getCognome());
			newUser.setNome(user.getNome());
			newUser.setEmail(user.getEmail());
			newUser.setPassword(user.getPassword());
			
			List<CorsoDto> corsi =user.getCorsi();
			corsi.add(corso);
			newUser.setRuoli(user.getRuoli());
			
			newUser.setCorsi(corsi);
			utenteService.updateUtente(newUser);
			
			return Response.status(Status.OK).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity("errore").build();
		}
	}
}
  
