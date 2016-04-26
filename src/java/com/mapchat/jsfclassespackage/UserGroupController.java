/*
 * Created by MapChat Development Team
 * Edited by Nathan Rosa, Anthony Barbee
 * Last Modified: 2016.03.28
 * Copyright © 2016 MapChat Development Team. All rights reserved.
 */
package com.mapchat.jsfclassespackage;

import com.mapchat.entitypackage.UserGroup;
import com.mapchat.jsfclassespackage.util.JsfUtil;
import com.mapchat.jsfclassespackage.util.JsfUtil.PersistAction;
import com.mapchat.sessionbeanpackage.UserGroupFacade;

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

/**
 *
 * @author Nathan Rosa
 * @author Anthony Barbee
 */
@Named("userGroupController")
@SessionScoped
public class UserGroupController implements Serializable {

    @EJB
    private com.mapchat.sessionbeanpackage.UserGroupFacade ejbFacade;
    private List<UserGroup> items = null;
    private UserGroup selected;

    /**
     * Establish a UserGroup Controller
     */
    public UserGroupController() {
    }

    /**
     * Get the selected UserGroup 
     * @return selected UserGroup
     */
    public UserGroup getSelected() {
        return selected;
    }

    /**
     * Set the selected UserGroup
     * @param selected UserGroup
     */
    public void setSelected(UserGroup selected) {
        this.selected = selected;
    }

    /**
     * Setter Method for EmbeddableKeys (Serializable)
     */
    protected void setEmbeddableKeys() {
    }
    
    /**
     * Initializing Method for EmbeddableKeys (Serializable)
     */
    protected void initializeEmbeddableKey() {
    }

    /**
     * Getter method for the UserGroupFacade
     * @return ejbFacade - the UserGroupFacade
     */
    private UserGroupFacade getFacade() {
        return ejbFacade;
    }

    /**
     * Initialize EmbeddableKey for selected UserGroup
     * @return selected (after initializeEmbeddableKey method)
     */
    public UserGroup prepareCreate() {
        selected = new UserGroup();
        initializeEmbeddableKey();
        return selected;
    }

    /**
     * Create method for UserGroup in the Bundle properties
     */
    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("UserGroupCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    /**
     * Update method for UserGroup in the Bundle properties
     */
    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("UserGroupUpdated"));
    }
    
    /**
     * Destroy method for UserGroup in the Bundle properties
     */
    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("UserGroupDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    /**
     * Get the List of UserGroups
     * @return items (UserGroup List)
     */
    public List<UserGroup> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    /**
     * Persist Method for UserGroupController. Interacts w/ Bundle.properties
     * @param persistAction - desired persist action
     * @param successMessage - desired message on success
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
     * Get the UserGroup by id (reference Facade class)
     * @param id
     * @return UserGroup
     */
    public UserGroup getUserGroup(java.lang.Integer id) {
        return getFacade().find(id);
    }

    /**
     * Get the UserGroup List (Selecting Many)
     * @return 
     */
    public List<UserGroup> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    /**
     * Get the UserGroup List (Selecting only one)
     * @return 
     */
    public List<UserGroup> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    /**
     * Converter class for UserGroupController()
     */
    @FacesConverter(forClass = UserGroup.class)
    public static class UserGroupControllerConverter implements Converter {

        /**
         * Method for getting a UserGroup as an object
         * @param facesContext
         * @param component
         * @param value
         * @return 
         */
        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            UserGroupController controller = (UserGroupController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "userGroupController");
            return controller.getUserGroup(getKey(value));
        }

        /**
         * Get the key based on a value
         * @param value
         * @return 
         */
        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        /**
         * Get a String based off an Integer value
         * @param value
         * @return 
         */
        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        /**
         * Get a UserGroup as a String component
         * @param facesContext
         * @param component
         * @param object
         * @return 
         */
        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof UserGroup) {
                UserGroup o = (UserGroup) object;
                return getStringKey(o.getUserId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), UserGroup.class.getName()});
                return null;
            }
        }

    }

}
