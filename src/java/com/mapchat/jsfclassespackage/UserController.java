/**
 * Created by MapChat Development Team
 * Edited by Nathan Rosa
 * Last Modified: 2016.03.22
 * Copyright © 2016 MapChat Development Team. All rights reserved.
 */
package com.mapchat.jsfclassespackage;

import com.mapchat.entitypackage.User;
import com.mapchat.jsfclassespackage.util.JsfUtil;
import com.mapchat.jsfclassespackage.util.JsfUtil.PersistAction;
import com.mapchat.sessionbeanpackage.UserFacade;

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

@Named("userController")
@SessionScoped
public class UserController implements Serializable {

    @EJB
    private com.mapchat.sessionbeanpackage.UserFacade ejbFacade;
    private List<User> items = null;
    private User selected;

    /**
     * UserController constructor
     */
    public UserController() {
    }

    /**
     * 
     * @return seleted user
     */
    public User getSelected() {
        return selected;
    }

    /**
     * Set the selected user to new user
     * @param selected 
     */
    public void setSelected(User selected) {
        this.selected = selected;
    }

    /**
     * Interface implemented method
     */
    protected void setEmbeddableKeys() {
    }

    /**
     * Interface implemented method
     */
    protected void initializeEmbeddableKey() {
    }

    /**
     * Get userFacade
     * @return userFacade
     */
    private UserFacade getFacade() {
        return ejbFacade;
    }

    /**
     * Prepare the user to be created
     * @return the new user
     */
    public User prepareCreate() {
        selected = new User();
        initializeEmbeddableKey();
        return selected;
    }

    /**
     * Create the PersistAction
     */
    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("UserCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    /**
     * Update the PersistAction
     */
    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("UserUpdated"));
    }

    /**
     * Destroy the PersistAction
     */
    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("UserDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    /**
     * Get list of users
     * @return list of users
     */
    public List<User> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    /**
     * Serializable persist operation that does CRUD interactions
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
     * Get user by user id
     * @param id of the user
     * @return User object
     */
    public User getUser(java.lang.Integer id) {
        return getFacade().find(id);
    }

    /**
     * Get the list all users from facade
     * @return list of users
     */
    public List<User> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    /**
     * return the list of users by select one
     * @return list of users
     */
    public List<User> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    /**
     * Convert faces context to userController object
     */
    @FacesConverter(forClass = User.class)
    public static class UserControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            UserController controller = (UserController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "userController");
            return controller.getUser(getKey(value));
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
            if (object instanceof User) {
                User o = (User) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), User.class.getName()});
                return null;
            }
        }

    }

}
