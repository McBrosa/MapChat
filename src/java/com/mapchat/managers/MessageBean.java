package com.mapchat.managers;

/*
 * Created by Sean Arcayan on 2016.04.12  * 
 * Copyright © 2016 Sean Arcayan. All rights reserved. * 
 */
import com.google.gson.Gson;
import com.mapchat.entitypackage.Groups;
import com.mapchat.entitypackage.Message;
import com.mapchat.sessionbeanpackage.MessageFacade;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;
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
    private MessageFacade msgFacade;
    
    private Date lastUpdate;
    private String messageUser;
    private String messageInput;
    private String[] availableChatrooms;
    private String[] activeMessages;

    @ManagedProperty(value="#{profileViewManager}")
    private ProfileViewManager profileViewManager;
    
    @ManagedProperty(value="#{groupManager}")
    private GroupManager groupManager;

    public GroupManager getGroupManager() {
        return groupManager;
    }

    public void setGroupManager(GroupManager groupManager) {
        this.groupManager = groupManager;
    }

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

        if (groupManager.getCurrentGroup() != null) {
                    System.out.println("curgroup: " + groupManager.getCurrentGroup().getGroupName());
            Message[] msgs = groupManager.getMessagesByChatroom(groupManager.getCurrentGroup()).toArray(new Message[0]);
            String[] ret = Arrays.stream(msgs).map(Object::toString).toArray(String[]::new);
            for (String s : ret) {
                System.out.println("msg: " + s);
            }
            return ret;
        }
        return null;
    }

    public void setActiveMessages(String[] activeMessages) {
        this.activeMessages = activeMessages;
    }

    public String[] getAvailableChatrooms() {
        availableChatrooms = groupManager.getAvailableChatrooms().toArray(new String[0]);
        return availableChatrooms;
    }

    public void setAvailableChatrooms(String[] availableChatrooms) {
        this.availableChatrooms = availableChatrooms;
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
        Groups curgrp = groupManager.getCurrentGroup();
        if (curgrp == null) {
            return;
        }
        
        // create messages and set properties
        Message msg = new Message();
        msg.setMessage(messageInput);
        msg.setUserId(profileViewManager.getLoggedInUser()); // automatically translates to id in the db
        msg.setTime(new Date());
        msg.setGroupId(curgrp); 
        
        // send the message to the current chatroom
        groupManager.getMessagesByChatroom(curgrp).add(msg);
        
        // send the message to the database 
        msgFacade.create(msg);
        
        // reset the input box
        messageInput = "";
    }
}