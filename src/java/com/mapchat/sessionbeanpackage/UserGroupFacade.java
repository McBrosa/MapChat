/*
 * Created by Nathan Rosa on 2016.03.22  * 
 * Copyright © 2016 Nathan Rosa. All rights reserved. * 
 */
package com.mapchat.sessionbeanpackage;

import com.mapchat.entitypackage.Groups;
import com.mapchat.entitypackage.UserGroup;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Nathan
 */
@Stateless
public class UserGroupFacade extends AbstractFacade<UserGroup> {

    @PersistenceContext(unitName = "MapChatPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UserGroupFacade() {
        super(UserGroup.class);
    }
    
    public List<UserGroup> findByUserId(Integer userId) {
        if (em.createQuery("SELECT g FROM UserGroup g WHERE g.userId = :userId")
                .setParameter("userId", userId)
                .getResultList().isEmpty()) {
            return null;
        }
        else {
            return (List<UserGroup>) (em.createQuery("SELECT g FROM UserGroup g WHERE g.userId = :userId")
                .setParameter("userId", userId)).getResultList();        
        }
    }
    public String test(Integer userId) {
        if (em.createQuery("SELECT u FROM UserGroup u WHERE u.userId = :userId")
                .setParameter("userId", userId)
                .getResultList().isEmpty()) {
            return null;
        }
        else {
            return (em.createQuery("SELECT u FROM UserGroup u")).getResultList().size() + "";        
        }
    }
    
}
