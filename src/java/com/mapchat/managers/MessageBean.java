package com.mapchat.managers;

/*
 * Created by Sean Arcayan on 2016.04.12  * 
 * Copyright © 2016 Sean Arcayan. All rights reserved. * 
 */
import com.mapchat.chat.Message;
import com.mapchat.chat.MessageManagerLocal;
import java.io.Serializable;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.el.ELContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import org.primefaces.context.RequestContext;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.json.JSONException;
 
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
 
    public void sendMessage(ActionEvent evt) {
        if (selectedChatroom == null) {
            return;
        }
        Message msg = new Message();
        msg.setMessage(messageInput);
        msg.setUser(profileViewManager.getLoggedInUser().getFirstName());
        mm.sendMessage(msg, selectedChatroom);
        messageInput = "";
    }
    
    public String getCurrentUserFromAccountManager() {
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        AccountManager accountManager = (AccountManager) FacesContext.getCurrentInstance().getApplication()
            .getELResolver().getValue(elContext, null, "accountManager");
        return accountManager.getFirstName();
    }
 
    public void firstUnreadMessage(ActionEvent evt) {
       if (selectedChatroom == null)
       {
           return;
       }
       RequestContext ctx = RequestContext.getCurrentInstance();
       Message m = mm.getFirstAfter(lastUpdate, selectedChatroom);
 
       ctx.addCallbackParam("ok", m!=null);
       if(m==null)
           return;
 
       lastUpdate = m.getDateSent();
 
       ctx.addCallbackParam("user", m.getUser());
       ctx.addCallbackParam("dateSent", m.getDateSent().toString());
       ctx.addCallbackParam("text", m.getMessage());
 
    }
    
    public void getChatroomMessages(ValueChangeEvent evt) {
       if (selectedChatroom == null)
       {
           return;
       }
       FacesContext ctx = FacesContext.getCurrentInstance();
 
    }
 
}