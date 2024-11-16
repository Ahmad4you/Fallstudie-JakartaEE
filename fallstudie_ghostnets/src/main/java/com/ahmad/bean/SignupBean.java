package com.ahmad.bean;
import com.ahmad.model.Benutzer;
import com.ahmad.service.BenutzerService;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
/**
 * 
 * @author Ahmad Alrefai
 */

@Named
@RequestScoped
public class SignupBean {
	
	@Inject
	private BenutzerService benutzerService;
	private Benutzer benutzer;
    
    private String passwortConfirm;
    
    @PostConstruct
    public void init() {
    	setBenutzer(new Benutzer());
    }
    
    public String submit() {
    	 FacesContext context = FacesContext.getCurrentInstance();
        // Prüfen, ob die Passwörter übereinstimmen
        if (!benutzer.getPasswordHash().equals(passwortConfirm)) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                "Passwortfehler", "Die Passwörter stimmen nicht überein.");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return null; // Bleibt auf der aktuellen Seite
        }

        // Benutzer speichern
        benutzerService.saveBenutzer(benutzer);

        context.getExternalContext().getSessionMap().put("user_email", benutzer.getEmail());
        // Erfolgsmeldung hinzufügen (optional)
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Erfolg", "Benutzer wurde erfolgreich registriert."));

        return "fishingnets?faces-redirect=true"; // Zur Startseite weiterleiten
    }


    public String getPasswortConfirm() {
        return passwortConfirm;
    }

    public void setPasswortConfirm(String passwortConfirm) {
        this.passwortConfirm = passwortConfirm;
    }

	public Benutzer getBenutzer() {
		return benutzer;
	}

	public void setBenutzer(Benutzer benutzer) {
		this.benutzer = benutzer;
	}
}
