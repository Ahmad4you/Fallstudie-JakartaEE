package com.ahmad.dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

import org.mindrot.jbcrypt.BCrypt;

import com.ahmad.model.Benutzer;
import com.ahmad.util.BenutzerTyp;
import com.ahmad.util.EntityManagerProducer;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

/**
 * 
 * @author Ahmad Alrefai
 */

@ApplicationScoped
public class BenutzerDAO {
	
	@Inject
	private EntityManagerProducer emp;
	
	 public void saveBenutzer(Benutzer benutzer) {
		 EntityManager em = null;
		 EntityTransaction tx = null;
	        try {
	        	em = emp.createEntityManager();
	        	tx = em.getTransaction();
	        	tx.begin();
	        	// Passwort hashen und dem Benutzer-Objekt zuweisen
	            String hashedPassword = BCrypt.hashpw(benutzer.getPasswordHash(), BCrypt.gensalt());
	            benutzer.setPasswordHash(hashedPassword);

	            benutzer.setCreatedAt(LocalDateTime.now());
	            // benutzerTyp bestimmen
	            if (  benutzer.getTelefonnr().trim().isEmpty() ) {
	            	benutzer.setIstAnonym(true);
	            	benutzer.setBenutzerTyp(BenutzerTyp.MELDENDER);
	            } else {
	            	benutzer.setBenutzerTyp(BenutzerTyp.BERGENDER);
	            }
	            
	            em.persist(benutzer); 
	        	tx.commit();
	        } catch (Exception e) {
	        	System.err.println("Fehler beim Speichern des Benutzers: " + e.getMessage());
	            FacesContext context = FacesContext.getCurrentInstance();
	            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
	                    ResourceBundle.getBundle("messages", context.getViewRoot().getLocale())
	                    .getString("custom.error.speichern.scheitert"), null);
	            context.addMessage(null, message);
	            if (tx != null && tx.isActive()) { 
		            tx.rollback();
		        }
		        e.printStackTrace();
	        }
	    }
	 
	 public Benutzer getBenutzerByEmail(String email) {
		    EntityManager em = null;
		    Benutzer benutzer = null;
		    try {
		        em = emp.createEntityManager();
		        // TypedQuery verwenden, um ein Benutzer-Objekt anhand der E-Mail-Adresse abzurufen
		        TypedQuery<Benutzer> query = em.createQuery(
		            "SELECT b FROM Benutzer b WHERE b.email = :email", Benutzer.class);
		        query.setParameter("email", email);

		        // SingleResult verwenden, da die E-Mail eindeutig sein sollte
		        benutzer = query.getSingleResult();
		    } catch (NoResultException e) {
		        System.out.println("Kein Benutzer mit der E-Mail-Adresse gefunden: " + email);
		        benutzer = null; // Optional: explizit setzen, um zu verdeutlichen, dass kein Ergebnis gefunden wurde
		    } catch (Exception e) {
		        System.err.println("Fehler beim Abrufen des Benutzers: " + e.getMessage());
		        e.printStackTrace();
		    } 
		    return benutzer;
		}

	 
	 @Transactional // Startet eine Transaktion für jeden Aufruf
	 public List<Benutzer> getAllBenutzer() {
	        EntityManager em = null;
	        try {
	            em = emp.createEntityManager();
	            TypedQuery<Benutzer> query = em.createQuery("SELECT b FROM Benutzer b", Benutzer.class);
	            return query.getResultList();
	        } catch (Exception e) {
	            System.err.println("Fehler beim Abrufen der Benutzerliste: " + e.getMessage());
	            e.printStackTrace();
	            return null;
	        } 

	    }

}
