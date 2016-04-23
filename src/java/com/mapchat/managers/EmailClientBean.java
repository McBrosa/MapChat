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
 * @author tcolburn
 */
@Named
@RequestScoped
public class EmailClientBean {

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getStatusMessage() {
        return statusMessage;
    }
    
    public String send() {
        statusMessage = "Message Sent";
        try {
            MailService.sendMessage(recipient, subject, message);
        }
        catch(MessagingException ex) {
            statusMessage = ex.getMessage();
        }
        return "ReportBug";  // redisplay page with status message
    }
    
    private String recipient = "mapchatservice@gmail.com";
    private String subject;
    private String message;
    private String statusMessage = "";
    
}