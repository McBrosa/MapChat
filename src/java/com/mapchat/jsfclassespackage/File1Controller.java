/*
 * Created by MapChat Development Team
 * Edited by Corey McQuay
 * Last Modified: 2016.03.28
 * Copyright © 2016 MapChat Development Team. All rights reserved.
 */

package com.mapchat.jsfclassespackage;

//Imports project files 
import com.mapchat.entitypackage.File1;
import com.mapchat.jsfclassespackage.util.JsfUtil;
import com.mapchat.jsfclassespackage.util.JsfUtil.PersistAction;
import com.mapchat.sessionbeanpackage.File1Facade;

//Import Java Files
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

//Bean attributes
@Named("fileController")
@SessionScoped
public class File1Controller implements Serializable {

    //Global Variables 
    @EJB
    private com.mapchat.sessionbeanpackage.File1Facade ejbFacade;
    private List<File1> items = null;
    private File1 selected;

    /**
     *Constructor for the class to make the object
     */
    public File1Controller() {
    }

    /**
     * Gets the selected file in the field
     * @return the selected file.
     */
    public File1 getSelected() {
        return selected;
    }

    /**
     * Setter of the selected file.
     * @param selected the new selected file.
     */
    public void setSelected(File1 selected) {
        this.selected = selected;
    }

    /**
     * Sets the Embeddable Keys so that keys can be accessed.
     */
    protected void setEmbeddableKeys() {
    }

    /**
     *Initializes the embedded key
     */
    protected void initializeEmbeddableKey() {
    }

    /**
     * Gets the Facade from the ejb
     * @return The current ejbFacade
     */
    private File1Facade getFacade() {
        return ejbFacade;
    }

    /**
     * Prepares and creates the selected file and initializes it into a embedabble key
     * 
     * @return
     */
    public File1 prepareCreate() {
        selected = new File1();
        initializeEmbeddableKey();
        return selected;
    }

    /**
     * Creates the file into the DB
     */
    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("FileCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    /**
     * Updates the file in the DB
     */
    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("FileUpdated"));
    }

    /**
     * Destroys the file in the DB
     */
    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("FileDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    /**
     * Gets The list of file items found
     * @return The list of file items found
     */
    public List<File1> getItems() {
        if (items == null) { //Null Check
            items = getFacade().findAll();
        }
        return items;
    }

    /**
     * Calls seriazable persist function
     * @param persistAction the action that needs to be done 
     * @param successMessage message letting know if the db action was success or not
     */
    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys(); //Sets the embedabble keys
            try { //Delete action
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
                if (msg.length() > 0) { //Error Messages
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
     *
     * @param id the file id number.
     * @return The file in the database
     * Finds the file in the db by the id
     */
    public File1 getFile(java.lang.Integer id) {
        return getFacade().find(id);
    }

    /**
     * Selects many files based on relation
     * @return all the files based off the facade.
     */
    public List<File1> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    /**
     * Selects one of the files from available files
     * @return
     */
    public List<File1> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    /**
     * Converts the information into of the file controller
     */
    @FacesConverter(forClass = File1.class)
    public static class FileControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null; //Null Check
            }
            File1Controller controller = (File1Controller) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "fileController");
            return controller.getFile(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        /**
         * Makes the key into the string 
         * @param value The value to be turn into a string
         * @return The key as a string
         */
        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        /**
         * Overridable to Get the string of the File Controller Object
         * @param facesContext the context of the face,
         * @param component the component
         * @param object the object
         * @return
         */
        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null; //Null Check
            }
            if (object instanceof File1) {
                File1 o = (File1) object; //Gets the file
                return getStringKey(o.getId()); //Prints out the object id
            } else { //Logs the file controller string 
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), File1.class.getName()});
                return null; 
            }
        }
    }
}
