package com.mapchat.managers;

/*
 * Created by Sean Arcayan on 2016.04.12  * 
 * Copyright © 2016 Sean Arcayan. All rights reserved. * 
 */
import com.mapchat.entitypackage.File1;
import com.mapchat.entitypackage.Groups;
import com.mapchat.entitypackage.Message;
import com.mapchat.sessionbeanpackage.File1Facade;
import com.mapchat.sessionbeanpackage.MessageFacade;
import com.mapchat.sessionbeanpackage.UserFacade;
import java.io.File;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.faces.bean.ManagedProperty;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
 
/**
 *
 * @author Sean Arcayan
 */
@ManagedBean
@ViewScoped
public class MessageBean implements Serializable {
 
    @EJB
    private MessageFacade msgFacade;
    
    @EJB
    private File1Facade file1Facade;
    
    private Date lastUpdate;
    private String messageUser;
    private String messageInput;
    private String[] availableChatrooms;
    private String[] activeMessages;

    @ManagedProperty(value="#{profileViewManager}")
    private ProfileViewManager profileViewManager;
    
    @ManagedProperty(value="#{groupManager}")
    private GroupManager groupManager;
    
    @ManagedProperty(value="#{fileManager}")
    private FileManager fileManager;

    public FileManager getFileManager() {
        return fileManager;
    }

    public void setFileManager(FileManager fileManager) {
        this.fileManager = fileManager;
    }

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
            Message[] msgs = groupManager.getMessagesByChatroom(groupManager.getCurrentGroup()).toArray(new Message[0]);
            String[] ret = Arrays.stream(msgs).map(Object::toString).toArray(String[]::new);
            return ret;
        }
        return null;
    }

    public void setActiveMessages(String[] activeMessages) {
        this.activeMessages = activeMessages;
    }

    public String[] getAvailableChatrooms() {
        availableChatrooms = groupManager.getAllGroups().toArray(new String[0]);
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
        Message msg = generateMessage(messageInput);
        if (msg == null)
        {
            return;
        }
                
        List<Message> cfq = groupManager.getMessagesByChatroom(groupManager.getCurrentGroup());
        
        // if we reach capacity in the list, the oldest message will be removed
        // from the list and the database
        if (cfq.size() > Constants.MAX_MESSAGES) {
            msgFacade.remove(cfq.remove(cfq.size()-1));
        }
        
        // send the message to the current chatroom
        // add to the front of the list, remove from the end
        cfq.add(0, msg);
        
        // send the current message to the database 
        msgFacade.create(msg);
        
        // reset the input box
        messageInput = "";
    }
    
    
    /**
     * upload a file to the group chatroom
     * @param evt
     */
    public void uploadFile(FileUploadEvent evt) {
        if (groupManager.getCurrentGroup() == null )
        {
            return;
        }
        
        /*
         * upload the file
         */
        fileManager.uploadFileToGroup(evt.getFile(), groupManager.getCurrentGroup());
        
        /*
         * send a message to notify that a file was uploaded
         */
        Message msg = generateMessage("New File: " + evt.getFile().getFileName());
        if (msg == null) {
            return;
        }
        
        // send the message to the current chatroom
        List<Message> cfq = groupManager.getMessagesByChatroom(groupManager.getCurrentGroup());
        
        // if we reach capacity in the list, the oldest message will be removed
        // from the list and the database
        if (cfq.size() > Constants.MAX_MESSAGES) {
            msgFacade.remove(cfq.remove(cfq.size() - 1));
        }
        
        // add to the front of the list, remove from the end
        cfq.add(0, msg);
        
        // send the current message to the database 
        msgFacade.create(msg);
    }
    
    private Message generateMessage(String msgContent) {
        Groups curgrp = groupManager.getCurrentGroup();
        if (curgrp == null) {
            return null;
        }
        
        // create messages and set properties
        Message msg = new Message();
        msg.setMessage(msgContent);
        msg.setUserId(profileViewManager.getLoggedInUser()); // automatically translates to id in the db
        msg.setTime(new Date());
        msg.setGroupId(curgrp); 

        return msg;
    }
    
    /**
     * Retrieve all the files for the group
     * @return 
     */
    public String[] getFileNamesInGroup() {
        
        String directory = Constants.ROOT_DIRECTORY + groupManager.getCurrentGroup().getId();
        
        new File(directory).mkdirs();
        
        File folder = new File(directory);
        File[] listOfFiles = folder.listFiles();
        String[] listOfFilesRelative = new String[listOfFiles.length];
        for (int i = 0; i < listOfFiles.length; i++) {
            listOfFilesRelative[i] = listOfFiles[i].getName();
        }
        return listOfFilesRelative;
    }
}