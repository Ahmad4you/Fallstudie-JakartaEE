package com.ahmad.service;

import java.util.List;
import com.ahmad.model.Ghostnet;
import com.ahmad.dao.GhostnetDAO;
import com.ahmad.model.Benutzer;
import com.ahmad.util.GhostnetStatus;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

/**
 * 
 * @author Ahmad Alrefai
 */

@Dependent
public class GhostnetService {
    
    @Inject
    private GhostnetDAO ghostnetDAO;

    @Transactional
    public void saveGhostnet(Ghostnet ghostnet) {
    	ghostnetDAO.saveGhostnet(ghostnet);
    }
    
    // Methode zum Abrufen aller Ghostnets
    public List<Ghostnet> getAllGhostnets() {
    	return ghostnetDAO.getAllGhostnets();
    }

    // Methode zum Aktualisieren des Status eines Ghostnets
    public void updateStatus(Long ghostnetId, GhostnetStatus newStatus) {
    	ghostnetDAO.updateStatus(ghostnetId, newStatus);
    }

    // Methode zum Löschen eines Ghostnets
    public void deleteGhostnet(Long ghostnetId) {
    	ghostnetDAO.deleteGhostnet(ghostnetId);
    }
    
    // Methode zum Zuweisen eines Benutzers als Retriever für ein Ghostnet
    public void assignRetriever(Long ghostnetId, Benutzer retriever) {
    	ghostnetDAO.assignRetriever(ghostnetId, retriever);
    }
    
    public List<Ghostnet> getGhostnetsByUser(Benutzer user) {
    	return ghostnetDAO.getGhostnetsByUser(user);
    }
    
    // Methode zum Aktualisieren mehrerer Ghostnets in der Datenbank
    public void updateGhostnets(List<Ghostnet> ghostnetsZugeordnet) {
    	ghostnetDAO.updateGhostnets(ghostnetsZugeordnet);
    }
    
 
    public void markAsVerschollen(Long ghostnetId, Benutzer currentUser) {
    	ghostnetDAO.markAsVerschollen(ghostnetId, currentUser);
    }
    	
    // Status auf „GEBORGEN“ setzt und das Geisternetz dem aktuellen Benutzer zuordnet.
    public void updateStatusToGeborgen(Long ghostnetId, Benutzer currentUser) {
    	ghostnetDAO.updateStatusToGeborgen(ghostnetId, currentUser);
    }


    
   }
