package com.ahmad.controller;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;

import com.ahmad.model.Benutzer;
import com.ahmad.service.BenutzerService;

/**
 * 
 * @author Ahmad Alrefai
 */

@Named
@SessionScoped
public class UserSession implements Serializable {

	private static final long serialVersionUID = 1L;

	private Benutzer currentUser; // Angemeldeter Benutzer
	
	@Inject
	private transient BenutzerService benutzerService;

	public Benutzer getCurrentUser() {
		 String currentangemeldeteEmail = getCurrentUserEmail(); // Diese Methode muss implementiert werden
         if (currentangemeldeteEmail != null) {
         	
        	 currentUser = benutzerService.getBenutzerByEmail(currentangemeldeteEmail);
         }
		return currentUser;
	}

	public void setCurrentUser(Benutzer currentUser) {
		this.currentUser = currentUser;
	}

	// Methode zur Überprüfung, ob ein Benutzer angemeldet ist
	public boolean isLoggedIn() {
		return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user_email") != null;
	}
	
	 public String logout() {
	        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
	        return "login?faces-redirect=true"; // Zurück zur Login-Seite nach der Abmeldung
	 }

	/*
	 * Getter für Benutzerdaten E-Mail des angemeldeten Benutzers aus der Session
	 * zurückgibt.
	 */
	public String getCurrentUserEmail() {
		return (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user_email");
	}
}
