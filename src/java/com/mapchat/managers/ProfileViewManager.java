/*
 * Created by Nathan Rosa on 2016.03.22  * 
 * Copyright © 2016 Nathan Rosa. All rights reserved. * 
 */
package com.mapchat.managers;

import com.mapchat.entitypackage.User;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

@Named(value = "profileViewManager")
@SessionScoped
/**
 *
 * @author Balci
 */
public class ProfileViewManager implements Serializable {

    // Instance Variable (Property)
    private User user;
    
  /**
   * The instance variable 'userFacade' is annotated with the @EJB annotation.
   * This means that the GlassFish application server, at runtime, will inject in
   * this instance variable a reference to the @Stateless session bean UserFacade.
   */  
  @EJB
  private com.mapchat.sessionbeanpackage.UserFacade userFacade;

  public ProfileViewManager() {

  }

  public String viewProfile() {
    return "Profile";
  }

  /**
   * @return the user
   */
  public User getUser() {
    return user;
  }

  public User getLoggedInUser() {
    return userFacade.find(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user_id"));
  }

  /**
   * @param user the user to set
   */
  public void setUser(User user) {
    this.user = user;
  }

}
