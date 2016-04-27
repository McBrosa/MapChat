/**
 * Created by MapChat Development Team
 * Edited by Nate Rosa, Corey McQuay
 * Last Modified: 2016.03.22
 * Copyright © 2016 MapChat Development Team. All rights reserved.
 */
package com.mapchat.sessionbeanpackage;

//imports
import com.mapchat.entitypackage.Groups;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * The Facade Class of Groups 
 * @author Corey McQuay
 */
@Stateless
public class GroupsFacade extends AbstractFacade<Groups> {

    //Globals
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
    public GroupsFacade() {
        super(Groups.class);
    }
    
    /**
     * Entity Manager finds the group based id
     * @param id The group id needed to find the group
     * @return The group found by the entity manager.
     */
    public Groups getGroup(int id) {
        return em.find(Groups.class, id);
    }
    
    /**
     * Finds the group in the em based off the group name string query. 
     * @param groupname Name of the group
     * @return the groups object of the group name.
     */
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
    
    /**
     * Finds the desired group based on the id and returns it
     * @param groupId Id that searches for the group
     * @return The group with the corresponding id.
     */
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
    
    /**
     * Finds the group with the entity manager and deletes the group with its id
     * @param id the id used to find and delete the group.
     */
    public void deleteGroup(int id){       
        Groups user = em.find(Groups.class, id);
        em.remove(user);
    }
}
