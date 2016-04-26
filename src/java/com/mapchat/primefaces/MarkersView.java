/**
 * Created by MapChat Development Team
 * Edited by Nate Rosa
 * Last Modified: 2016.04.25
 * Copyright © 2016 MapChat Development Team. All rights reserved.
 */
package com.mapchat.primefaces;

import com.mapchat.entitypackage.File1;
import com.mapchat.entitypackage.User;
import com.mapchat.entitypackage.UserGroup;
import com.mapchat.managers.GroupManager;
import com.mapchat.managers.MessageBean;
import com.mapchat.sessionbeanpackage.File1Facade;
import com.mapchat.sessionbeanpackage.UserFacade;
import com.mapchat.sessionbeanpackage.UserGroupFacade;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
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
@SessionScoped
public class MarkersView implements Serializable {
    
    private MapModel simpleModel;
    /* Maps a Marker object to a user id */
    private Map<String, Integer> markerMap;
    private Marker marker;
    private String markerInfo;
    /* API key for OpenWeather API */
    private final static String API_KEY = "a9a8c0d15d89a97605ba38615fdd436d";
    private String weatherInfo;
    private User markerUser;

    @EJB
    private UserFacade userFacade;
    
    @EJB
    private File1Facade fileFacade;
    
    @EJB
    private UserGroupFacade userGroupFacade;
  
    /* Access the groupManager bean */
    @ManagedProperty(value="#{groupManager}")
    private GroupManager groupManager;
    
    /* Access the messageBean  bean*/
    @ManagedProperty(value="#{messageBean}")
    private MessageBean messageBean;

    /* Get the messageBean */
    public MessageBean getMessageBean() {
        return messageBean;
    }

    /* Set the messageBean */
    public void setMessageBean(MessageBean messageBean) {
        this.messageBean = messageBean;
    }
    
    /* Initialize the GMap and place the logged in user's location on the map */
    @PostConstruct
    public void init() {
        simpleModel = new DefaultMapModel();
        updateLocations();
    }
    
    /**
     * Get the user and the users in the current group's locations.
     */
    private void updateLocations()
    {      
        /* Get the current user loggeg in */
        User user = userFacade.find(FacesContext.getCurrentInstance().
                getExternalContext().getSessionMap().get("user_id"));
        markerMap = new HashMap<>();
        /* The users location */
        LatLng coord1 = new LatLng(user.getLocationX(), user.getLocationY());
        /* Create the marker setting the location, title, data, and icon image */
        Marker currentMarker = new Marker(coord1, user.getUsername(), user, userPhotoLocation(user));
        /* Map the Marker to a specific User */
        markerMap.put(currentMarker.getData().toString(), user.getId());
        /* Add the Marker to the Map */
        simpleModel.addOverlay(currentMarker);
        
        /* If there is no current group selected or global groups is selected */
        if(messageBean.getCurrentGroup() != null && !groupManager.getGlobalgrps().contains(messageBean.getCurrentGroup().getGroupName()))
        {
            /* Gets the list of users in the currently selected group */
            List<UserGroup> usergroups = userGroupFacade.findByGroupId(messageBean.getCurrentGroup().getId()); 
            /* Get all the users in the group and display them on the map */
            usergroups.stream().forEach((ug) -> {
                User current = userFacade.getUser(ug.getUserId());
                Marker userMarker = new Marker(new LatLng(current.getLocationX(), current.getLocationY()), current.getUsername(), current, userPhotoLocation(current));
                markerMap.put(userMarker.getData().toString(),current.getId());
                simpleModel.addOverlay(userMarker);
            });
        }
    }
    
    /**
     * Remove all the markes from the map and update the locations 
     */
    public void refreshMap()
    {
        simpleModel.getMarkers().clear();
        markerMap.clear();
        updateLocations();
    }
  
    /**
     * Returns the map object
     * @return the map object
     */
    public MapModel getSimpleModel() {
        return simpleModel;
    }
    
    /**
     * On selecting a Marker Object the users Marker info is set.
     * @param event the action of a user clicking the marker
     */
    public void onMarkerSelect(OverlaySelectEvent event) {
        marker = (Marker) event.getOverlay();
        /* Get the user's id based of the selected Marker */
        Integer userId = markerMap.get(marker.getData().toString());
        setMarkerUser(userFacade.getUser(userId));
        /* Generate the marker's info to be displayed */
        markerInfo(getMarkerUser());
    }
    
    /**
     * Gets the Marker's info and sets it to the global variable
     * @param user the user's info to be generated
     */
    private void markerInfo(User user)
    {
        String userInfo = getWeather(user);
        markerInfo = userInfo;
    }

    /**
     * Return the current user represented by a Marker
     * @return markerUser
     */
    public User getMarkerUser() {
        if (markerUser == null) {
            markerUser = userFacade.getUser(markerMap.get(marker.getData().toString()));
        }
        return markerUser;
    }

    /**
     * Set the Marker representing the user to a new user
     * @param markerUser the new user to represent a marker
     */
    public void setMarkerUser(User markerUser) {
        this.markerUser = markerUser;
    }
    
