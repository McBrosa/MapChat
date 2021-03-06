/**
 * Created by MapChat Development Team
 * Edited by Nate Rosa, Corey McQuay
 * Last Modified: 2016.03.25
 * Copyright © 2016 MapChat Development Team. All rights reserved.
 */
package com.mapchat.sessionbeanpackage;

import com.mapchat.entitypackage.User;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *  The facade class for the UserFacade object
 * @author Nathan Rosa
 */
@Stateless
public class UserFacade extends AbstractFacade<User> {

    //The persistence context of the entity class.
    @PersistenceContext(unitName = "MapChatPU")
    private EntityManager em;

    /**
     * Returns the entity manager for this class
     * @return The entity manager for this class
     */
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    /**
     * The constructor
     */
    public UserFacade() {
        super(User.class);
    }
    
    /**
     * Returns a User with the given user id
     * @param id The id of the User to return
     * @return The User with the given id
     */
    public User getUser(int id) {
        return em.find(User.class, id);
    }

    /**
     * Finds a User with the given username
     * @param username THe username of the User to find
     * @return A User with the given username
     */
    public User findByUsername(String username) {
        if (em.createQuery("SELECT u FROM User u WHERE u.username = :uname")
                .setParameter("uname", username)
                .getResultList().isEmpty()) {
            return null;
        }
        else {
            return (User) (em.createQuery("SELECT u FROM User u WHERE u.username = :uname")
                .setParameter("uname", username)
                .getSingleResult());        
        }
    }
    
    /**
     * Deletes a User from the database based on the User id given
     * @param id The id of a User to delete
     */
    public void deleteUser(int id){       
        User user = em.find(User.class, id);
        em.remove(user);
    }
}
