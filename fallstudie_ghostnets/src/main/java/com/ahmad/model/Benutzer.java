package com.ahmad.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.ahmad.util.BenutzerTyp;

import jakarta.persistence.*;

/**
 * Hier habe ich in der Anwendung geprüft, ob eine Person anonym ist (über das
 * Feld istAnonym) und ob es sich um eine BERGENDER- oder MELDENDER-Person
 * handelt.
 * 
 * Anonyme Meldungen: Wenn benutzerTyp auf MELDENDER gesetzt ist und istAnonym
 * auf true, muss keine Telefonnummer angegeben werden. Bergende Personen: Wenn
 * benutzerTyp auf BERGENDER gesetzt ist, kann das Ghostnet dieser Person
 * zugeordnet werden, und das Feld telefonnummer sollte befüllt sein.
 * 
 * @author Ahmad Alrefai
 */

@Entity
@Table(name = "benutzer")
public class Benutzer implements Serializable {

	private static final long serialVersionUID = 1570455481052402699L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@Column(name = "vorname", nullable = false)
	private String vorname;

	@Column(name = "nachname", nullable = false)
	private String nachname;

	@Column(name = "password_hash", nullable = false)
	private String passwordHash;

	@Column(name = "email", nullable = false, unique = true)
	private String email;
	@Column(name = "telefonnr", nullable = true)
	private String telefonnr;

	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;

	// Liste der Ghostnets, die mit dem Benutzer als Abrufer verknüpft sind.
	@OneToMany(mappedBy = "retriever", fetch = FetchType.EAGER)
	private List<Ghostnet> linkedGhostnets;

	@Enumerated(EnumType.STRING)
	@Column(name = "benutzer_typ", nullable = false)
	private BenutzerTyp benutzerTyp;

	@Column(name = "ist_anonym", nullable = false)
	private boolean istAnonym = false;

	public Benutzer() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Benutzer) {
			Benutzer b = (Benutzer) obj;
			if (b.getVorname().equals(this.vorname) && b.getNachname().equals(this.nachname)
					&& b.getPasswordHash().equals(this.passwordHash)) {
				return true;
			}
		}
		return false;
	}

	public Long getId() {
		return id;
	}

	public String getVorname() {
		return vorname;
	}

	public void setVorname(String vorname) {
		this.vorname = vorname;
	}

	public String getNachname() {
		return nachname;
	}

	public void setNachname(String nachname) {
		this.nachname = nachname;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefonnr() {
		return telefonnr;
	}

	public void setTelefonnr(String telefonnr) {
		this.telefonnr = telefonnr;
	}

	public List<Ghostnet> getLinkedGhostnets() {
		return linkedGhostnets;
	}

	public void setLinkedGhostnets(List<Ghostnet> linkedGhostnets) {
		this.linkedGhostnets = linkedGhostnets;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public BenutzerTyp getBenutzerTyp() {
		return benutzerTyp;
	}

	public void setBenutzerTyp(BenutzerTyp benutzerTyp) {
		this.benutzerTyp = benutzerTyp;
	}

	public boolean isIstAnonym() {
		return istAnonym;
	}

	public void setIstAnonym(boolean istAnonym) {
		this.istAnonym = istAnonym;
	}

}
