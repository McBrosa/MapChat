/**
 * Created by MapChat Development Team
 * Edited by Nate Rosa
 * Last Modified: 2016.03.22
 * Copyright © 2016 MapChat Development Team. All rights reserved.
 */
package com.mapchat.sessionbeanpackage;

import com.mapchat.entitypackage.File1;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
    
     public List<File1> findFilesByUserID(Integer userID) {
        return (List<File1>) em.createNamedQuery("File1.findFilesByUserId")
                .setParameter("userId", userID)
                .getResultList();
    }
    public List<File1> findById(Integer fileId) {
        return (List<File1>) em.createNamedQuery("File1.findById")
                .setParameter("id", fileId)
                .getResultList();
    }
    public List<File1> findByGroupId(Integer groupId) {
        return (List<File1>) em.createNamedQuery("File1.findByGroupId")
                .setParameter("gid", groupId)
                .getResultList();
    }
    
}
