/*
 * Created by Nathan Rosa on 2016.03.22  * 
 * Copyright © 2016 Nathan Rosa. All rights reserved. * 
 */
package com.mapchat.managers;

import com.mapchat.entitypackage.User;
import com.mapchat.sessionbeanpackage.UserFacade;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;

@ManagedBean(name = "passwordResetManager")
@SessionScoped
/**
 * The manager class for password reset
 */
public class PasswordResetManager implements Serializable{
    
    // Instance Variables (Properties)
    private String username;
    private String message = "";
    private String answer;
    private String password;
    
    /**
     * The instance variable 'userFacade' is annotated with the @EJB annotation.
     * This means that the GlassFish application server, at runtime, will inject in
     * this instance variable a reference to the @Stateless session bean UserFacade.
     */
    @EJB
    private UserFacade userFacade;

    /**
     * Get the username
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set the username
     * @param username 
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Get the message
     * @return message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Set the message
     * @param message 
     */
    public void setMessage(String message) {
        this.message = message;
    }
        
    /**
     * Submit the username
     * @return redirect depending if username is found
     */
    public String usernameSubmit() {
        User user = userFacade.findByUsername(username);
        if (user == null) {
            message = "Entered username does not exist!";
            return "EnterUsername?faces-redirect=true";
        }
        else {
            message = "";
            return "SecurityQuestion?faces-redirect=true";
        }
    }
    
    /**
     * submit the security question
     * @return redirect depending on security answer is correct
     */
    public String securityquestionSubmit() {
        User user = userFacade.findByUsername(username);
        if (user.getSecurityAnswer().equals(answer)) {
            message = "";
            return "ResetPassword?faces-redirect=true";
        }
        else {
            message = "Answer incorrect";
            return "SecurityQuestion?faces-redirect=true";
        }
    }
    
    /**
     * retrieve the security question
     * @return 
     */
    public String getSecurityQuestion() {
        int question = userFacade.findByUsername(username).getSecurityQuestion();
        return Constants.QUESTIONS[question];
    }

    /**
     * retrieve the answer
     * @return answer
     */
    public String getAnswer() {
        return answer;
    }

    /**
     * set the answer
     * @param answer 
     */
    public void setAnswer(String answer) {
        this.answer = answer;
    }

    /**
     * Validate the password
     * @param event 
     */
    public void validateInformation(ComponentSystemEvent event) {
        FacesContext fc = FacesContext.getCurrentInstance();

        UIComponent components = event.getComponent();
        // get password
        UIInput uiInputPassword = (UIInput) components.findComponent("password");
        String pwd = uiInputPassword.getLocalValue() == null ? ""
                : uiInputPassword.getLocalValue().toString();

        // get confirm password
        UIInput uiInputConfirmPassword = (UIInput) components.findComponent("confirmPassword");
        String confirmPassword = uiInputConfirmPassword.getLocalValue() == null ? ""
                : uiInputConfirmPassword.getLocalValue().toString();

        if (pwd.isEmpty() || confirmPassword.isEmpty()) {
            // Do not take any action. 
            // The required="true" in the XHTML file will catch this and produce an error message.
            return;
        }

        if (!pwd.equals(confirmPassword)) {
            message = "Passwords must match!";
        } else {
            message = "";
        }   
    }   

    /**
     * Get the password
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set the password
     * @param password 
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
    /**
     * Reset the password
     * @return redirect depending on success/failure
     */
    public String resetPassword() {
        if (message.equals("")) {
            message = "";
            User user = userFacade.findByUsername(username);
            try {
                user.setPassword(password);
                userFacade.edit(user);
                username = answer = password = "";                
            } catch (EJBException e) {
                message = "Something went wrong editing your profile, please try again!";
                return "ResetPassword?faces-redirect=true";            
            }
            return "index?faces-redirect=true";            
        }
        else {
            return "ResetPassword?faces-redirect=true";            
        }
    }
            
}
