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
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
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
    private final static String API_KEY = "a9a8c0d15d89a97605ba38615fdd436d";
    private String weatherInfo;

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
                + "phone: " + user.getPhone() + "<br />" +
                getWeather(user);
        markerInfo = userInfo;
    }

    public String getWeatherInfo() {
        return weatherInfo;
    }

    public void setWeatherInfo(String weatherInfo) {
        this.weatherInfo = weatherInfo;
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
    
    private String getWeather(User user)
    {
        String requestUrl = (String) "http://api.openweathermap.org/data/2.5/weather?lat="+ Double.toString(user.getLocationX()) +"&lon="+ Double.toString(user.getLocationY()) +"&appid="+ API_KEY;
        URL url = null;
        String weatherStr = "";
        try {
            url = new URL(requestUrl);
        } catch (MalformedURLException ex) {
            Logger.getLogger(MarkersView.class.getName()).log(Level.SEVERE, null, ex);
        }
        try (InputStream is = url.openStream();
            JsonReader rdr = Json.createReader(is)) {

            JsonObject obj = rdr.readObject();
            JsonArray weather = obj.getJsonArray("weather");
            weatherStr += "City: " + obj.getJsonString("name") + "<br />";
            //weatherStr += "Cond: " + weather.getString(2) + "<br />";
            double fTemp = kToF(Double.parseDouble(obj.getJsonObject("main").get("temp").toString()));
            weatherStr += "Temp: " + Double.toString(fTemp) + "&#8457;<br />";
       } catch (IOException ex) {
            Logger.getLogger(MarkersView.class.getName()).log(Level.SEVERE, null, ex);
        }
        return weatherStr;
    }
    
    private double kToF(double kelvin)
    {
        return Math.round((kelvin * 1.8 - 459.67) * 100)/100;
    }
}
