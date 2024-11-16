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

@FacesValidator("emailValidator")
public class EmailValidator implements Validator<Object> {
    
    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        String email = value.toString();
        
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                ResourceBundle.getBundle("messages", context.getViewRoot().getLocale())
                    .getString("custom.error.email"), null); // Fehlermeldungen sind in der Messages.properties gespeichert
            throw new ValidatorException(msg);
        }
    }
}