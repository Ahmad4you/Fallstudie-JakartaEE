package com.ahmad.validator;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.FacesValidator;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;
import jakarta.inject.Inject;

import java.util.List;
import java.util.ResourceBundle;

import org.mindrot.jbcrypt.BCrypt;

import com.ahmad.model.Benutzer;
import com.ahmad.service.BenutzerService;

/**
 * 
 * @author Ahmad Alrefai
 */

@FacesValidator("loginValidator")
public class LoginValidator implements Validator<Object> {

    @Inject
    private BenutzerService benutzerService;

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        String email = (String) component.getAttributes().get("email");  // Email aus Attribut lesen
        String password = (value != null) ? value.toString() : null;

        // Fehlermeldung, falls Email oder Passwort fehlen
        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
        	 throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                     ResourceBundle.getBundle("messages", context.getViewRoot().getLocale())
                         .getString("custom.error.login.ungueltigkeite"), null));
        }

        List<Benutzer> benutzers = benutzerService.getAllBenutzer();
        boolean valid = benutzers.stream()
                                 .anyMatch(b -> email.equals(b.getEmail()) && BCrypt.checkpw(password, b.getPasswordHash()));

        if (!valid) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                ResourceBundle.getBundle("messages", context.getViewRoot().getLocale())
                    .getString("custom.error.login.ungueltigkeite"), null));
        }
    }
}
