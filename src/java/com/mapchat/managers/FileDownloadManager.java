/*
 * Created by Sean Arcayan on 2016.04.21  * 
 * Copyright © 2016 Sean Arcayan. All rights reserved. * 
 */
package com.mapchat.managers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
 
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@ManagedBean
/**
 *
 * @author Sean
 */
public class FileDownloadManager {
    
    private StreamedContent file;
    
    @ManagedProperty(value="#{groupManager}")
    private GroupManager groupManager;

    public GroupManager getGroupManager() {
        return groupManager;
    }

    public void setGroupManager(GroupManager groupManager) {
        this.groupManager = groupManager;
    }
    
     
    public FileDownloadManager() {        
        
    }
 
    public StreamedContent getFile() {
        return file;
    }
    
    public void prepareDownload(String fileName) throws FileNotFoundException {
        String path = Constants.ROOT_DIRECTORY + groupManager.getCurrentGroup().getId() + "/" + fileName;
        InputStream stream = new FileInputStream(path);
        this.file = new DefaultStreamedContent(stream, getExtension(fileName), fileName);
    }
    
    
    /**
     * Retrieve the filename  from a file path
     * @param filename
     * @return the extension
     */
    private String getFileName(String fileName) {
        String[] tokens = fileName.split("\\.(?=[^\\.]+$)");
        return tokens[0];
    }
    
    /**
     * Retrieve the extension from a file path
     * @param filename
     * @return the extension
     */
    private String getExtension(String fileName) {
        String[] tokens = fileName.split("\\.(?=[^\\.]+$)");
        return tokens[1];
    }
    
}