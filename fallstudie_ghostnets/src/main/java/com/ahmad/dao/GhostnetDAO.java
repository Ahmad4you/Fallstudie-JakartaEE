package com.ahmad.dao;

import java.util.List;
import java.util.ResourceBundle;
import java.util.Date;

import com.ahmad.model.Ghostnet;
import com.ahmad.controller.UserSession;
import com.ahmad.model.Benutzer;
import com.ahmad.util.EntityManagerProducer;
import com.ahmad.util.GhostnetStatus;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

/**
 * 
 * @author Ahmad Alrefai
 */

@ApplicationScoped
public class GhostnetDAO {

    @Inject
    private EntityManagerProducer emp;
    
    @Inject
    private BenutzerDAO benutzerDAO;
    
    @Inject
    private UserSession userSession;

    
    public void saveGhostnet(Ghostnet ghostnet) {
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            em = emp.createEntityManager();
            tx = em.getTransaction();
            tx.begin();
            
            // Setzt das Erstellungsdatum auf das aktuelle Datum, falls nicht gesetzt
            if (ghostnet.getSichtungDatum() == null) {
                ghostnet.setSichtungDatum(new Date());
            }
            
            if (ghostnet.getId() != null) {
            	// Merge
            }

         // Benutzer abrufen und zuweisen
            String currentangemeldeteEmail = userSession.getCurrentUserEmail();
            if (currentangemeldeteEmail != null) {
            	
            	Benutzer benutzer = benutzerDAO.getBenutzerByEmail(currentangemeldeteEmail);
                ghostnet.setRetriever(benutzer);
                em.persist(ghostnet); 
                tx.commit();
            } else {
//            	throw new IllegalStateException("Kein angemeldeter Benutzer.");
            	System.out.println("Kein angemeldeter Benutzer.... Ghostnetz gespeichert");
            	em.persist(ghostnet); 
                tx.commit();
            }     
        } catch (Exception e) {
            System.err.println("Fehler beim Speichern des Ghostnets: " + e.getMessage());
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
    
    // Methode zum Abrufen aller Ghostnets
    public List<Ghostnet> getAllGhostnets() {
        EntityManager em = null;
        try {
            em = emp.createEntityManager();
            TypedQuery<Ghostnet> query = em.createQuery("SELECT g FROM Ghostnet g", Ghostnet.class);
            query.setHint("jakarta.persistence.cache.storeMode", "REFRESH"); // Cache umgehen, um die neuesten Daten zu erhalten
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Fehler beim Abrufen der Ghostnets: " + e.getMessage());
            e.printStackTrace();
            return null;
        } 
    }

    // Methode zum Aktualisieren des Status eines Ghostnets
    public void updateStatus(Long ghostnetId, GhostnetStatus newStatus) {
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            em = emp.createEntityManager();
            tx = em.getTransaction();
            tx.begin();
            
            Ghostnet ghostnet = em.find(Ghostnet.class, ghostnetId);
            if (ghostnet != null) {
                ghostnet.setStatus(newStatus);
                em.merge(ghostnet);
            }
            
            tx.commit();
        } catch (Exception e) {
            System.err.println("Fehler beim Aktualisieren des Ghostnet-Status: " + e.getMessage());
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
        } 
    }

