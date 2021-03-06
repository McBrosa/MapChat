/**
 * Created by MapChat Development Team
 * Edited by Nate Rosa, Anthony Barbee
 * Last Modified: 2016.04.20
 * Copyright © 2016 MapChat Development Team. All rights reserved.
 */
package com.mapchat.managers;

import com.mapchat.entitypackage.File1;
import com.mapchat.entitypackage.User;
import com.mapchat.sessionbeanpackage.File1Facade;
import com.mapchat.sessionbeanpackage.UserFacade;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;

@ManagedBean(name = "accountManager")
@SessionScoped
public class AccountManager implements Serializable {
 
    // Instance Variables (Properties)
    private String firstName;
    private String middleName;
    private String lastName;
    private String username;
    private String password;
    private String email;
    private String phone;
    private String statusMessage;
    private int security_question;
    private String security_answer;
        
    private Map<String, Object> security_questions;
    
    private User selected;
    
    /**
     * The instance variable 'userFacade' is annotated with the @EJB annotation.
     * This means that the GlassFish application server, at runtime, will inject in
     * this instance variable a reference to the @Stateless session bean UserFacade.
     */
    @EJB
    private UserFacade userFacade;

    /**
     * The instance variable 'fileFacade' is annotated with the @EJB annotation.
     * This means that the GlassFish application server, at runtime, will inject in
     * this instance variable a reference to the @Stateless session bean PhotoFacade.
     */
    @EJB
    private File1Facade fileFacade;

    /**
     * Creates a new instance of AccountManager
     */
    public AccountManager() {
    }

    /**
     * @return the first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the first name to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Get the middle name 
     * @return  middle name
     */
    public String getMiddleName() {
        return middleName;
    }

    /**
     * Set the middle name
     * @param middleName  the new middle name
     */
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    /**
     * @return the last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the last name to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 
     * @return get the phone number
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 
     * @param phone set the phone number
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    /**
     * 
     * @return the security question
     */
    public int getSecurity_question() {
        return security_question;
    }

    /**
     * Set the security question
     * @param security_question 
     */
    public void setSecurity_question(int security_question) {
        this.security_question = security_question;
    }

    /**
     * Get the security question answer
     * @return 
     */
    public String getSecurity_answer() {
        return security_answer;
    }

    /**
     * Set the security question answer
     * @param security_answer 
     */
    public void setSecurity_answer(String security_answer) {
        this.security_answer = security_answer;
    }

    /**
     * @return Mapping of the security questions
     */
    public Map<String, Object> getSecurity_questions() {
        if (security_questions == null) {
            security_questions = new LinkedHashMap<>();
            for (int i = 0; i < Constants.QUESTIONS.length; i++) {
                security_questions.put(Constants.QUESTIONS[i], i);
            }
        }
        return security_questions;
    }
    
    /**
     * @return the statusMessage
     */
    public String getStatusMessage() {
        return statusMessage;
    }

    /**
     * @param statusMessage the statusMessage to set
     */
    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    /**
     * 
     * @return the selected user
     */
    public User getSelected() {
        if (selected == null) {
            selected = userFacade.find(FacesContext.getCurrentInstance().
                getExternalContext().getSessionMap().get("user_id"));
        }
        return selected;
    }

    /**
     * Set the selected user to a new user
     * @param selected 
     */
    public void setSelected(User selected) {
        this.selected = selected;
    }

    /**
     * Create an account for the new user
     * @return a status message
     */
    public String createAccount() {
        
        // Check to see if a user already exists with the username given.
        User aUser = userFacade.findByUsername(username);
        
        if (aUser != null) {
            username = "";
            statusMessage = "Username already exists! Please select a different one!";
            return "";
        }

        /* Set all the user basic info */
        if (statusMessage.isEmpty()) {
            try {
                User user = new User();
                user.setFirstName(firstName);
                user.setMiddleName(middleName);
                user.setLastName(lastName);
                if(security_question != 0)
                {
                    user.setSecurityQuestion(security_question);
                }
                else
                {
                    statusMessage = "Please select a Security Question.";
                    return "";
                }
                user.setSecurityAnswer(security_answer);
                user.setPhone(phone);
                user.setEmail(email);
                user.setUsername(username);                
                user.setPassword(password);
                userFacade.create(user);                
            } catch (EJBException e) {
                username = "";
                statusMessage = "Something went wrong while creating your account!";
                return "";
            }
            initializeSessionMap();
            return "Dashboard?faces-redirect=true";
        }
        return "";
    }

