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
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
    private Groups currentGroup;
    private Set<Groups> allGroups;
    private Set<Groups> nonGlobalGroups;        
    private Set<Groups> globalGroups; // List of global groups
    private Map<Groups, Collection> groupMessageMap; // Chatroom data structure of <group name, list of messages>
    // Groups all users have access to
    private ArrayList<String> globalgrps = new ArrayList();
        
    @ManagedProperty(value="#{profileViewManager}")
    private ProfileViewManager profileViewManager;
    @EJB
    private GroupsFacade groupsFacade;
  
    @EJB
    private UserGroupFacade userGroupFacade;
    
    @EJB
    private UserFacade usersFacade;
    
    public GroupManager() {
    }
    
    @PostConstruct
    public void init() {
        nonGlobalGroups = Collections.synchronizedSet(new HashSet<Groups>());
        groupMessageMap = 
            Collections.synchronizedMap(new HashMap<Groups, Collection>());
        
            initializeGlobalGroups();
            initializeNonGlobalGroups();
        
    }

    public ProfileViewManager getProfileViewManager() {
        return profileViewManager;
    }

    public void setProfileViewManager(ProfileViewManager profileViewManager) {
        this.profileViewManager = profileViewManager;
    }   

    public ArrayList<String> getGlobalgrps() {
        return globalgrps;
    }

    public void setGlobalgrps(ArrayList<String> globalgrps) {
        this.globalgrps = globalgrps;
    }
    
    public Groups getCurrentGroup() {
        if (currentGroup == null) return null;
        System.out.println("current group: " + currentGroup.getGroupName());
        return currentGroup;
    }

    public void setCurrentGroup(Groups currentGroup) {
        this.currentGroup = currentGroup;
    }

    public Set<Groups> getNonGlobalGroups() {
        List<UserGroup> usergroups = userGroupFacade.findByUserId(profileViewManager.getLoggedInUser().getId());
        if (usergroups == null) 
        {
            return null;
        }
        
        for (UserGroup ug : usergroups) {
            String name = groupsFacade.getGroup(ug.getGroupId()).getGroupName();
            nonGlobalGroups.add(groupsFacade.getGroup(ug.getGroupId()));
            System.out.println("group name: " + name);
        }
        return nonGlobalGroups;
    }

    public void setNonGlobalGroups(Set<Groups> nonGlobalGroups) {
        this.nonGlobalGroups = nonGlobalGroups;
    }    
    
    
    public Set<Groups> getGlobalGroups() {
        globalGroups = groupMessageMap.keySet();
        return globalGroups;
    }

    public void setGlobalGroups(Set<Groups> globalGroups) {
        this.globalGroups = globalGroups;
    }
    
    public Set<Groups> getAllGroups() {
         allGroups = groupMessageMap.keySet();
        return allGroups;
    }

    public void setAllGroups(Set<Groups> allGroups) {
        this.allGroups = allGroups;
    }
    
    public Set<Groups> getAvailableChatrooms() {
        return globalGroups;
    }
    
    
    public List<Message> getMessagesByChatroom(Groups chatroomName) {
        return (List<Message>)groupMessageMap.get(chatroomName);
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
        //check the group name entered
        if(groupNameToCreate == null || groupNameToCreate.equals(""))
        {
            statusMessage += "Group name entered is empty";
            groupNameToCreate = "";
            return "";
        }
        Groups grp = groupsFacade.findByGroupname(groupNameToCreate);
        if (grp == null) {
            // the group doesnt exist, so create it
            Collection<Message> collection = Collections.synchronizedList(new LinkedList<Message>());
            Groups g = new Groups();
            g.setGroupName(groupNameToCreate);
            g.setMessageCollection(collection);
            
            // add to the database
            groupsFacade.create(g);
            
            // add to the map
            groupMessageMap.put(g, collection);
            
            // create the user group to link the user to the group
            UserGroup userGroup = new UserGroup();
            userGroup.setGroupId(g.getId());
            currentUser = usersFacade.find(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user_id"));
            userGroup.setUserId(currentUser.getId());
            userGroupFacade.create(userGroup);
        }
        else {
            // its already created!
            statusMessage += "Group already exists!";
        }
        groupNameToCreate = "";
        return "";
        /*
        statusMessage = "";
        try
        {
            //check the group name entered
            if(groupNameToCreate == null || groupNameToCreate.equals(""))
            {
                statusMessage += "Group name entered is empty";
                groupNameToCreate = "";
                return "";
            }
            //Check to see if the group already exists
            Groups check = groupsFacade.findByGroupname(groupNameToCreate);
            if(check == null)
            {
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
                // add to the map
                Collection<Message> collection = Collections.synchronizedList(new LinkedList<Message>());
                groupMessageMap.put(foundGroup, collection);
                allGroups.add(foundGroup);
                //Uncomment next line when other functionalities work to work on this one
                //currentGroup = foundGroup;
            }
            else
            {
                statusMessage += groupNameToCreate + " already exists";
                groupNameToCreate = "";
                return "";
            }
        } catch(EJBException e)
        {
            groupNameToCreate = "";
            statusMessage += "Something went wrong creating the group";
            return "";
        }
        return "";
        */
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
        return "";
    }
    
    public String addUser() {
        statusMessage = "";
        if(usernameToAdd == null || usernameToAdd.trim().equals(""))
        {
            statusMessage += "No username entered";
            usernameToAdd = "";
            return "";
        }
        //Check the current group, if there is one
        if(currentGroup == null)
        {
            statusMessage += "No group is selected";
            usernameToAdd = "";
            return "";
        }
        else if(currentGroup.getId() == null || currentGroup.getGroupName() == null)
        {
            statusMessage += "There is something wrong with the current group selected";
            usernameToAdd = "";
            return "";   
        }
        
        Integer groupId = currentGroup.getId();
        Groups publicGroup = groupsFacade.findById(groupId);
        if(publicGroup == null)
        {
            statusMessage += "The current group does not exist";
            usernameToAdd = "";
            return "";
        }
        else if(globalgrps.contains(publicGroup.getGroupName()))
        {
            statusMessage += "Everyone is already a part of a global group";
            usernameToAdd = "";
            return "";
        }
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
        return "";
    }
    
    public String removeUser() {
        statusMessage = "";
        if(usernameToDelete == null || usernameToDelete.trim().equals(""))
        {
            statusMessage += "No username entered";
            usernameToDelete = "";
            return "";
        }
        //Check the current group, if there is one
        if(currentGroup == null)
        {
            statusMessage += "No group is selected";
            usernameToDelete = "";
            return "";
        }
        else if(currentGroup.getId() == null || currentGroup.getGroupName() == null)
        {
            statusMessage += "There is something wrong with the current group selected";
            usernameToDelete = "";
            return "";   
        }
        Integer groupId = currentGroup.getId();
        Groups publicGroup = groupsFacade.findById(groupId);
        if(publicGroup == null)
        {
            statusMessage += "The current group does not exist";
            usernameToDelete = "";
            return "";
        }
        else if(globalgrps.contains(publicGroup.getGroupName()))
        {
            statusMessage += "Cannot remove someone from a global group";
            usernameToDelete = "";
            return "";
        }
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
        return "";
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
        if(globalgrps == null || globalgrps.isEmpty())
        {
            globalgrps.add("#Music");
            globalgrps.add("#For Sale");
            globalgrps.add("#Entertainment");
        }
        // create a message stream for each group
        for (String grpName : globalgrps) {
            
            Groups grp = groupsFacade.findByGroupname(grpName);
            
            // if the group doesnt exist
            if (grp == null) {  
                Collection<Message> collection = Collections.synchronizedList(new LinkedList<Message>());

                // create the group and set the properties
                Groups g = new Groups();
                g.setGroupName(grpName);
                g.setMessageCollection(collection);
                // g.setFileCollection(insert file collection);
                
                // add to the database
                groupsFacade.create(g);
                
                // add to the map
                groupMessageMap.put(g, collection);
                
            }
            // if the group exists
            else {
                groupMessageMap.put(grp, grp.getMessageCollection());
            }
        }    
    }
    
    /**
     * Initialize nonglobal groups
     */
    private void initializeNonGlobalGroups() {
        // TODO
        
        // 1. retrieve all groups the user is in
        // 2. loop through each group
        //      a. groupMessageMap.put(grp, grp.getMessageCollection());
        
        ArrayList<String> nonglobalGroupNames = new ArrayList();
        User user = usersFacade.find(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user_id"));
        List<UserGroup> groupsUserIsIn = userGroupFacade.findByUserId(user.getId());
        if(groupsUserIsIn == null)
            groupsUserIsIn = new ArrayList();
        for(UserGroup usergroup : groupsUserIsIn)
        {
            Groups found = groupsFacade.findById(usergroup.getGroupId());
            nonglobalGroupNames.add(found.getGroupName());
        }
        
        // create a message stream for each group
        for (String grpName : nonglobalGroupNames) {
            
            Groups grp = groupsFacade.findByGroupname(grpName);
            
            // if the group doesnt exist
            if (grp == null) {  
                Collection<Message> collection = Collections.synchronizedList(new LinkedList<Message>());

                // create the group and set the properties
                Groups g = new Groups();
                g.setGroupName(grpName);
                g.setMessageCollection(collection);
                // g.setFileCollection(insert file collection);
                
                // add to the database
                groupsFacade.create(g);
                
                // add to the map
                groupMessageMap.put(g, collection);
                
                // create the user group to link the user to the group
                UserGroup userGroup = new UserGroup();
                userGroup.setGroupId(g.getId());
                currentUser = usersFacade.find(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user_id"));
                userGroup.setUserId(currentUser.getId());
                userGroupFacade.create(userGroup);
                
            }
            // if the group exists
            else {
                groupMessageMap.put(grp, grp.getMessageCollection());
            }
        }
    }
        
}
