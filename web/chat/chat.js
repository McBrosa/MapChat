/* 
 * Created by Sean Arcayan on 2016.04.15  * 
 * Copyright © 2016 Sean Arcayan. All rights reserved. * 
 */

/*
 * Every 200 ms call nextMessage(), which is a remote command in chat.xhtml
 * This will update the chatroom messages
 */
setInterval("nextMessage()", 200);                
        
// the rest of the js file is unneeded, commented out just in case

/*
function createMessageNode(user, text) {
    var div = document.createElement('div');
    div.className = "msg";
    div.textContent = "[ " + user + " ] : " + text;
    return div;
}
*/
/**
 * Update the chatroom. This is called by nextMessage()
 * 
 * @param {type} xhr
 * @param {type} status
 * @param {type} args
 * @returns {undefined}
function nextMessageJS(xhr, status, args) {

    if(!args.ok) return;
    
    // define the div to place the messages
    var messages = document.getElementById('chat-messages');
    
    // create a HTML DOM node which contains the message
    msgNode = createMessageNode(args.user, args.text);
    
    // append the created node to the chat-messages div
    messages.appendChild(msgNode);
}
 */

/**
 * Retrieve the message and place them in the chat-messages div.
 * This is called when chatrooms change. 
 * @param {type} xhr
 * @param {type} status
 * @param {type} args contains arguments passed from the managed bean
 * @returns {undefined}

function retrieveMessagesJS(xhr, status, args) {
    
    // convert JSON representation of the array of messages in this chatroom 
    // to a javascript object
    var msgs = JSON.parse(args.msgjson);
    
    // get the div to place the messages in
    var messages = document.getElementById('chat-messages');
    
    // create a HTML DOM node for each message and place it in the div
    msgs.forEach(function(msg) {
        messages.appendChild(createMessageNode(msg.user, msg.message));
    });

} */

/**
 * Called on chatroom change
 * @returns {undefined}
 
function handleChatroomChange() {
    clearMessages();
    retrieveMessages({});
}*/

/**
 * Clears all messages in the chat-messages div
 * @returns {undefined}
 
function clearMessages() {
    var messages = document.getElementById('chat-messages');
    while (messages.firstChild) {
        messages.removeChild(messages.firstChild);
    }
}*/
         