    /**
     * Update the account manager
     * @return the status message or Profile page
     */
    public String updateAccount() {
        if (statusMessage.isEmpty()) {
            int user_id = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user_id");
                User editUser = userFacade.getUser(user_id);
            try {
                editUser.setFirstName(this.selected.getFirstName());
                editUser.setLastName(this.selected.getLastName());
                editUser.setPhone(this.selected.getPhone());
                editUser.setEmail(this.selected.getEmail());
                editUser.setPassword(this.selected.getPassword());
                userFacade.edit(editUser);
            } catch (EJBException e) {
                username = "";
                statusMessage = "Something went wrong while editing your profile!";
                return "";
            }
            return "Profile";
        }
        return "";
    }
    
    /**
     * Update the the uses location and send to MySQL database
     */
    public void updateLocation()
    {
        int user_id = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user_id");
        User editUser = userFacade.getUser(user_id);
        editUser.setLocationX(this.selected.getLocationX());
        editUser.setLocationY(this.selected.getLocationY());
        userFacade.edit(editUser);
    }
    
    /**
     * Delete the user account and return the index page
     * @return error message or index.xhtml
     */
    public String deleteAccount() {
        if (statusMessage.isEmpty()) {
            int user_id = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user_id");
            try {
                userFacade.deleteUser(user_id);
                                
            } catch (EJBException e) {
                username = "";
                statusMessage = "Something went wrong while deleting your account!";
                return "";
            }
            
            return "/index.xhtml?faces-redirect=true";
        }
        return "";
    }
    
    /**
     * Make sure the two password are equal when the event is received
     * @param event the event to listen for
     */
    public void validateInformation(ComponentSystemEvent event) {
        FacesContext fc = FacesContext.getCurrentInstance();

        UIComponent components = event.getComponent();
        // Get password
        UIInput uiInputPassword = (UIInput) components.findComponent("password");
        String pwd = uiInputPassword.getLocalValue() == null ? ""
                : uiInputPassword.getLocalValue().toString();

        // Get confirm password
        UIInput uiInputConfirmPassword = (UIInput) components.findComponent("confirmPassword");
        String confirmPassword = uiInputConfirmPassword.getLocalValue() == null ? ""
                : uiInputConfirmPassword.getLocalValue().toString();

        if (pwd.isEmpty() || confirmPassword.isEmpty()) {
            // Do not take any action. 
            // The required="true" in the XHTML file will catch this and produce an error message.
            return;
        }

        if (!pwd.equals(confirmPassword)) {
            statusMessage = "Passwords must match!";
        } else {
            statusMessage = "";
        }   
    }

    /**
     * Initialize the session map
     */
    public void initializeSessionMap() {
        User user = userFacade.findByUsername(getUsername());
        FacesContext.getCurrentInstance().getExternalContext().
                getSessionMap().put("username", username);
        FacesContext.getCurrentInstance().getExternalContext().
                getSessionMap().put("user_id", user.getId());
    }

    /**
     * Make sure the correct password was entered
     * @param components the component to check
     * @return true is correct, false otherwise
     */
    private boolean correctPasswordEntered(UIComponent components) {
        UIInput uiInputVerifyPassword = (UIInput) components.findComponent("verifyPassword");
        String verifyPassword = uiInputVerifyPassword.getLocalValue() == null ? ""
                : uiInputVerifyPassword.getLocalValue().toString();
        if (verifyPassword.isEmpty()) {
            statusMessage = "";
            return false;
        } else {
            if (verifyPassword.equals(password)) {
                return true;
            } else {
                statusMessage = "Invalid password entered!";
                return false;
            }
        }
    }

    /**
     * Logout the current user and clear all fields. Also redirect to home page.
     * @return the index.xhtml
     */
    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().clear();
        username = firstName = middleName = lastName = password = email = statusMessage = "";
        phone = security_answer = "";
        security_question = 0;
        
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/index.xhtml?faces-redirect=true";
    }
   
    /**
     * Get the user photo form FIleStorageLocation
     * @return the location of the use photo
     */
    public String userPhoto() {
        String user_name = (String) FacesContext.getCurrentInstance()
                .getExternalContext().getSessionMap().get("username");
        User user = userFacade.findByUsername(user_name);
        List<File1> fileList = fileFacade.findFilesByUserID(user.getId());
        if (fileList.isEmpty()) {
            return "defaultUserPhoto.png";
        }
        return fileList.get(0).getThumbnailName();
    }
}
