/*
 * Created by Anthony Barbee on 2016.04.19  * 
 * Copyright © 2016 Anthony Barbee. All rights reserved. * 
 */
package com.mapchat.primefaces;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
 
@ManagedBean
public class ImagesView {
     
    private List<String> images;
     
    @PostConstruct
    public void init() {
        images = new ArrayList();
        for (int i = 1; i <= 12; i++) {
            images.add("movie" + i + ".jpg");
        }
    }
 
    public List<String> getImages() {
        return images;
    }
}