    /**
     * Return the current weather info
     * @return  the current weather info
     */
    public String getWeatherInfo() {
        return weatherInfo;
    }

    /**
     * Set the weather info to new weather info
     * @param weatherInfo the new weather info
     */
    public void setWeatherInfo(String weatherInfo) {
        this.weatherInfo = weatherInfo;
    }
    
    /**
     * Returns the marker info to be displayed
     * @return marker info to be displayed
     */
    public String getMarkerInfo() {
        return markerInfo;
    }

    /**
     * Sets the marker info to new info
     * @param markerInfo new info
     */
    public void setMarkerInfo(String markerInfo) {
        this.markerInfo = markerInfo;
    }
    
    /**
     * Gets the current marker
     * @return the current marker
     */
    public Marker getMarker() {
        return marker;
    }
    
    /**
     * Sets the marker to a new marker
     * @param marker new marker
     */
    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    /**
     * Gets the markerMap between the Markers and Users
     * @return the markerMap
     */
    public Map<String, Integer> getMarkerMap() {
        return markerMap;
    }

    /**
     * Sets the markerMap to the new markerMap
     * @param markerMap new markerMap
     */
    public void setMarkerMap(Map<String, Integer> markerMap) {
        this.markerMap = markerMap;
    }
    
    /**
     * Returns the full file path to where the user photos are stored.
     * @param user the user's photo location we want to get
     * @return the photo location path
     */
    public String userPhotoLocation(User user) {
        List<File1> fileList = fileFacade.findFilesByUserID(user.getId());
        if (fileList.isEmpty()) {
            return "FileStorageLocation/defaultUserPhoto_icon.png";
        }
        return "FileStorageLocation/" + fileList.get(0).getIconName();
    }

    /**
     * Get the groupManager
     * @return the groupManager
     */
    public GroupManager getGroupManager() {
        return groupManager;
    }

    /**
     * Set the groupManager
     * @param groupManager  the new groupManager
     */
    public void setGroupManager(GroupManager groupManager) {
        this.groupManager = groupManager;
    }
    
    /**
     * Get the current weather for a user
     * @param user the user we want to the weather for
     * @return the weather string formatted for HTML display
     */
    private String getWeather(User user)
    {
        /* The openweather API url we pass the location too */
        String requestUrl = (String) "http://api.openweathermap.org/data/2.5/weather?lat="+ Double.toString(user.getLocationX()) +"&lon="+ Double.toString(user.getLocationY()) +"&appid="+ API_KEY;
        /* The  openweather url to get icons */
        String iconUrl = "http://openweathermap.org/img/w/";
        URL url = null;
        String weatherStr = "";
        try {
            /* Try requesting the Json weather info from the requestUrl */
            url = new URL(requestUrl);
        } catch (MalformedURLException ex) {
            Logger.getLogger(MarkersView.class.getName()).log(Level.SEVERE, null, ex);
        }
        try (InputStream is = url.openStream();
            /* Get the response from the server and read in a json*/
            JsonReader rdr = Json.createReader(is)) {

            /* Parse through the data and create weather string that will be formatted in HTML */
            JsonObject obj = rdr.readObject();
            JsonArray weather = obj.getJsonArray("weather");
            /* Get the city name near location */
            weatherStr += "<b>" + obj.getJsonString("name").getString().replaceAll("\"", "") + "</b><br />";
            /* Get the icon to represent the weather */
            String iconType = weather.getJsonObject(0).get("icon").toString().replaceAll("\"", "") + ".png";
            /* Get the condition description for the location */
            String condition = weather.getJsonObject(0).get("description").toString().replaceAll("\"", "");
            String icon = "<img src=\""+ iconUrl + iconType +"\"><br />";
            weatherStr += icon;
            /* Get the temp and covert from Kelvin to Fahrenheit */
            double fTemp = kToF(Double.parseDouble(obj.getJsonObject("main").get("temp").toString()));
            weatherStr += "" + Double.toString(fTemp) + "&#8457;<br />";
            weatherStr += "" + firstLetterToUpperCase(condition);
       } catch (IOException ex) {
            Logger.getLogger(MarkersView.class.getName()).log(Level.SEVERE, null, ex);
        }
        return weatherStr;
    }
    
    /* Covert from Kelvin to Fahrenheit */
    private double kToF(double kelvin)
    {
        return Math.round((kelvin * 1.8 - 459.67) * 100)/100;
    }
    
    /* Capitalize the first letter of each word like so: This Is An Example */
    private String firstLetterToUpperCase(String str)
    {
        StringBuffer stringbf = new StringBuffer();
        Matcher m = Pattern.compile("([a-z])([a-z]*)",
        Pattern.CASE_INSENSITIVE).matcher(str);
        while (m.find()) {
           m.appendReplacement(stringbf, 
           m.group(1).toUpperCase() + m.group(2).toLowerCase());
        }
        return (m.appendTail(stringbf).toString());
    }
    
    /* Just get the user's photo */
    public String userPhoto() {
        List<File1> fileList = fileFacade.findFilesByUserID(getMarkerUser().getId());
        if (fileList.isEmpty()) {
            return "defaultUserPhoto.png";
        }
        return fileList.get(0).getThumbnailName();
    }
}
