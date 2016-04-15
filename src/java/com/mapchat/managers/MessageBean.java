package com.mapchat.managers;

/*
 * Created by Sean Arcayan on 2016.04.12  * 
 * Copyright © 2016 Sean Arcayan. All rights reserved. * 
 */
import com.google.gson.Gson;
import com.mapchat.chat.Message;
import com.mapchat.chat.MessageManagerLocal;
import java.io.Serializable;
import java.util.Date;
import javax.ejb.EJB;
import javax.el.ELContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import org.primefaces.context.RequestContext;
import javax.faces.bean.ManagedProperty;
import javax.faces.component.ContextCallback;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.component.html.HtmlOutputLabel;
import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.context.FacesContext;
 
/**
 *
 * @author Sean Arcayan
 */
@ManagedBean
@ViewScoped
public class MessageBean implements Serializable {
 
    @EJB
    MessageManagerLocal mm;
    
    //private final List messages;
    private Date lastUpdate;
    private String messageUser;
    private String messageInput;
    private String selectedChatroom;
    private String[] availableChatrooms;
    private Message[] activeMessages;
    private UIComponent found;

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
        //messages = Collections.synchronizedList(new LinkedList());
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
   
    
    public Message[] getActiveMessages() {
        if (selectedChatroom != null) {
            activeMessages = mm.getMessagesByChatroom(selectedChatroom).toArray(new Message[0]);
        }
        return activeMessages;
    }

    public void setActiveMessages(Message[] activeMessages) {
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
     * Send the message to a chatroom
     * @param evt 
     */
    public void sendMessage(ActionEvent evt) {
        if (selectedChatroom == null) {
            return;
        }
        
        // create messages and set properties
        Message msg = new Message();
        msg.setMessage(messageInput);
        msg.setUser(profileViewManager.getLoggedInUser().getFirstName());
        
        // send the message to the current chatroom
        mm.sendMessage(msg, selectedChatroom);
        
        // reset the input box
        messageInput = "";
    }
 
    /**
     * Get the first unread message from the chatroom and send it to the front end
     * @param evt 
     */
    public void firstUnreadMessage(ActionEvent evt) {
       if (selectedChatroom == null)
       {
           return;
       }
       RequestContext ctx = RequestContext.getCurrentInstance();
       Message m = mm.getFirstAfter(lastUpdate, selectedChatroom);
 
       // create a parameter "ok" and set it to m
       ctx.addCallbackParam("ok", m!=null);
       if(m==null)
           return;
 
       lastUpdate = m.getDateSent();
 
       // set call back parameters
       // in the javascript function, these can be retrieved through "args.<param>"
       ctx.addCallbackParam("user", m.getUser());
       ctx.addCallbackParam("dateSent", m.getDateSent().toString());
       ctx.addCallbackParam("text", m.getMessage());
 
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