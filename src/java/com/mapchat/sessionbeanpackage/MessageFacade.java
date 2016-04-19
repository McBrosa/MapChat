/*
 * Created by Nathan Rosa on 2016.03.22  * 
 * Copyright © 2016 Nathan Rosa. All rights reserved. * 
 */
package com.mapchat.sessionbeanpackage;

import com.mapchat.entitypackage.Message;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Nathan
 */
@Stateless
public class MessageFacade extends AbstractFacade<Message> {

    @PersistenceContext(unitName = "MapChatPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MessageFacade() {
        super(Message.class);
    }
    
    public Message getUser(int id) {
        return em.find(Message.class, id);
    }
    
    public List<Message> getMessagesByGroupId(Integer gid) {
        if (em.createNamedQuery("Message.findByGroupId")
            .setParameter("gid", gid)
            .getResultList().isEmpty()) {
            return null;
        }
        else {
            return (List<Message>) (em.createNamedQuery("Message.findByGroupId")
                .setParameter("gid", gid)
                .getSingleResult());        
        }
    }
    
}
