/**
 * Created by MapChat Development Team
 * Edited by Nate Rosa
 * Last Modified: 2016.03.22
 * Copyright © 2016 MapChat Development Team. All rights reserved.
 */
package com.mapchat.sessionbeanpackage;

import com.mapchat.entitypackage.UserGroup;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class UserGroupFacade extends AbstractFacade<UserGroup> {

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
    public UserGroupFacade() {
        super(UserGroup.class);
    }
    
    /**
     * Returns a list of UserGroup objects where the user id in all the objects
     * equals the given user id
     * @param userId The user id in the UserGroup object to look for
     * @return A list of UserGroup objects where the user id equals the given user id
     */
    public List<UserGroup> findByUserId(Integer userId) {
        if (em.createNamedQuery("UserGroup.findByUserId")
                .setParameter("userId", userId)
                .getResultList().isEmpty()) {
            return null;
        }
        else {
            return (List<UserGroup>) (em.createNamedQuery("UserGroup.findByUserId")
                .setParameter("userId", userId)).getResultList();        
        }
    }
    
    /**
     * Returns a list of UserGroup objects where the group id in all the objects
     * equals the given group id
     * @param groupId The group id in the UserGroup object to look for
     * @return A list of UserGroup objects where the group id equals the given group id
     */
    public List<UserGroup> findByGroupId(Integer groupId) {
        if (em.createNamedQuery("UserGroup.findByGroupId")
                .setParameter("groupId", groupId)
                .getResultList().isEmpty()) {
            return null;
        }
        else {
            return (List<UserGroup>) (em.createNamedQuery("UserGroup.findByGroupId")
                .setParameter("groupId", groupId)).getResultList();        
        }
    }
    /**
     * Returns a list of UserGroup objects where both the group id and user id
     * in all the objects equals the given group id and the user id respectively
     * @param userId The user id in a UserGroup object to look for
     * @param groupId The group id in a UserGroup object to look for
     * @return A UserGroup object with both the given user id and group id in
     * their respective field
     */
    public UserGroup findByIds(Integer userId, Integer groupId) {
        if (em.createNamedQuery("UserGroup.findByIds")
                .setParameter("userId", userId)
                .setParameter("groupId", groupId)
                .getResultList().isEmpty()) {
            return null;
        }
        else {
            return (UserGroup) (em.createNamedQuery("UserGroup.findByIds")
                .setParameter("userId", userId)
                .setParameter("groupId", groupId)
                .getSingleResult());        
        }
    }
    
    //Function to delete TODO remove
    public UserGroup test() {
        return em.find(UserGroup.class, new Integer(6));
    }
    
    /**
     * Deletes a user group row from the database
     * @param usergroup The user group to delete from the databases
     */
    public void deleteUserGroup(UserGroup usergroup){   
        UserGroup temp = em.merge(usergroup);
        em.remove(temp);
    }
}
