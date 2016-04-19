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
import com.mapchat.entitypackage.Message;
 
/**
 * Simple chat logic
 * @author Sean
 */
@Singleton
@Startup
public class MessageManager implements MessageManagerLocal {
 
    // List of Chatrooms
    private List<String> globalChatroomNameList;
    // Chatroom data structure of <chatroom name, list>
    private Map<String, List> chatroomName_Messages;
    
    @PostConstruct
    public void init() {
        
        globalChatroomNameList = Collections.synchronizedList(new ArrayList<String>());
        
        chatroomName_Messages = 
            Collections.synchronizedMap(new HashMap<String, List>());
        
        // Chatrooms all users have access to
        globalChatroomNameList.add("#Music");
        globalChatroomNameList.add("#For Sale");
        globalChatroomNameList.add("#Entertainment");
        
        // create a message stream for each chatroom
        for (String room : globalChatroomNameList) {
            chatroomName_Messages.put(room, Collections.synchronizedList(new LinkedList<Message>()));
        }    
    }
    
    @Override
    public void sendMessage(Message msg, String chatroomName) {
        chatroomName_Messages.get(chatroomName).add(msg);
    }

    @Override
    public List<String> getAvailableChatrooms() {
        return globalChatroomNameList;
    }
    
    @Override
    public List<Message> getMessagesByChatroom(String chatroomName) {
        
        return chatroomName_Messages.get(chatroomName);
        
    }
}