package com.ahmad.validator;

import java.util.ResourceBundle;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.FacesValidator;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;

/**
 * 
 * @author Ahmad Alrefai
 */

@FacesValidator("passwordValidator")
public class PasswordValidator implements Validator<Object> {
    
    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if (value == null || value.toString().trim().isEmpty()) {
            return; // Let required="true" handle empty values
        }
        
        String password = value.toString();
        
        // Mindestens 8 Zeichen
        if (password.length() < 8) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                ResourceBundle.getBundle("messages", context.getViewRoot().getLocale())
                    .getString("custom.error.password"), null); // Fehlermeldungen sind in der Messages.properties gespeichert
            throw new ValidatorException(msg);
        }
        
       
        // Mindestens ein Großbuchstabe
        if (!password.matches(".*[A-Z].*")) {
//            throw new ValidatorException(new FacesMessage(
//                FacesMessage.SEVERITY_ERROR, 
//                "Das Passwort muss mindestens einen Großbuchstaben enthalten.", null));
            
            throw new ValidatorException(new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("messages", context.getViewRoot().getLocale())
                    .getString("custom.error.password.großbuchstaben"), null));
                    
        }
        
        // Mindestens eine Zahl
        if (!password.matches(".*\\d.*")) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                ResourceBundle.getBundle("messages", context.getViewRoot().getLocale())
                    .getString("custom.error.password.zahl"), null));
        }
        
        // Mindestens ein Sonderzeichen
        if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                ResourceBundle.getBundle("messages", context.getViewRoot().getLocale())
                    .getString("custom.error.password.sonderzeichen"), null));
        }
    }
}