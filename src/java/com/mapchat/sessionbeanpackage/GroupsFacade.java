/**
 * Created by MapChat Development Team
 * Edited by Nate Rosa
 * Last Modified: 2016.03.22
 * Copyright © 2016 MapChat Development Team. All rights reserved.
 */
package com.mapchat.sessionbeanpackage;

import com.mapchat.entitypackage.Groups;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class GroupsFacade extends AbstractFacade<Groups> {

    @PersistenceContext(unitName = "MapChatPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public GroupsFacade() {
        super(Groups.class);
    }
    
    public Groups getGroup(int id) {
        return em.find(Groups.class, id);
    }
    
    public Groups findByGroupname(String groupname) {
        if (em.createQuery("SELECT g FROM Groups g WHERE g.groupName = :gname")
                .setParameter("gname", groupname)
                .getResultList().isEmpty()) {
            return null;
        }
        else {
            return (Groups) (em.createQuery("SELECT g FROM Groups g WHERE g.groupName = :gname")
                .setParameter("gname", groupname)
                .getSingleResult());        
        }
    }
    
    public Groups findById(Integer groupId) {
        if (em.createQuery("SELECT g FROM Groups g WHERE g.id = :gid")
                .setParameter("gid", groupId)
                .getResultList().isEmpty()) {
            return null;
        }
        else {
            return (Groups) (em.createQuery("SELECT g FROM Groups g WHERE g.id = :gid")
                .setParameter("gid", groupId)
                .getSingleResult());        
        }
    }
    
    public void deleteGroup(int id){       
        Groups user = em.find(Groups.class, id);
        em.remove(user);
    }
}
