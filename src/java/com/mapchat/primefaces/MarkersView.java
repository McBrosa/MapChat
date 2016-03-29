/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mapchat.primefaces;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
 
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

import javax.swing.ImageIcon;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import static java.awt.image.BufferedImage.*;
import java.awt.RenderingHints;


import java.io.IOException;
import java.io.InputStream;
 
@ManagedBean
public class MarkersView implements Serializable {
    
    private MapModel simpleModel;
    private ImageIcon foxIcon;
    private final static int WIDTH = 50;
    private final static int HEIGHT = 50;
  
    @PostConstruct
    public void init() {
        simpleModel = new DefaultMapModel();
          
        //Shared coordinates
        LatLng coord1 = new LatLng(36.879466, 30.667648);
        LatLng coord2 = new LatLng(36.883707, 30.689216);
        LatLng coord3 = new LatLng(36.879703, 30.706707);
        LatLng coord4 = new LatLng(36.885233, 30.702323);
        
//        foxIcon = createImageIcon("mapchat-icon.ico", "USER");
//        Image image = foxIcon.getImage(); // transform it 
//        Image newimg = image.getScaledInstance(WIDTH, HEIGHT,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
//        foxIcon = new ImageIcon(newimg);  // transform it back
        //Basic marker
        simpleModel.addOverlay(new Marker(coord1, "Konyaalti", null, new ImageIcon("resources/images/mapchat-icon.ico").toString()));
        simpleModel.addOverlay(new Marker(coord2, "Ataturk Parki"));
        simpleModel.addOverlay(new Marker(coord3, "Karaalioglu Parki"));
        simpleModel.addOverlay(new Marker(coord4, "Kaleici"));
    }
  
    public MapModel getSimpleModel() {
        return simpleModel;
    }
    
    protected ImageIcon createImageIcon(String path, String description) 
    {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
}
