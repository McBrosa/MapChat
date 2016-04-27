/**
 * Created by MapChat Development Team
 * Edited by Sean Arcayan
 * Last Modified: 2016.03.22
 * Copyright © 2016 MapChat Development Team. All rights reserved.
 */
package com.mapchat.sessionbeanpackage;

import com.mapchat.entitypackage.Groups;
import com.mapchat.entitypackage.Message;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * The facade class for the message object
 * @author Sean Arcayan
 */
@Stateless
public class MessageFacade extends AbstractFacade<Message> {

    //Global
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
    public MessageFacade() {
        super(Message.class);
    }
    
    /**
     * Gets the user responsible for the message
     * @param id the id of the user
     * @return  the message of the user
     */
    public Message getUser(int id) {
        return em.find(Message.class, id);
    }
    
    /**
     * Retrieve the messages given a group
     * @param grp the group's messages to retrieve
     * @return 
     */
    public List<Message> getMessagesByGroup(Groups grp) {
        if (em.createNamedQuery("Message.findByGroupId")
            .setParameter("gid", grp)
            .getResultList().isEmpty()) {
            return null;
        }
        else {
            return (List<Message>) (em.createNamedQuery("Message.findByGroupId")
                .setParameter("gid", grp)
                .getResultList());        
        }
    }
    
}
