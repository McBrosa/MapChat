/* 
 * Created by Alan Cai on 2016.04.20  * 
 * Copyright © 2016 Alan Cai. All rights reserved. * 
 */

function enterNewGroupName() {
    var groupName = "";
    while(groupName.trim() === "")
        groupName = window.prompt("Enter new group's name: ");
    var hidden = document.getElementById("create-groups-form:newgroupname");
    hidden.value = groupName;
    
}

function enterUsernameToAdd() {
    var username = "";
    while(username.trim() === "")
        username = window.prompt("Enter new user's username: ");
    var hidden = document.getElementById("add-user-form:addusername");
    hidden.value = username;
}
function enterUsernameToRemove() {
    var username = "";
    while(username.trim() === "")
        username = window.prompt("Enter user's username: ");
    var hidden = document.getElementById("remove-user-form:removeusername");
    hidden.value = username;
}