/*
 * Created by Alan Cai on 2016.03.23  * 
 * Copyright © 2016 Alan Cai. All rights reserved. * 
 */
package com.mapchat.managers;

import com.mapchat.entitypackage.Groups;
import com.mapchat.entitypackage.User;
import com.mapchat.entitypackage.UserGroup;
import com.mapchat.sessionbeanpackage.GroupsFacade;
import com.mapchat.sessionbeanpackage.UserFacade;
import com.mapchat.sessionbeanpackage.UserGroupFacade;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.ejb.EJBException;
import javax.faces.context.FacesContext;

/**
 *
 * @author Alan
 */
@ManagedBean(name = "groupManager")
@SessionScoped
public class GroupManager implements Serializable {
    
    private HashMap<String, ArrayList<String>> currentGroups;
    private String newGroup;
    private String message;
    private User currentUser;
    private String statusMessage;
    
    @EJB
    private GroupsFacade groupsFacade;
  
    @EJB
    private UserGroupFacade userGroupFacade;
    
    @EJB
    private UserFacade usersFacade;
  
    public GroupManager() {
    }
    
    public String getStatusMessage()
    {
        return statusMessage;
    }
    
    public void setStatusMessage(String message)
    {
        statusMessage = message;
    }
    
    public User getCurrentUser() {
        return currentUser;
    }
    
    public void setCurrentUser(User user) {
        currentUser = user;
    }
    
    public HashMap<String, ArrayList<String>> getCurrentGroups() {
        return currentGroups;
    }
    
    public void setCurrentGroups(HashMap<String, ArrayList<String>> currentGroups) {
        this.currentGroups = currentGroups;
    }
    
    public String getNewGroup() {
        return this.newGroup;
    }
    
    public void setNewGroup(String newGroup) {
        this.newGroup = newGroup;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public ArrayList<String> getGroups() {
        Iterator iterator = currentGroups.entrySet().iterator();
        ArrayList<String> groups = new ArrayList<String>();
        while(iterator.hasNext())
        {
            Map.Entry pair = (Map.Entry)iterator.next();
            groups.add((String)pair.getKey());
        }
        return groups;
    }
    
    public void removeGroup(String group) {
        currentGroups.remove(group);
    }
    
    public void addGroup() {
        currentGroups.put(newGroup, new ArrayList<String>());
    }
    
    public ArrayList<String> getUsers(String group) {
        return currentGroups.get(group);
    }
    
    public void addUser(String group, String user) {
        currentGroups.get(group).add(user);
    }
    
    public void removeUser(String group, String user) {
        currentGroups.get(group).remove(user);
    }
        
    public String createGroup() {
        statusMessage = "";
        try
        {
            Groups group = new Groups();
            group.setGroupName(newGroup);
            groupsFacade.create(group);
            Groups foundGroup = groupsFacade.findByGroupname(group.getGroupName());
            UserGroup userGroup = new UserGroup();
            userGroup.setGroupId(foundGroup.getId());
            currentUser = usersFacade.find(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user_id"));
            userGroup.setUserId(currentUser.getId());
            userGroupFacade.create(userGroup);
        } catch(EJBException e)
        {
            newGroup = "";
            statusMessage += "Something went wrong creating the group";
            return "";
        }
        return "groups";
    }
    
    /*public String addUser() {
        statusMessage = "";
        return "groups";
    }
    public String showAllUsers(Integer groupId)
    {
        
    }*/
    public String showAllGroups()
    {
        String groupsString = "";
        
        //if(currentGroups == null) {
            currentGroups = new HashMap<String, ArrayList<String>>();
            currentUser = usersFacade.find(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user_id"));
            //ArrayList<UserGroup> searchResult = new ArrayList<UserGroup>(userGroupFacade.findByUserId(currentUser.getId()));
            ArrayList<UserGroup> searchResult = new ArrayList<UserGroup>(userGroupFacade.findAll());
            
            //UserGroup temp = userGroupFacade.findByUserId(currentUser.getId()).get(0);
            //groupsString = currentUser.getId() + " , " + temp.getGroupId() + ", " + temp.getUserId();
            //groupsString += userGroupFacade.findByUserId(1).size();
            //groupsString += userGroupFacade.test(currentUser.getId());
            for(int i = 0; i < searchResult.size(); i++)
            {
                //groupsString += searchResult.size() + "<<>>";
                groupsString += "Group ID: " + (searchResult.get(i).getGroupId()) + " User ID: " + searchResult.get(i).getUserId() + "\n<br />";
            }
        //
        /*Iterator iterator = currentGroups.entrySet().iterator();
        ArrayList<String> groupsArrayList = new ArrayList<String>();
        while(iterator.hasNext())
        {
            Map.Entry pair = (Map.Entry)iterator.next();
            groupsArrayList.add((String)pair.getKey());
            groupsString += pair.getKey() + "\n";
        }*/
        
        return groupsString;
    }
}
