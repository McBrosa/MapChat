package com.mapchat.managers;

/*
 * Created by Sean Arcayan on 2016.04.12  * 
 * Copyright © 2016 Sean Arcayan. All rights reserved. * 
 */
import com.mapchat.chat.Message;
import com.mapchat.chat.MessageManagerLocal;
import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.ejb.EJB;
import javax.el.ELContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import org.primefaces.context.RequestContext;
import javax.faces.bean.ManagedProperty;
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
   
    public Date getLastUpdate() {
        return lastUpdate;
    }
 
    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
 
    public void sendMessage(ActionEvent evt) {
        Message msg = new Message();
        msg.setMessage(messageInput);
        msg.setUser(profileViewManager.getLoggedInUser().getFirstName());
        mm.sendMessage(msg);
        messageInput = "";
    }
    
    public String getCurrentUserFromAccountManager() {
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        AccountManager accountManager = (AccountManager) FacesContext.getCurrentInstance().getApplication()
            .getELResolver().getValue(elContext, null, "accountManager");
        return accountManager.getFirstName();
    }
 
    public void firstUnreadMessage(ActionEvent evt) {
       RequestContext ctx = RequestContext.getCurrentInstance();
 
       Message m = mm.getFirstAfter(lastUpdate);
 
       ctx.addCallbackParam("ok", m!=null);
       if(m==null)
           return;
 
       lastUpdate = m.getDateSent();
 
       ctx.addCallbackParam("user", m.getUser());
       ctx.addCallbackParam("dateSent", m.getDateSent().toString());
       ctx.addCallbackParam("text", m.getMessage());
 
    }
 
}