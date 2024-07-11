package it.corso.model;

import java.util.ArrayList;
import java.util.List;

import it.corso.enums.TipoRuolo;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinColumn;
@Entity
public class Ruolo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)    
    @Column(name = "ID_G")
    private int id;
    
    @Enumerated(EnumType.STRING)    
    @Column(name = "TIPOLOGIA")
    private TipoRuolo tipologia;
    
    @ManyToMany(mappedBy = "ruoli", cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    private List<Utente> utenti = new ArrayList<>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public TipoRuolo getTipologia() {
		return tipologia;
	}

	public void setTipologia(TipoRuolo tipologia) {
		this.tipologia = tipologia;
	}

	public List<Utente> getUtenti() {
		return utenti;
	}

	public void setUtenti(List<Utente> utenti) {
		this.utenti = utenti;
	}
    
}