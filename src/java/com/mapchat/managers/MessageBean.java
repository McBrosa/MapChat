package com.mapchat.managers;

/*
 * Created by Sean Arcayan on 2016.04.12  * 
 * Copyright © 2016 Sean Arcayan. All rights reserved. * 
 */
import com.mapchat.chat.MessageManager;
import com.mapchat.chat.MessageManagerLocal;
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
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.faces.bean.ManagedProperty;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
 
/**
 * This is the session scoped Message class that handles sending messages and the
 * current group. This class will communicate with the application scoped 
 * MessageManager.java class. 
 *
 * @author Sean Arcayan
 */
@ManagedBean
@SessionScoped 
public class MessageBean implements Serializable {

    @EJB
    private MessageFacade msgFacade;
    
    @EJB
    private File1Facade file1Facade;
    
    private Date lastUpdate;
    private String messageUser; // TODO remove
    private String messageInput;
    private String[] availableChatrooms;
    private String[] activeMessages;
    private Groups currentGroup;

    @ManagedProperty(value="#{messageManager}")
    private MessageManager mm;
    
    @ManagedProperty(value="#{profileViewManager}")
    private ProfileViewManager profileViewManager;
    
    @ManagedProperty(value="#{groupManager}")
    private GroupManager groupManager;
    
    @ManagedProperty(value="#{fileManager}")
    private FileManager fileManager;

    /**
     * Creates a new instance of MessageBean
     */
    public MessageBean() {
        lastUpdate = new Date(0);
    }
    
    /**
     * Retrieve the message manager
     * @return 
     */
    public MessageManager getMm() {
        return mm;
    }
    
    /**
     * Set the message manager
     * @param mm 
     */
    public void setMm(MessageManager mm) {
        this.mm = mm;
    }
    
    /**
     * Retrieve the current group that the user is in
     * TODO -- remove if statement
     * @return currentGroup
     */
    public Groups getCurrentGroup() {
        if (currentGroup == null) return null;
        
        return currentGroup;
    }

    /**
     * Set the current group
     * @param currentGroup 
     */
    public void setCurrentGroup(Groups currentGroup) {
        this.currentGroup = currentGroup;
    }
    
    /**
     * Get the 
     * @return 
     */
    public String getMessageUser() {
        return messageUser;
    }
 
    // remove
    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }
    
    /**
     * Get the string from the message input box
     * @return messageInput
     */
    public String getMessageInput() {
        return messageInput;
    }

    /**
     * Set the string from the message input box
     * @param messageInput 
     */
    public void setMessageInput(String messageInput) {
        this.messageInput = messageInput;
    }
   
    /**
     * Get the file manager
     * @return fileManager
     */
    public FileManager getFileManager() {
        return fileManager;
    }

    /**
     * Set the file manager
     * @param fileManager 
     */
    public void setFileManager(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    /**
     * Get the group manager
     * @return groupManager
     */
    public GroupManager getGroupManager() {
        return groupManager;
    }

    /**
     * Set the group manager
     * @param groupManager 
     */
    public void setGroupManager(GroupManager groupManager) {
        this.groupManager = groupManager;
    }

    /**
     * Get the profile view manager
     * @return profileViewManager
     */
    public ProfileViewManager getProfileViewManager() {
        return profileViewManager;
    }

    /**
     * Set the profile view manager
     * @param profileViewManager 
     */
    public void setProfileViewManager(ProfileViewManager profileViewManager) {
        this.profileViewManager = profileViewManager;
    }
    
    /**
     * Retrieve the active messages. The active messages are the messages
     * that are in the group the user is currently in. 
     * @return the active messages in the group
     */
    public String[] getActiveMessages() {
        if (currentGroup != null) {
            Message[] msgs = mm.getMessagesInCurrentGroup().toArray(new Message[0]);
            String[] ret = Arrays.stream(msgs).map(Object::toString).toArray(String[]::new);
            return ret;
        }
        return null;
    }

    /**
     * Set the active messages
     * @param activeMessages 
     */
    public void setActiveMessages(String[] activeMessages) {
        this.activeMessages = activeMessages;
    }

    /**
     * Get the available chatrooms -- TODO remove
     * @return 
     */
    public String[] getAvailableChatrooms() {
        /*
        availableChatrooms = groupManager.getAllGroups().toArray(new String[0]);
        return availableChatrooms;
        */
        return mm.getAvailableChatrooms().toArray(new String[0]);
    }

    // remove
    public void setAvailableChatrooms(String[] availableChatrooms) {
        this.availableChatrooms = availableChatrooms;
    }
    
   // TODO -- remove
    public Date getLastUpdate() {
        return lastUpdate;
    }
 
    // TOOD -- remove
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
        // send the message to the current chatroom
        // add to the front of the list, remove from the end
        mm.sendMessageToCurrentGroup(msg);
        
        // reset the input box
        messageInput = "";
    }
    
    
    /**
     * upload a file to the group chatroom
     * @param evt
     */
    public void uploadFile(FileUploadEvent evt) {
        if (currentGroup == null )
        {
            return;
        }
        
        /*
         * upload the file
         */
        fileManager.uploadFileToGroup(evt.getFile(), currentGroup);
        
        /*
         * send a message to notify that a file was uploaded
         */
        Message msg = generateMessage("New File: " + evt.getFile().getFileName());
        if (msg == null) {
            return;
        }
        
        // send the message to the current chatroom
        List<Message> cfq = mm.getMessagesByChatroom(currentGroup);
        
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
    
    /**
     * Generate the message object
     * @param msgContent the content of the message
     * @return the message object
     */
    private Message generateMessage(String msgContent) {
        Groups curgrp = currentGroup;
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
        
        String directory = Constants.ROOT_DIRECTORY + currentGroup.getId();
        System.out.println("dir: " + directory);
        new File(directory).mkdirs();
        
        File folder = new File(directory);
        File[] listOfFiles = folder.listFiles();
        String[] listOfFilesRelative = new String[listOfFiles.length];
        System.out.println("numfiles: " + listOfFiles.length);
        for (int i = 0; i < listOfFiles.length; i++) {
            listOfFilesRelative[i] = listOfFiles[i].getName();
        }
        return listOfFilesRelative;
    }
    
}
