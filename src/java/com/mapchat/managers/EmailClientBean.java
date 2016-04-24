/*
 * Created by Corey McQuay on 2016.04.21  * 
 * Copyright © 2016 Corey McQuay. All rights reserved. * 
 */
package com.mapchat.managers;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.mail.MessagingException;
import com.mapchat.mail.MailService;

/**
 * A bean class that illustrates sending an email message
 * @author Corey McQuay
 */

//Attributes of the bean
@Named
@RequestScoped
public class EmailClientBean {
    
    //Global Variables that will be received from the xhtml 
    private final String RECIPIENT = "mapchatservice@gmail.com"; //Only sending the comments to the our email
    private String subject; //Subject reference
    private String message; //
    private String statusMessage = ""; //Confirmation or error message
    
    /**
     * Getter method for the message global
     * 
     * @return The message string from the user
     */
    public String getMessage() {
        return message;
    }

    /**
     * Setter for the message global variable
     * @param message The message from the user
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Getter for the subject global
     * @return The value in the subject global variable
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Sets the subject global variable to the whatever was passed in from the user.
     * @param subject 
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * getter method for the status variable 
     * @return The status messaged stored in this variable
     */
    public String getStatusMessage() {
        return statusMessage;
    }
    
    /**
     * The method takes all the global variables and attempts to send it with the MailService class
     * If an error occurs an error message will be populated
     * @return The xhtml reference itself and show a status message if necessary
     */
    public String send() {
        statusMessage = "Message Sent"; //Successful message
        try {
            MailService.sendMessage(RECIPIENT, subject, message); 
        }
        catch(MessagingException ex) {
            statusMessage = ex.getMessage(); //Error Message 
        }
        return "ReportBug";  // redisplay page with status message
    }  
    
}