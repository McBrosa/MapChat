/**
 * Created by MapChat Development Team
 * Edited by Anthony Barbee
 * Last Modified: 2016.04.20
 * Copyright © 2016 MapChat Development Team. All rights reserved.
 */
package com.mapchat.primefaces;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
 
@ManagedBean
public class ImagesView {
     
    private List<String> images;
     /**
      * Initializes an array for the photo collage
      */
    @PostConstruct
    public void init() {
        images = new ArrayList();
        for (int i = 1; i <= 12; i++) {
            images.add("movie" + i + ".jpg");
        }
    }
    
    /**
     * Return the list of images
     * @return list of images
     */
    public List<String> getImages() {
        return images;
    }
}
