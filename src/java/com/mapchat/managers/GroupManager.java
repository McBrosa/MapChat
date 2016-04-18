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
import javax.ejb.EJBException;
import javax.faces.context.FacesContext;

/**
 *
 * @author Alan
 */
@ManagedBean(name = "groupManager")
@SessionScoped
public class GroupManager implements Serializable {
    
    //private HashMap<String, ArrayList<String>> currentGroups;
    private String groupNameToCreate;
    private String groupNameToDelete;
    private String usernameToAdd;
    private String usernameToDelete;
    private User currentUser;
    private String message;
    private String statusMessage;
    
    @EJB
    private GroupsFacade groupsFacade;
  
    @EJB
    private UserGroupFacade userGroupFacade;
    
    @EJB
    private UserFacade usersFacade;
  
    public GroupManager() {
    }
    

    public String getGroupNameToCreate() {
        return groupNameToCreate;
    }

    public String getGroupNameToDelete() {
        return groupNameToDelete;
    }

    public String getUsernameToAdd() {
        return usernameToAdd;
    }

    public String getUsernameToDelete() {
        return usernameToDelete;
    }

    public void setGroupNameToCreate(String groupNameToCreate) {
        this.groupNameToCreate = groupNameToCreate;
    }

    public void setGroupNameToDelete(String groupNameToDelete) {
        this.groupNameToDelete = groupNameToDelete;
    }

    public void setUsernameToAdd(String usernameToAdd) {
        this.usernameToAdd = usernameToAdd;
    }

    public void setUsernameToDelete(String usernameToDelete) {
        this.usernameToDelete = usernameToDelete;
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
    
  
    
    /*public HashMap<String, ArrayList<String>> getCurrentGroups() {
        return currentGroups;
    }
    
    public void setCurrentGroups(HashMap<String, ArrayList<String>> currentGroups) {
        this.currentGroups = currentGroups;
    }*/
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    /*public ArrayList<String> getGroups() {
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
    }*/
        
    public String createGroup() {
        statusMessage = "";
        try
        {
            //Check to see if the group already exists
            //Groups check = groupsFacade.findByGroupname(groupNameToCreate);
            //if(check == null)
            //{
                Groups group = new Groups();
                group.setGroupName(groupNameToCreate);
                groupsFacade.create(group);
                Groups foundGroup = groupsFacade.findByGroupname(group.getGroupName());
                UserGroup userGroup = new UserGroup();
                userGroup.setGroupId(foundGroup.getId());
                currentUser = usersFacade.find(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user_id"));
                userGroup.setUserId(currentUser.getId());
                userGroupFacade.create(userGroup);
                groupNameToCreate = "";
            /*}
            else
            {
                statusMessage += groupNameToCreate + " already exists";
                groupNameToCreate = "";
                return "";
            }*/
        } catch(EJBException e)
        {
            groupNameToCreate = "";
            statusMessage += "Something went wrong creating the group";
            return "";
        }
        return "groups";
    }
    
    public String addUser(Integer groupId) {
        statusMessage = "";
        try
        {
            //Check to see if the user exists
            User check = usersFacade.findByUsername(usernameToAdd);
            if(check == null)
            {
                statusMessage += usernameToAdd + " does not exist";
                usernameToAdd = "";
                return "";
            }
            
            
            
            
            //Check to see if the user is already in the group
            if(userGroupFacade.findByIds(check.getId(), groupId) != null)
            {
                statusMessage += usernameToAdd + " is already in the group";
                usernameToAdd = "";
                return "";
            }
            User user = usersFacade.findByUsername(usernameToAdd);
            UserGroup userGroup = new UserGroup();
            userGroup.setUserId(user.getId());
            userGroup.setGroupId(groupId);
            userGroupFacade.create(userGroup);
            usernameToAdd = "";
        } catch(EJBException e)
        {
            usernameToAdd = "";
            statusMessage += "Something went wrong adding the user to the group";
            return "";
        }
        return "groups";
    }
    
    /*public String addUser(String groupName) {
        statusMessage = "";
        try
        {
            Groups group = groupsFacade.findByGroupname(groupName);
            User user = usersFacade.findByUsername(userInputUserName);
            UserGroup userGroup = new UserGroup();
            userGroup.setUserId(user.getId());
            userGroup.setGroupId(group.getId());
            userGroupFacade.create(userGroup);
        } catch(EJBException e)
        {
            userInputUserName = "";
            statusMessage += "Something went wrong adding the user to the group";
            return "";
        }
        return "groups";
    }*/
    
    public String removeUser(Integer groupId) {
        statusMessage = "";
        try
        {
            //Check to see if the user exists
            User check = usersFacade.findByUsername(usernameToDelete);
            if(check == null)
            {
                statusMessage += usernameToDelete + " does not exist";
                usernameToDelete = "";
                return "";
            }
            //Check to see if the user is in the group
            if(userGroupFacade.findByIds(check.getId(), groupId) == null)
            {
                statusMessage += usernameToDelete + " is not in the group";
                usernameToDelete = "";
                return "";
            }
            User user = usersFacade.findByUsername(usernameToDelete);
            UserGroup userGroup = new UserGroup();
            userGroup.setUserId(user.getId());
            userGroup.setGroupId(groupId);
            userGroupFacade.removeUserGroup(userGroup);
            usernameToDelete = "";
        } catch(EJBException e)
        {
            usernameToDelete = "";
            statusMessage += "Something went wrong removing the user to the group";
            return "";
        }
        return "groups";
    }
    
    /*public String removeUser(String groupName) {
        statusMessage = "";
        try
        {
            Groups group = groupsFacade.findByGroupname(groupName);
            User user = usersFacade.findByUsername(userInputUserName);
            UserGroup userGroup = new UserGroup();
            userGroup.setUserId(user.getId());
            userGroup.setGroupId(group.getId());
            userGroupFacade.remove(userGroup);
        } catch(EJBException e)
        {
            userInputUserName = "";
            statusMessage += "Something went wrong adding the user to the group";
            return "";
        }
        return "groups";
    }*/
    
    /*public String showAllUsers(Integer groupId)
    {
        
    }*/
    public String showAllGroups()
    {
        String groupsString = "";
        
        //if(currentGroups == null) {
            //currentGroups = new HashMap<String, ArrayList<String>>();
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
                groupsString += "Group ID: " + (searchResult.get(i).getGroupId()) + " User ID: " + searchResult.get(i).getUserId() + " ";
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
