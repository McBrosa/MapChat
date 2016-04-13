package com.mapchat.chat;

/*
 * Created by Sean Arcayan on 2016.04.12  * 
 * Copyright © 2016 Sean Arcayan. All rights reserved. * 
 */

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
 
/**
 * Simple chat logic
 * @author Sean
 */
@Singleton
@Startup
public class MessageManager implements MessageManagerLocal {
 
    // Chatroom data structure of <id, chatroom name>
    private Map<Integer, String> chatroomID_Name;
    // Chatroom data structure of <id, messages>
    private Map<Integer, List> chatroomID_Messages;
    
    //private final List messages =
    //        Collections.synchronizedList(new LinkedList());
 
    @PostConstruct
    public void init() {
        
        chatroomID_Name = 
            Collections.synchronizedMap(new HashMap());
        chatroomID_Messages = 
            Collections.synchronizedMap(new HashMap());
        
        // Chatrooms all users have access to
        chatroomID_Name.put(1, "Chatroom 1");
        chatroomID_Name.put(2, "Chatroom 2");
        chatroomID_Name.put(3, "Chatroom 3");
        
        chatroomID_Messages.put(1, Collections.synchronizedList(new LinkedList()));
        chatroomID_Messages.put(2, Collections.synchronizedList(new LinkedList()));
        chatroomID_Messages.put(3, Collections.synchronizedList(new LinkedList()));
        
    }
    
    @Override
    public void sendMessage(Message msg) {
        chatroomID_Messages.get(1).add(msg);
        msg.setDateSent(new Date());
    }
 
    @Override
    public Message getFirstAfter(Date after) {
        List messages = chatroomID_Messages.get(1);
        if(messages.isEmpty())
            return null;
        if(after == null)
            return (Message)messages.get(0);
        for(Object m : messages) {
            if(((Message)m).getDateSent().after(after))
                return (Message)m;
        }
        return null;
    }
 
}