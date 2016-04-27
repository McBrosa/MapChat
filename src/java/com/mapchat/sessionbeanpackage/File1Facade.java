/**
 * Created by MapChat Development Team
 * Edited by Nate Rosa, Corey McQuay
 * Last Modified: 2016.03.22
 * Copyright © 2016 MapChat Development Team. All rights reserved.
 */
package com.mapchat.sessionbeanpackage;

import com.mapchat.entitypackage.File1;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * The facade class for the File1Facade object
 *
 * @author Corey McQuay
 */
@Stateless
public class File1Facade extends AbstractFacade<File1> {

    @PersistenceContext(unitName = "MapChatPU")
    private EntityManager em;

    /**
     * Returns the entity manager for this class
     *
     * @return The entity manager for this class
     */
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    /**
     * The constructor
     */
    public File1Facade() {
        super(File1.class);
    }

    /**
     * Returns a list of files by user id using a query by the entity manager
     *
     * @param userID the id used to find the files
     * @return The list of files found by the passed in ID
     */
    public List<File1> findFilesByUserID(Integer userID) {
        return (List<File1>) em.createNamedQuery("File1.findFilesByUserId")
                .setParameter("userId", userID)
                .getResultList();
    }

    /**
     * Returns a list of files by file id using a query by the entity manager
     *
     * @param fileId the id used to find the files
     * @return The list of files found by the the passed in ID
     */
    public List<File1> findById(Integer fileId) {
        return (List<File1>) em.createNamedQuery("File1.findById")
                .setParameter("id", fileId)
                .getResultList();
    }

    /**
     * Returns a list of files by group id using a query by the entity manager
     *
     * @param groupId the id used to find the files
     * @return The list of files found by group id by the entity manager
     */
    public List<File1> findByGroupId(Integer groupId) {
        return (List<File1>) em.createNamedQuery("File1.findByGroupId")
                .setParameter("gid", groupId)
                .getResultList();
    }

}
