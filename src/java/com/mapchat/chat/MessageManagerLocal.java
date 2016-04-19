package com.mapchat.chat;

/*
 * Created by Sean Arcayan on 2016.04.12  * 
 * Copyright © 2016 Sean Arcayan. All rights reserved. * 
 */
import java.util.List;
import javax.ejb.Local;
import com.mapchat.entitypackage.Message;
 
/**
 * Local interface for chat logic EJB
 * @author Danon
 */
@Local
public interface MessageManagerLocal {
 
    void sendMessage(Message msg, String chatroomName);
 
    List<String> getAvailableChatrooms();
    
    List<Message> getMessagesByChatroom(String chatroomName);
 
}