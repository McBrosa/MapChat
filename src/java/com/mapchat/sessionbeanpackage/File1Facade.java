/*
 * Created by Nathan Rosa on 2016.03.22  * 
 * Copyright © 2016 Nathan Rosa. All rights reserved. * 
 */
package com.mapchat.sessionbeanpackage;

import com.mapchat.entitypackage.File1;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Nathan
 */
@Stateless
public class File1Facade extends AbstractFacade<File1> {

    @PersistenceContext(unitName = "MapChatPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public File1Facade() {
        super(File1.class);
    }
    
     public List<File1> findFilesByUserID(Integer userId) {
        return (List<File1>) em.createNamedQuery("File1.findFilesByUserId")
                .setParameter("userId", userId)
                .getResultList();
    }
}
