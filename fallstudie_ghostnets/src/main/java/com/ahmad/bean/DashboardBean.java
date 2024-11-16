package com.ahmad.bean;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import com.ahmad.controller.UserSession;
import com.ahmad.model.Benutzer;
import com.ahmad.model.Ghostnet;
import com.ahmad.service.GhostnetService;
import com.ahmad.util.GhostnetStatus;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

/**
 * 
 * @author Ahmad Alrefai
 */

@Named
@SessionScoped
public class DashboardBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Ghostnet> ghostnetsZugeordnet;
    private List<Ghostnet> ghostnets;
    @Inject
    private transient GhostnetService ghostnetService; // Service, der Ghostnets verwaltet

    @Inject
    private UserSession userSession; // Benutzersitzung

    @PostConstruct
    public void init() {
        loadAllGhostnets();
        loadBenutzerGhostnets();
    }
    
    // Ghostnet zugeordnet fuer bestimmte Benutzer
    public void loadBenutzerGhostnets() {
        // Alle Ghostnets für den eingeloggten Benutzer abrufen
    	Benutzer benutzer = userSession.getCurrentUser();
    	if(benutzer != null) {
    		
    		ghostnetsZugeordnet = ghostnetService.getGhostnetsByUser(benutzer);
    	}
    }
    
    public void loadAllGhostnets() {
        
        ghostnets = ghostnetService.getAllGhostnets();
    }
    
    // für edit abel Tabelle
    public void saveChanges() {
    	FacesContext context = FacesContext.getCurrentInstance();
        try {
            ghostnetService.updateGhostnets(ghostnetsZugeordnet);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                ResourceBundle.getBundle("messages", context.getViewRoot().getLocale())
                    .getString("custom.elfolgreich.speichern"), null));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                ResourceBundle.getBundle("messages", context.getViewRoot().getLocale())
                    .getString("custom.error.speichern.scheitert"), null));
        }
    }
    
    /**
     * Hier wird der Button nur angezeigt, wenn der Benutzer eingeloggt ist (Bedingung #{dashboardBean.userSession.loggedIn}). 
     * Durch das Drücken des Buttons wird die markAsVerschollen-Methode aufgerufen, die die Berechtigungen überprüft und, 
     * wenn der Benutzer authentifiziert ist, den Status des Geisternetzes auf „verschollen“ setzt.
     * 
     * 
     * @param ghostnetId
     */
    
    public void markAsVerschollen(Long ghostnetId) {
        Benutzer currentUser = userSession.getCurrentUser();
        try {
            ghostnetService.markAsVerschollen(ghostnetId, currentUser);
            FacesMessage msg = new FacesMessage("Geisternetz erfolgreich als verschollen gemeldet.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (Exception e) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fehler", "Das Geisternetz konnte nicht als verschollen gemeldet werden.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            e.printStackTrace();
        }
    }
    
    public void markAsGeborgen(Long ghostnetId) {
        Benutzer currentUser = userSession.getCurrentUser();
        ghostnetService.updateStatusToGeborgen(ghostnetId, currentUser);
        
        // Ghostnets-Liste aktualisieren, damit der neue Status angezeigt wird
        loadBenutzerGhostnets();
    }

    public List<Ghostnet> getGhostnets() {
        return ghostnets;
    }
    
    public List<Ghostnet> getGhostnetsZugeordnet() {
        return ghostnetsZugeordnet;
    }
    
 // Methode zur Rückgabe aller Enum-Werte für die Anzeige im Menü
    public List<GhostnetStatus> getStatusValues() {
        return Arrays.asList(GhostnetStatus.values());
    }
    
    public boolean getIsLoggedIn() {
    	return userSession.isLoggedIn();
    }
}
