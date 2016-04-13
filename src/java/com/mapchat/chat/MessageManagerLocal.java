package com.mapchat.chat;

/*
 * Created by Sean Arcayan on 2016.04.12  * 
 * Copyright © 2016 Sean Arcayan. All rights reserved. * 
 */

import java.util.Date;
import javax.ejb.Local;
 
/**
 * Local interface for chat lagic EJB
 * @author Danon
 */
@Local
public interface MessageManagerLocal {
 
    void sendMessage(Message msg);
 
    Message getFirstAfter(Date after);
 
}