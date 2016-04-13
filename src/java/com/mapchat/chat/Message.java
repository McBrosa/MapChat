package com.mapchat.chat;

/*
 * Created by Sean Arcayan on 2016.04.12  * 
 * Copyright © 2016 Sean Arcayan. All rights reserved. * 
 */
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
 
/**
 * Represents message
 * @author Danon
 */
public class Message implements Serializable {
    private Date dateSent; // this should probably be changed to LocalDateTime or LocalDate to account for timezones
    private String user;
    private String message;
 
    public Date getDateSent() {
        return dateSent;
    }
 
    public void setDateSent(Date dateSent) {
        this.dateSent = dateSent;
    }
 
    public String getMessage() {
        return message;
    }
    
    public String resetMessage() {
        message = "";
        return message;
    }
 
    public void setMessage(String message) {
        this.message = message;
    }
 
    public String getUser() {
        return user;
    }
 
    public void setUser(String user) {
        this.user = user;
    }
    
    @Override
    public String toString() {
            
        StringBuilder builder = new StringBuilder("[");
        builder.append(dateSent);
        builder.append("] ");
        builder.append(user);
        builder.append(" : ");
        builder.append(message);
        return builder.toString();
        
    }
}