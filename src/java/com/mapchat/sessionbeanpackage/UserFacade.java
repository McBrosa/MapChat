/*
 * Created by Nathan Rosa on 2016.03.22  * 
 * Copyright © 2016 Nathan Rosa. All rights reserved. * 
 */
package com.mapchat.sessionbeanpackage;

import com.mapchat.entitypackage.User;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Nathan
 */
@Stateless
public class UserFacade extends AbstractFacade<User> {

    @PersistenceContext(unitName = "MapChatPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UserFacade() {
        super(User.class);
    }
    
    public User getUser(int id) {
        return em.find(User.class, id);
    }

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
    
    public void deleteUser(int id){       
        User user = em.find(User.class, id);
        em.remove(user);
    }
}
