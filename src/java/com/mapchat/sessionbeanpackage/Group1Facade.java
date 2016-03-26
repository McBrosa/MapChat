/*
 * Created by Nathan Rosa on 2016.03.22  * 
 * Copyright © 2016 Nathan Rosa. All rights reserved. * 
 */
package com.mapchat.sessionbeanpackage;

import com.mapchat.entitypackage.File1;
import com.mapchat.entitypackage.Group1;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Nathan
 */
@Stateless
public class Group1Facade extends AbstractFacade<Group1> {

    @PersistenceContext(unitName = "MapChatPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public Group1Facade() {
        super(Group1.class);
    }
    
    public List<File1> findById(Integer groupId) {
        return (List<File1>) em.createNamedQuery("File1.findById")
                .setParameter("id", groupId)
                .getResultList();
    }
}