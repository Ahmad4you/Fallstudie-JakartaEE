package com.ahmad.model;

import java.io.Serializable;
import java.util.Date;
import com.ahmad.util.GhostnetStatus;
import jakarta.persistence.*;


/**
 * 
 * @author Ahmad Alrefai
 */
@Entity
@Table(name = "ghostnet")
public class Ghostnet implements Serializable{

	private static final long serialVersionUID = -7227014865971052389L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", unique = true, nullable = false)
    private Long id;
	
	// Ghostnet location
	@Column
    private Double breite;  
	
	@Column
    private Double laenge; 

    // geschätzte Größe in quadrat meter
	@Column
    private Integer size;
	
	@Column
	@Temporal(TemporalType.DATE)
	private Date sichtungDatum;
    
	// Status -Ghostnet (e.g., GEMELDET, BERGUNG_BEVORSTEHEND, GEBORGEN, VERSCHOLLEN)
    @Enumerated(EnumType.STRING)
    private GhostnetStatus status;
    
    // Der Benutzer, der für den Abruf des Ghostnets verantwortlich ist. Entweder null oder 1 Benutzer
    @ManyToOne(fetch = FetchType.EAGER)
    private Benutzer retriever;
    
    public Ghostnet() {
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public Double getBreite() {
		return breite;
	}

	public void setBreite(Double breite) {
		this.breite = breite;
	}

	public Double getLaenge() {
		return laenge;
	}

	public void setLaenge(Double laenge) {
		this.laenge = laenge;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public Date getSichtungDatum() {
		return sichtungDatum;
	}

	public void setSichtungDatum(Date sichtungDatum) {
		this.sichtungDatum = sichtungDatum;
	}

	public GhostnetStatus getStatus() {
		return status;
	}

	public void setStatus(GhostnetStatus status) {
		this.status = status;
	}

	public Benutzer getRetriever() {
		return retriever;
	}

	public void setRetriever(Benutzer retriever) {
		this.retriever = retriever;
	}

	public void setId(Long id) {
		this.id = id;
	}
    
    
}
