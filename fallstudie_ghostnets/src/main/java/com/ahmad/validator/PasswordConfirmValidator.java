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

@FacesValidator("passwordConfirmValidator")
public class PasswordConfirmValidator implements Validator<Object> {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        // Wert des Passwort-Bestätigungseingabefelds
        String confirmPassword = value.toString();

        // Ursprüngliches Passwort 
        String password = (String) component.getAttributes().get("originalPassword");

        if (password == null || !password.equals(confirmPassword)) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                ResourceBundle.getBundle("messages", context.getViewRoot().getLocale())
                    .getString("custom.error.passwort.confirm.ungueltigkeite"), null);
            throw new ValidatorException(msg);
        }
    }
}
