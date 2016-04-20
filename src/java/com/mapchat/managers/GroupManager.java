/*
 * Created by Alan Cai on 2016.03.23  * 
 * Copyright © 2016 Alan Cai. All rights reserved. * 
 */
package com.mapchat.managers;

import com.mapchat.entitypackage.Groups;
import com.mapchat.entitypackage.Message;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedProperty;
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
    private String currentGroupName;
    private List<String> nonGlobalGroupNames;        
    private List<String> globalGroupNames; // List of global groups
    private Map<String, Collection> groupNameMessagesMap; // Chatroom data structure of <group name, list of messages>
    
    @ManagedProperty(value="#{profileViewManager}")
    private ProfileViewManager profileViewManager;
    
    @EJB
    private GroupsFacade groupsFacade;
  
    @EJB
    private UserGroupFacade userGroupFacade;
    
    @EJB
    private UserFacade usersFacade;
    
    @PostConstruct
    public void init() {
        nonGlobalGroupNames = Collections.synchronizedList(new ArrayList<String>());
        globalGroupNames = Collections.synchronizedList(new ArrayList<String>());
        groupNameMessagesMap = 
            Collections.synchronizedMap(new HashMap<String, Collection>());
        
        initializeGlobalGroups();
        
    }

    public ProfileViewManager getProfileViewManager() {
        return profileViewManager;
    }

    public void setProfileViewManager(ProfileViewManager profileViewManager) {
        this.profileViewManager = profileViewManager;
    }   
    
    public String getCurrentGroupName() {
        return currentGroupName;
    }

    public void setCurrentGroupName(String currentGroupName) {
        this.currentGroupName = currentGroupName;
    }

    public List<String> getNonGlobalGroupNames() {
        List<UserGroup> usergroups = userGroupFacade.findByUserId(profileViewManager.getLoggedInUser().getId());
        if (usergroups == null) 
        {
            return null;
        }
        
        for (UserGroup ug : usergroups) {
            String name = groupsFacade.getGroup(ug.getGroupId()).getGroupName();
            nonGlobalGroupNames.add(groupsFacade.getGroup(ug.getGroupId()).getGroupName());
            System.out.println("group name: " + name);
        }
        return nonGlobalGroupNames;
    }

    public void setNonGlobalGroupNames(List<String> nonGlobalGroupNames) {
        this.nonGlobalGroupNames = nonGlobalGroupNames;
    }    
    
    
    public List<String> getGlobalGroupNames() {
        return globalGroupNames;
    }

    public void setGlobalGroupNames(List<String> globalGroupNames) {
        this.globalGroupNames = globalGroupNames;
    }
    
    public List<String> getAvailableChatrooms() {
        return globalGroupNames;
    }
    
    
    public List<Message> getMessagesByChatroom(String chatroomName) {
        
        return (List<Message>)groupNameMessagesMap.get(chatroomName);
        
    }    
    
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
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
        
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
                sgtatusMessage += groupNameToCreate + " already exists";
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
    
    public String deleteGroup(Integer groupId) {
        statusMessage = "";
        try
        {
            //Check to see if the group already exists
            Groups check = groupsFacade.findById(groupId);
            ArrayList<UserGroup> check2 = (ArrayList<UserGroup>)userGroupFacade.findByGroupId(groupId);
            if(check == null)
            {
                statusMessage += "The doesn't exists";
                return "";
            }
            else if(check2 != null) {
                statusMessage += "The chat is not empty!";
                return "";
            } else
            {
                //Groups foundGroup = groupsFacade.findById(groupId);
                groupsFacade.deleteGroup(groupId);
                
                UserGroup userGroup = new UserGroup();
                userGroup.setGroupId(groupId);
                currentUser = usersFacade.find(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user_id"));
                userGroup.setUserId(currentUser.getId());
                userGroupFacade.remove(userGroup);
            }
        } catch(EJBException e)
        {
            statusMessage += "Something went wrong deleting the group";
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
            UserGroup foundUserGroup = userGroupFacade.findByIds(check.getId(), groupId);
            if(foundUserGroup == null)
            {
                statusMessage += usernameToDelete + " is not in the group";
                usernameToDelete = "";
                return "";
            }
            statusMessage = "UserGroup " + foundUserGroup.getId() + " deleted";
            userGroupFacade.deleteUserGroup(foundUserGroup);
            
            ArrayList<UserGroup> emptyCheck = (ArrayList<UserGroup>)userGroupFacade.findByGroupId(groupId);
            if(emptyCheck == null) {
                deleteGroup(groupId);
            }
            
            usernameToDelete = "";
        } catch(EJBException e)
        {
            usernameToDelete = "";
            statusMessage += "Something went wrong removing the user to the group";
            return "";
        }
        return "groups";
    }
    
    public ArrayList<Integer> getUsers(Integer groupId) {
        ArrayList<Integer> userIds = new ArrayList<Integer>();
        ArrayList<UserGroup> searchResult = new ArrayList<UserGroup>(userGroupFacade.findByGroupId(groupId));
        for(int i = 0; i < searchResult.size(); i++)
        {
            userIds.add(searchResult.get(i).getUserId());
        }
        return userIds;
    }
    
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
                groupsString += "Group ID: " + (searchResult.get(i).getGroupId()) + " User ID: " + searchResult.get(i).getUserId() + " Id: " + searchResult.get(i).getId() + "/";
            }
            
            UserGroup test = userGroupFacade.test();
            if(test != null)
            groupsString += ">>" + test.getId() + "<<";
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
    
    /**
     * Initialize global groups
     */
    private void initializeGlobalGroups() {
        // Groups all users have access to
        globalGroupNames.add("#Music");
        globalGroupNames.add("#For Sale");
        globalGroupNames.add("#Entertainment");
        
        // create a message stream for each group
        for (String grpName : globalGroupNames) {
            Collection<Message> collection = Collections.synchronizedList(new LinkedList<Message>());
            groupNameMessagesMap.put(grpName, collection);
            
            // check if the group already exists
            if (groupsFacade.findByGroupname(grpName) == null) {   
                // create the group and set the properties
                Groups g = new Groups();
                g.setGroupName(grpName);
                g.setMessageCollection(collection);
                // g.setFileCollection(insert file collection);
                groupsFacade.create(g);
            }
        }    
    }
}
