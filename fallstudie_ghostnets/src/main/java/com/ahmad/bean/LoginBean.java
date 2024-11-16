package com.ahmad.bean;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.net.URL;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;
import com.ahmad.model.Benutzer;
import com.ahmad.service.BenutzerService;

/***
 * 
 * @author Ahmad Alrefai
 */

@Named
@RequestScoped
public class LoginBean implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Benutzer benutzer =null;
    List<Benutzer> benutzers;
    private String _email;

	private String password;
    @Inject
    private BenutzerService benutzerService;
    
    @Inject
    private DashboardBean dashboardBean;
    
    @PostConstruct
	public void init() {
    	setBenutzer(new Benutzer());
		// Überprüfen den Classpath 
		URL url = this.getClass().getClassLoader().getResource("messages.properties");
		System.out.println("Resource URL: " + url);
	}

    public String submit() {
        FacesContext context = FacesContext.getCurrentInstance();

        if (_email == null || password == null || 
            _email.trim().isEmpty() || password.trim().isEmpty()) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                "Fehlerhafte Eingabe", "Bitte Email und Passwort eingeben.");
            context.addMessage(null, message);
            return null;
        }

        benutzers = benutzerService.getAllBenutzer();
        if (benutzers == null) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                "Fehler", "Benutzerliste konnte nicht geladen werden.");
            context.addMessage(null, message);
            return null;
        }

        boolean valid = benutzers.stream()
                                 .anyMatch(b -> _email.equals(b.getEmail()) && 
                                                BCrypt.checkpw(password, b.getPasswordHash()));

        if (valid) {
            context.getExternalContext().getSessionMap().put("user_email", _email);
            
            // load zugeordnete Ghostnetze
            dashboardBean.loadBenutzerGhostnets();
            return "fishingnets?faces-redirect=true";
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                "Ungültige Anmeldedaten", "Email oder Passwort ist falsch.");
            context.addMessage(null, message);
            return null;
        }
    }

	public Benutzer getBenutzer() {
		return benutzer;
	}

	public void setBenutzer(Benutzer benutzer) {
		this.benutzer = benutzer;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
    
	public String get_email() {
		return _email;
	}

	public void set_email(String _email) {
		this._email = _email;
	}
}