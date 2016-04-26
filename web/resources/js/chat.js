/* 
 * Created by Sean Arcayan on 2016.04.15  * 
 * Copyright © 2016 Sean Arcayan. All rights reserved. * 
 */

/*
 * Every 200 ms call nextMessage(), which is a remote command in chat.xhtml
 * This will update the chatroom messages
 */
setInterval("nextMessage()", 200);                
       
       
       // TODO REMOVE if not used
function start() {
    PF('statusDialog').show();
}
 
function stop() {
    PF('statusDialog').hide();
}
         