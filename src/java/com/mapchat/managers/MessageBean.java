package com.mapchat.managers;

/*
 * Created by Sean Arcayan on 2016.04.12  * 
 * Copyright © 2016 Sean Arcayan. All rights reserved. * 
 */
import com.google.gson.Gson;
import com.mapchat.entitypackage.Message;
import com.mapchat.chat.MessageManagerLocal;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import org.primefaces.context.RequestContext;
import javax.faces.bean.ManagedProperty;
 
/**
 *
 * @author Sean Arcayan
 */
@ManagedBean
@ViewScoped
public class MessageBean implements Serializable {
 
    @EJB
    MessageManagerLocal mm;
    
    private Date lastUpdate;
    private String messageUser;
    private String messageInput;
    private String selectedChatroom;
    private String[] availableChatrooms;
    private String[] activeMessages;

    @ManagedProperty(value="#{profileViewManager}")
    private ProfileViewManager profileViewManager;

    public ProfileViewManager getProfileViewManager() {
        return profileViewManager;
    }

    public void setProfileViewManager(ProfileViewManager profileViewManager) {
        this.profileViewManager = profileViewManager;
    }
    
    /**
     * Creates a new instance of MessageBean
     */
    public MessageBean() {
        lastUpdate = new Date(0);
    }
    
    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }
    
    public String getMessageInput() {
        return messageInput;
    }

    public void setMessageInput(String messageInput) {
        this.messageInput = messageInput;
    }
   
    
    public String[] getActiveMessages() {
        
        if (selectedChatroom != null) {
            Message[] msgs = mm.getMessagesByChatroom(selectedChatroom).toArray(new Message[0]);
            String[] ret = Arrays.stream(msgs).map(Object::toString).toArray(String[]::new);
            return ret;
        }
        return null;
    }

    public void setActiveMessages(String[] activeMessages) {
        this.activeMessages = activeMessages;
    }

    public String[] getAvailableChatrooms() {
        availableChatrooms = mm.getAvailableChatrooms().toArray(new String[0]);
        return availableChatrooms;
    }

    public void setAvailableChatrooms(String[] availableChatrooms) {
        this.availableChatrooms = availableChatrooms;
    }

    public String getSelectedChatroom() {
        return selectedChatroom;
    }

    public void setSelectedChatroom(String selectedChatroom) {
        this.selectedChatroom = selectedChatroom;
    }

    
    public Date getLastUpdate() {
        return lastUpdate;
    }
 
    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
 
    /**
     * Send the message to a chat room
     * @param evt 
     */
    public void sendMessage(ActionEvent evt) {
        if (selectedChatroom == null) {
            return;
        }
        
        // create messages and set properties
        Message msg = new Message();
        msg.setMessage(messageInput);
        msg.setUserId(profileViewManager.getLoggedInUser()); // TODO replace with actual ID
        
        // send the message to the current chatroom
        mm.sendMessage(msg, selectedChatroom);
        
        // reset the input box
        messageInput = "";
    }
 
    /**
     * Retrieve the messages from the current chatroom and convert them to JSON
     * format. After that, set it as the context parameter to send to the frontend
     */
    public void getChatroomMessages() {
       if (selectedChatroom == null)
       {
           return;
       }
       RequestContext ctx = RequestContext.getCurrentInstance();
       Gson gson = new Gson();
       ctx.addCallbackParam("msgjson", gson.toJson(getActiveMessages()));
       System.out.println(gson.toJson(getActiveMessages()));
    }
}