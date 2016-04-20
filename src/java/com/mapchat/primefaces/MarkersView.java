/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mapchat.primefaces;

import com.mapchat.entitypackage.User;
import com.mapchat.managers.AccountManager;
import com.mapchat.sessionbeanpackage.UserFacade;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
 
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
 
@ManagedBean
public class MarkersView implements Serializable {
    
    private MapModel simpleModel;

    @EJB
    private UserFacade userFacade;
  
    @PostConstruct
    public void init() {
        simpleModel = new DefaultMapModel();
        User user = userFacade.find(FacesContext.getCurrentInstance().
                getExternalContext().getSessionMap().get("user_id"));
          
        //Shared coordinates
        LatLng coord1 = new LatLng(user.getLocationX(), user.getLocationY());
        
        //Basic marker
        //simpleModel.addOverlay(new Marker(coord1, user.getFirstName(), null, new ImageIcon("resources/images/mapchat-icon.ico").toString()));
        simpleModel.addOverlay(new Marker(coord1, user.getFirstName()));
    }
  
    public MapModel getSimpleModel() {
        return simpleModel;
    }
}
