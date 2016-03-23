/*
 * Created by Nathan Rosa on 2016.03.22  * 
 * Copyright © 2016 Nathan Rosa. All rights reserved. * 
 */
package com.mapchat.sessionbeanpackage;

import com.mapchat.entitypackage.UserGroup;
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
    
}
