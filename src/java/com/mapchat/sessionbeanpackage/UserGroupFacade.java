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
    public List<UserGroup> findByIds(Integer userId, Integer groupId) {
        if (em.createNamedQuery("UserGroup.findByIds")
                .setParameter("userId", userId)
                .setParameter("groupId", groupId)
                .getResultList().isEmpty()) {
            return null;
        }
        else {
            return (List<UserGroup>) (em.createNamedQuery("UserGroup.findByIds")
                .setParameter("userId", userId)
                .setParameter("groupId", groupId)
                .getResultList());        
        }
    }
    public void removeUserGroup(UserGroup userGroup) {
        em.createQuery("DELETE FROM UserGroup WHERE u.userId = :userId AND u.groupId = :groupId")
                .setParameter("userId", userGroup.getUserId())
                .setParameter("groupId", userGroup.getGroupId());
    }
}
