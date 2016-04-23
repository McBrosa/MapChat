/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mapchat.primefaces;

import com.mapchat.entitypackage.File1;
import com.mapchat.entitypackage.Groups;
import com.mapchat.entitypackage.User;
import com.mapchat.entitypackage.UserGroup;
import com.mapchat.managers.GroupManager;
import com.mapchat.sessionbeanpackage.File1Facade;
import com.mapchat.sessionbeanpackage.UserFacade;
import com.mapchat.sessionbeanpackage.UserGroupFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.map.OverlaySelectEvent;
 
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
 
@ManagedBean
@ViewScoped
public class MarkersView implements Serializable {
    
    private MapModel simpleModel;
    private Map<String, Integer> markerMap;
    private Marker marker;
    private String markerInfo;

    @EJB
    private UserFacade userFacade;
    
    @EJB
    private File1Facade fileFacade;
    
    @EJB
    private UserGroupFacade userGroupFacade;
  
    @ManagedProperty(value="#{groupManager}")
    private GroupManager groupManager;
    
    @PostConstruct
    public void init() {
        simpleModel = new DefaultMapModel();
        updateLocations();
    }
    
    private void updateLocations()
    {      
        User user = userFacade.find(FacesContext.getCurrentInstance().
                getExternalContext().getSessionMap().get("user_id"));
        markerMap = new HashMap<>();
        //The users location
        LatLng coord1 = new LatLng(user.getLocationX(), user.getLocationY());
        Marker currentMarker = new Marker(coord1, user.getUsername(), user, userPhotoLocation(user));
        markerMap.put(currentMarker.getData().toString(), user.getId());
        simpleModel.addOverlay(currentMarker);
        
        if(groupManager.getCurrentGroup() != null)
        {
            List<UserGroup> usergroups = userGroupFacade.findByGroupId(groupManager.getCurrentGroup().getId()); 
            // Get all the users in the group and display them on the map
            usergroups.stream().forEach((ug) -> {
                User current = userFacade.getUser(ug.getUserId());
                Marker userMarker = new Marker(new LatLng(current.getLocationX(), current.getLocationY()), current.getUsername(), current, userPhotoLocation(current));
                markerMap.put(userMarker.getData().toString(),current.getId());
                simpleModel.addOverlay(userMarker);
            });
        }
    }
    
    public void refreshMap()
    {
        simpleModel.getMarkers().clear();
        markerMap.clear();
        updateLocations();
    }
  
    public MapModel getSimpleModel() {
        return simpleModel;
    }
    
    public void onMarkerSelect(OverlaySelectEvent event) {
        marker = (Marker) event.getOverlay();
        Integer userId = markerMap.get(marker.getData().toString());
        User selectedUser = userFacade.getUser(userId);
        marker.setTitle(selectedUser.getUsername());
        marker.setData(selectedUser);
        markerInfo(selectedUser);
    }
    
    private void markerInfo(User user)
    {
        String userInfo = user.getUsername() + "<br />"
                + "email: " + user.getEmail() + "<br/>"
                + "phone: " + user.getPhone() + "<br />";
        markerInfo = userInfo;
    }

    public String getMarkerInfo() {
        return markerInfo;
    }

    public void setMarkerInfo(String markerInfo) {
        this.markerInfo = markerInfo;
    }
    
    public Marker getMarker() {
        return marker;
    }

    public Map<String, Integer> getMarkerMap() {
        return markerMap;
    }

    public void setMarkerMap(Map<String, Integer> markerMap) {
        this.markerMap = markerMap;
    }
    
    public String userPhotoLocation(User user) {
        List<File1> fileList = fileFacade.findFilesByUserID(user.getId());
        if (fileList.isEmpty()) {
            return "FileStorageLocation/defaultUserPhoto_icon.png";
        }
        return "FileStorageLocation/" + fileList.get(0).getIconName();
    }

    public GroupManager getGroupManager() {
        return groupManager;
    }

    public void setGroupManager(GroupManager groupManager) {
        this.groupManager = groupManager;
    }
    
}
