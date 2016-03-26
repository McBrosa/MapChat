/*
 * Created by Corey McQuay on 2016.03.22  * 
 * Copyright © 2016 Corey McQuay. All rights reserved. 
 * This class ensures that the phone number will be validated from what the user
 * has passed in for the phhone number field.
 */
package com.mapchat.validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("phoneNumberValidator")
public class PhoneNumberValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        
        // Typecast the phone number "value" entered by the user to String.
        String phoneNumber = (String) value;

        //Empty phone number
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            // Do not take any action. 
            // The required="true" in the XHTML file will catch this and produce an error message.
            return;
        }
       /**
        * regex stands for REGular EXpression, which defines a search pattern for strings.
        * To learn about how to use regex, see https://docs.oracle.com/javase/tutorial/essential/regex/
        **/
        
        // Validate if the email address entered by the user is in the right format.
        String regex = "^\\(?([0-9]{3}\\)?[-.\\s]?([0-9]{3})[-.\\s]?([0-9]{4})$"; //US Phone number
        String regexInt = "^\\+(?:[0-9]?){6,14}[0-9]$"; //International Phone number
        
        if (!phoneNumber.matches(regex) || !phoneNumber.matches(regexInt)) {
            throw new ValidatorException(new FacesMessage("Please Enter a Valid Phone Number!"));
        }        
    } 
}