    // Methode zum Löschen eines Ghostnets
    public void deleteGhostnet(Long ghostnetId) {
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            em = emp.createEntityManager();
            tx = em.getTransaction();
            tx.begin();
            
            Ghostnet ghostnet = em.find(Ghostnet.class, ghostnetId);
            if (ghostnet != null) {
                em.remove(ghostnet);
            }
            
            tx.commit();
        } catch (Exception e) {
            System.err.println("Fehler beim Löschen des Ghostnets: " + e.getMessage());
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
        } 
    }
    
    // Methode zum Zuweisen eines Benutzers als Retriever für ein Ghostnet
    public void assignRetriever(Long ghostnetId, Benutzer retriever) {
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            em = emp.createEntityManager();
            tx = em.getTransaction();
            tx.begin();
            
            Ghostnet ghostnet = em.find(Ghostnet.class, ghostnetId);
            if (ghostnet != null) {
                ghostnet.setRetriever(retriever);
                em.merge(ghostnet);
            }
            
            tx.commit();
        } catch (Exception e) {
            System.err.println("Fehler beim Zuweisen des Retrievers für das Ghostnet: " + e.getMessage());
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
        } 
    }
    
    public List<Ghostnet> getGhostnetsByUser(Benutzer user) {
    	EntityManager em = null;
        try {
            em = emp.createEntityManager();
            
            TypedQuery<Ghostnet> query = em.createQuery("SELECT g FROM Ghostnet g WHERE g.retriever = :user", Ghostnet.class)
                    .setParameter("user", user);
            query.setHint("jakarta.persistence.cache.storeMode", "REFRESH"); // Cache umgehen, um die neuesten Daten zu erhalten
        return query.getResultList();
        } catch (Exception e) {
            System.err.println("Fehler beim Abrufen der Ghostnets: " + e.getMessage());
            e.printStackTrace();
            return null;
        } 
    }
    
 // Methode zum Aktualisieren mehrerer Ghostnets in der Datenbank
    @Transactional
    public void updateGhostnets(List<Ghostnet> ghostnetsZugeordnet) {
        EntityManager em = null;
        EntityTransaction tx = null;
        
        try {
            em = emp.createEntityManager();
            tx = em.getTransaction();
            tx.begin();

            for (Ghostnet ghostnet : ghostnetsZugeordnet) {
                // Überprüfen, ob das Ghostnet bereits in der Datenbank existiert
                if (ghostnet.getId() != null) {
                    // Aktualisieren des bestehenden Datensatzes
                    em.merge(ghostnet);
                } else {
                    // Falls das Ghostnet neu ist, persistieren
                    em.persist(ghostnet);
                }
            }
            
            tx.commit();
        } catch (Exception e) {
            System.err.println("Fehler beim Aktualisieren der Ghostnets: " + e.getMessage());
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
        } 
    }
    
    @Transactional
    public void markAsVerschollen(Long ghostnetId, Benutzer currentUser) {
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            em = emp.createEntityManager();
            tx = em.getTransaction();
            tx.begin();
            
            // Ghostnet finden
            Ghostnet ghostnet = em.find(Ghostnet.class, ghostnetId);
            if (ghostnet != null) {
                // Überprüfen, ob der Benutzer berechtigt ist, den Status zu ändern
                if (currentUser != null) {
                    ghostnet.setStatus(GhostnetStatus.VERSCHOLLEN);
                    ghostnet.setRetriever(currentUser);
                    em.merge(ghostnet);
                } else {
                    throw new SecurityException("Nicht berechtigt, dieses Geisternetz als verschollen zu melden.");
                }
            } else {
                throw new IllegalArgumentException("Geisternetz nicht gefunden.");
            }
            
            tx.commit();
        } catch (Exception e) {
            System.err.println("Fehler beim Ändern des Geisternetz-Status auf verschollen: " + e.getMessage());
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
        } 
    }
    	
    // Status auf „GEBORGEN“ setzt und das Geisternetz dem aktuellen Benutzer zuordnet.
    public void updateStatusToGeborgen(Long ghostnetId, Benutzer currentUser) {
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            em = emp.createEntityManager();
            tx = em.getTransaction();
            tx.begin();
            
            Ghostnet ghostnet = em.find(Ghostnet.class, ghostnetId);
            if (ghostnet != null) {
                // Nur aktualisieren, wenn der Status nicht schon "GEBORGEN" ist
                if (ghostnet.getStatus() != GhostnetStatus.GEBORGEN) {
                    // Aktuellen Benutzer als Retriever setzen, falls noch kein Retriever vorhanden ist
                    if (ghostnet.getRetriever() == null || !ghostnet.getRetriever().equals(currentUser)) {
                        ghostnet.setRetriever(currentUser);
                    }
                    
                    // Status auf GEBORGEN setzen
                    ghostnet.setStatus(GhostnetStatus.GEBORGEN);
                    em.merge(ghostnet); // Update speichern
                }
            }
            
            tx.commit();
        } catch (Exception e) {
            System.err.println("Fehler beim Aktualisieren des Ghostnet-Status auf GEBORGEN: " + e.getMessage());
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
        } 
    }


    
   }
