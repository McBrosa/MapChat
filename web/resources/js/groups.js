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
    
}
function enterUsernameToRemove() {
    
}