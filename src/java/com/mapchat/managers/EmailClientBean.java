/*
 * Created by Corey McQuay on 2016.04.21  * 
 * Copyright © 2016 Corey McQuay. All rights reserved. * 
 */
package com.mapchat.managers;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.mail.MessagingException;
import com.mapchat.mail.MailService;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;


/**
 * A bean class that illustrates sending an email body
 *
 * @author Corey McQuay
 */
//Attributes of the bean
@Named
@RequestScoped //At the time when a request to the mail server is needed
public class EmailClientBean {

    //Global Variables that will be received from the xhtml 
    private final String RECIPIENT = "mapchatservice@gmail.com"; //Only sending the comments to the our email
    private String subject; //Subject reference
    private String body; //
    private String statusMessage = ""; //Confirmation or error body

    /**
     * Getter method for the body global
     *
     * @return The body string from the user
     */
    public String getBody() {
        return body;
    }

    /**
     * Setter for the body global variable
     *
     * @param body The body from the user
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * Getter for the subject global
     *
     * @return The value in the subject global variable
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Sets the subject global variable to the whatever was passed in from the
     * user.
     *
     * @param subject
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * getter method for the status variable
     *
     * @return The status messaged stored in this variable
     */
    public String getStatusMessage() {
        return statusMessage;
    }

    /**
     * The method takes all the global variables and attempts to send it with
     * the MailService class If an error occurs an error body will be populated
     *
     */
    public void send() {
        statusMessage = "Message Sent"; //Successful body

        //Checks to see if the fields in the form from the front end are empty and gives user meaningful mailMessage
        //Both are empty
        if (subject.equals("") && body.equals("")) {
            addMessage("Subject and Body Field Required", "The message did not send because the subject and body field was not filled out.");
            return;
        }
        //Subject Empty
        if (subject.equals("")) {
            addMessage("Subject Line Required", "The message did not send because the subject field was not filled out.");
            return;
        }
        //Body Empty
        if (body.equals("")) {
            addMessage("Body of Email Required", "The message did not send because the body field was not filled out.");
            return;
        }
        try {
            MailService.sendMessage(RECIPIENT, subject, body);
        } catch (MessagingException ex) {
            statusMessage = ex.getMessage(); //Error Message
            addMessage("Message Did Not Send!", statusMessage);
            return;
        }

        //Resets the fields
        subject = "";
        body = "";
        addMessage("Message Sent!", "The Message has beeen sent to our team.");
    }

    /**
     * This method works with the growl tag in the footer template that notifies the user 
     * that their email sent or not. 
     * 
     * 
     * @param summary Notification Header 
     * @param detail  Notification Detail
     */
    public void addMessage(String summary, String detail) {
        //id of the tag from xhtml file
        FacesMessage mailMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, mailMessage); //Sends the message to the front end.
    }
    
    /**
     * Resests the text boxes of the field to empty so that the form is empty when the user clicks on the link again.
     */
    public void clearFields()
    {
        subject = "";
        body = "";
    }
}
