package com.mapchat.chat;

/*
 * Created by Sean Arcayan on 2016.04.12  * 
 * Copyright © 2016 Sean Arcayan. All rights reserved. * 
 */

import com.mapchat.entitypackage.Groups;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import com.mapchat.entitypackage.Message;
import com.mapchat.managers.Constants;
import com.mapchat.managers.GroupManager;
import com.mapchat.managers.MessageBean;
import com.mapchat.sessionbeanpackage.MessageFacade;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.el.ELContext;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ApplicationScoped;
import javax.faces.context.FacesContext;
 
/**
 * The Message Manager is application scoped because it serves as a central 
 * point of communication between all the users of the application. Different
 * sessions will send their message by using this class 
 * 
 * @author Sean Arcayan
 */
@ManagedBean
@ApplicationScoped
public class MessageManager implements MessageManagerLocal {
 
    @EJB
    private MessageFacade msgFacade; // The facade class for messages
    
    private Map<Integer, Collection> groupMessageMap; // Chatroom data structure of <group id, list of messages>
    
    @PostConstruct
    /**
     * Call this method on startup
     */
    public void init() {
        
        // A synchronized map is used since the same data is shared between different 
        // users in the same group
        groupMessageMap = 
            Collections.synchronizedMap(new HashMap<Integer, Collection>());
    }
    
    /**
     * Retrieve the list of messages in a group
     * @param group The group whose messages to retrieve
     * @return the list of messages
     */
    public List<Message> getMessagesByChatroom(Groups group) {
        return (List<Message>)groupMessageMap.get(group.getId());
    } 
    
    @Override
    /**
     * Determine the current group that the user is in and send the message to it.
     * This method will also save the message in the database.
     * 
     * @param msg The message to send
     */
    public void sendMessageToCurrentGroup(Message msg) {
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        MessageBean messageBean 
            = (MessageBean) FacesContext.getCurrentInstance().getApplication()
            .getELResolver().getValue(elContext, null, "messageBean");
        
        List<Message> cfq = (List<Message>)groupMessageMap.get(messageBean.getCurrentGroup().getId());
        
        // if we reach capacity in the list, the oldest message will be removed
        // from the list and the database
        if (cfq.size() > Constants.MAX_MESSAGES) {
            msgFacade.remove(cfq.remove(cfq.size() - 1));
        }
        
        // send the current message to the database 
        msgFacade.create(msg);
        
        // send the message to the current chatroom
        // add to the front of the list, remove from the end
        cfq.add(0, msg);
    }

    @Override
    /**
     * Get the available  ----- probably going to be deleted
     */
    public List<String> getAvailableChatrooms() {
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        GroupManager groupManager 
            = (GroupManager) FacesContext.getCurrentInstance().getApplication()
            .getELResolver().getValue(elContext, null, "groupManager");
        Set<Groups> grps = groupManager.getAllGroups();
        List<String> strings = grps.stream()
            .map(object -> (object != null ? object.toString() : null))
            .collect(Collectors.toList());
        
        return strings;
    }
    
    @Override
    /**
     * Get the messages in the current group
     * 
     * @return The list of messages in the user's current group
     */
    public List<Message> getMessagesInCurrentGroup() { 
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        MessageBean messageBean 
            = (MessageBean) FacesContext.getCurrentInstance().getApplication()
            .getELResolver().getValue(elContext, null, "messageBean");
        return (List<Message>)groupMessageMap.get(messageBean.getCurrentGroup().getId());
    }
    
    /**
     * Retrieve the map between group id and a collection of messages
     * @return the map
     */
    public Map<Integer, Collection> getGroupMessageMap() {
        return groupMessageMap;
    }

    /**
     * Set the map between group id and a collection of messages
     * @param groupMessageMap The map
     */
    public void setGroupMessageMap(Map<Integer, Collection> groupMessageMap) {
        this.groupMessageMap = groupMessageMap;
    }
    
} // end class