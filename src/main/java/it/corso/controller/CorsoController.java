package it.corso.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;

import it.corso.dto.CorsoDto;
import it.corso.enums.TipoRuolo;
import it.corso.jwt.JWTTokenNeeded;
import it.corso.jwt.Secured;
import it.corso.enums.TipoRuolo;
import it.corso.model.Categoria;
import it.corso.service.CategoriaService;
import it.corso.service.CorsoService;

import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;


//@JWTTokenNeeded
@Path("/corsi")
//@Secured (role = TipoRuolo.Admin)
public class CorsoController {
	
	@Autowired
	private CorsoService corsoService;
	
	@Autowired
	private CategoriaService categoriaService;
	
	@GET
	@Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
	public Response getAllCorsi () {
		
		List<CorsoDto> corsi = corsoService.getCorsi();
		
		return Response.status(Status.OK).entity(corsi).build();
	}
	
	@GET
	@Path("/cat/{categoria}")
    @Produces(MediaType.APPLICATION_JSON)
	public Response getCorsiByCategoriai (@PathParam("categoria") String nome) {
		
		Categoria categoria = categoriaService.getCategoriaByName(nome);
		List<CorsoDto> corsi = corsoService.getCorsiByCategoria(categoria);
		
		return Response.status(Status.OK).entity(corsi).build();
	}

    @GET
    @Path("/get")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCorsoById(@QueryParam("id") int id) {
    	if(id==0) {
            return Response.status(Status.BAD_REQUEST).build();
    	}
    	CorsoDto corso = corsoService.getCorsoById(id);
        if (corso != null) {
            return Response.status(Status.OK).entity(corso).build();
        }
        return Response.status(Status.NOT_FOUND).build();
    }

	
	@DELETE
	@Path("/delete/{id}")
	public Response deleteCorso (@PathParam("id") int id) {
		try {
			corsoService.cancellaCorso(id);
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
	public Response updateCorso (@RequestBody CorsoDto corso) {
		try {
			if(!corsoService.existsCorsoById(corso.getId())) {
				return Response.status(Status.BAD_REQUEST).entity("non trovato").build();
			}
			corsoService.updateCorso(corso);
			return Response.status(Status.OK).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity("errore").build();
		}
	}

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerUtente(@Valid CorsoDto corsoDto) {
		try {
			if(corsoService.existsCorsoById(corsoDto.getId())) {
				return Response.status(Status.BAD_REQUEST).entity("già registrato").build();
			}
			corsoService.registraCorso(corsoDto);
			return Response.status(Status.OK).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity("errore").build();
		}
    }
    


}
