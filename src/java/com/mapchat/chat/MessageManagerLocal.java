package com.mapchat.chat;

/*
 * Created by Sean Arcayan on 2016.04.12  * 
 * Copyright © 2016 Sean Arcayan. All rights reserved. * 
 */

import com.mapchat.entitypackage.Message;
import com.mapchat.entitypackage.Groups;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.ejb.Local;
import javax.ejb.LocalBean;



/**
 * Local interface for chat logic EJB
 * @author Sean Arcayan
 */
@LocalBean
public interface MessageManagerLocal {
 
    void sendMessageToCurrentGroup(Message msg);
 
    List<String> getAvailableChatrooms();
    
    List<Message> getMessagesInCurrentGroup();
}