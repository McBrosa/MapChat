/**
 * Created by MapChat Development Team
 * Edited by Alan Cai
 * Last Modified: 2016.03.22
 * Copyright © 2016 MapChat Development Team. All rights reserved.
 */
package com.mapchat.jsfclassespackage;

import com.mapchat.entitypackage.Groups;
import com.mapchat.jsfclassespackage.util.JsfUtil;
import com.mapchat.jsfclassespackage.util.JsfUtil.PersistAction;
import com.mapchat.sessionbeanpackage.GroupsFacade;

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

@Named("groupsController")
@SessionScoped
public class GroupsController implements Serializable {

    @EJB
    private com.mapchat.sessionbeanpackage.GroupsFacade ejbFacade;
    private List<Groups> items = null;
    private Groups selected;

    /**
     * The constructor
     */
    public GroupsController() {
    }

    /**
     * Returns the selected group
     * @return The selected group
     */
    public Groups getSelected() {
        return selected;
    }

    /**
     * Sets the selected group
     * @param selected The new selected group
     */
    public void setSelected(Groups selected) {
        this.selected = selected;
    }

    /**
     * Sets the embeddable keys
     */
    protected void setEmbeddableKeys() {
    }

    /**
     * Initializes the embeddable keys
     */
    protected void initializeEmbeddableKey() {
    }

    /**
     * Gets the a GroupsFacade Object
     * @return A GroupsFacade Object
     */
    private GroupsFacade getFacade() {
        return ejbFacade;
    }

    /**
     * Sets the current group to a newly created Group, not in the database
     * @return The selected group
     */
    public Groups prepareCreate() {
        selected = new Groups();
        initializeEmbeddableKey();
        return selected;
    }

    /**
     * Executes the Serializable interface's create function
     */
    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("GroupsCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    /**
     * Executes the Serializable interface's update function
     */
    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("GroupsUpdated"));
    }

    /**
     * Executes the Serializable interface's delete function
     */
    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("GroupsDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    /**
     * Gets all Groups
     * @return A list of all Groups in the database
     */
    public List<Groups> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    /**
     * Executes the Serializable's persist function
     * @param persistAction The persist action to perform
     * @param successMessage The message to display on success
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
     * Returns a group from the database with the given id
     * @param id The id of the Groups to look for
     * @return The Groups Object with the id with the specified id
     */
    public Groups getGroups(java.lang.Integer id) {
        return getFacade().find(id);
    }

    /**
     * Returns a list of Groups from the database
     * @return A list of Groups from the database
     */
    public List<Groups> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    /**
     * Returns one Group from the database
     * @return 
     */
    public List<Groups> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    /**
     * A converter class for the groups class
     */
    @FacesConverter(forClass = Groups.class)
    public static class GroupsControllerConverter implements Converter {

        /**
         * Returns a Groups object as an Object
         * @param facesContext The FacesContext to use
         * @param component The UIComponent to use
         * @param value The string version of the id of the Groups object to look for
         * @return The Groups object as an Object with the specified id
         */
        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            GroupsController controller = (GroupsController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "groupsController");
            return controller.getGroups(getKey(value));
        }

        /**
         * Returns the integer version of a string number
         * @param value The String version of a number
         * @return The integer version of value
         */
        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        /**
         * Returns the String version of an integer
         * @param value An integer
         * @return The String version of value
         */
        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        /**
         * Returns the String version of an Object
         * @param facesContext The FacesContext to use
         * @param component The UIComponent to use
         * @param object The Object to get the String version of
         * @return THe String version of the given Object
         */
        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Groups) {
                Groups o = (Groups) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Groups.class.getName()});
                return null;
            }
        }

    }

}
