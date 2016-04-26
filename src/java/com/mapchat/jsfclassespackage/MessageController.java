/**
 * Created by MapChat Development Team
 * Edited by Sean Arcayan
 * Last Modified: 2016.03.22
 * Copyright © 2016 MapChat Development Team. All rights reserved.
 */
package com.mapchat.jsfclassespackage;

import com.mapchat.entitypackage.Message;
import com.mapchat.jsfclassespackage.util.JsfUtil;
import com.mapchat.jsfclassespackage.util.JsfUtil.PersistAction;
import com.mapchat.sessionbeanpackage.MessageFacade;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@Named("messageController")
@SessionScoped
/**
 * The controller for the Message class
 */
public class MessageController implements Serializable {

    @EJB
    private com.mapchat.sessionbeanpackage.MessageFacade ejbFacade; 
    private List<Message> items = null;
    private Message selected;

    /**
     * Constructor
     */
    public MessageController() {
    }

    /**
     * Retrieve the selected message
     * @return selected
     */
    public Message getSelected() {
        return selected;
    }

    /**
     * Set the selected message
     * @param selected 
     */
    public void setSelected(Message selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    /**
     * Retrieve the facade for the controller
     * @return ejbFacade
     */
    private MessageFacade getFacade() {
        return ejbFacade;
    }

    /**
     * Prepare the creation of a message
     * @return selected
     */
    public Message prepareCreate() {
        selected = new Message();
        initializeEmbeddableKey();
        return selected;
    }

    /**
     * Create a message
     */
    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("MessageCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    /**
     * Update a message
     */
    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("MessageUpdated"));
    }

    /**
     * Destroy a message
     */
    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("MessageDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    /**
     * Get a list of the messages
     * @return items the list of messages
     */
    public List<Message> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    /**
     * Persist the message
     * @param persistAction
     * @param successMessage 
     */
    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    /**
     * Get message by id
     * @param id the message id
     * @return the message
     */
    public Message getMessage(java.lang.Integer id) {
        return getFacade().find(id);
    }

    /**
     * Generated method. Currently retrieves all the messages.
     * @return all the messages
     */
    public List<Message> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    /**
     * Generated method. Currently retrieves all the messages.
     * @return all the messages
     */
    public List<Message> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    /**
     * The converter class for the Message Controller
     */
    @FacesConverter(forClass = Message.class)
    public static class MessageControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            MessageController controller = (MessageController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "messageController");
            return controller.getMessage(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Message) {
                Message o = (Message) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Message.class.getName()});
                return null;
            }
        }

    }

}
