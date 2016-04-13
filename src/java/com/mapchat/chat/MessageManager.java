package com.mapchat.chat;

/*
 * Created by Sean Arcayan on 2016.04.12  * 
 * Copyright © 2016 Sean Arcayan. All rights reserved. * 
 */

import java.util.ArrayList;
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
 
    // List of Chatrooms
    private List<String> chatroomNameList;
    // Chatroom data structure of <id, messages>
    private Map<String, List> chatroomName_Messages;
    
    //private final List messages =
    //        Collections.synchronizedList(new LinkedList());
 
    @PostConstruct
    public void init() {
        
        chatroomNameList = Collections.synchronizedList(new ArrayList<String>());
        
        chatroomName_Messages = 
            Collections.synchronizedMap(new HashMap<String, List>());
        
        // Chatrooms all users have access to
        chatroomNameList.add("Chatroom 1");
        chatroomNameList.add("Chatroom 2");
        chatroomNameList.add("Chatroom 3");
        
        // create a message stream for each chatroom
        for (String room : chatroomNameList) {
            chatroomName_Messages.put(room, Collections.synchronizedList(new LinkedList<Message>()));
        }    
    }
    
    @Override
    public void sendMessage(Message msg, String chatroomName) {
        chatroomName_Messages.get(chatroomName).add(msg);
        msg.setDateSent(new Date());
    }
 
    @Override
    public Message getFirstAfter(Date after, String chatroomName) {
        List messages = chatroomName_Messages.get(chatroomName);
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

    @Override
    public List<String> getAvailableChatrooms() {
        return chatroomNameList;
    }
    
    @Override
    public List<String> getMessagesByChatroom(String chatroomName) {
        List<Message> msgs = chatroomName_Messages.get(chatroomName);
        List<String> strMsgs = new ArrayList<>();
        for (Message msg : msgs) {
            strMsgs.add(msg.toString());
        }
        return strMsgs;
    }
}