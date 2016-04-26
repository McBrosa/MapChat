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
 * The manager class for downloading files
 * @author Sean
 */
public class FileDownloadManager {
    
    // The file to download
    private StreamedContent file;
    
    @ManagedProperty(value="#{messageBean}")
    private MessageBean messageBean;

    /**
     * Constructor
     */
    public FileDownloadManager() {        
        
    }
    
    /**
     * Prepare the file download
     * @param fileName the name of the file to download
     * @throws FileNotFoundException 
     */
    public void prepareDownload(String fileName) throws FileNotFoundException {
        String path = Constants.ROOT_DIRECTORY + messageBean.getCurrentGroup().getId() + "/" + fileName;
        InputStream stream = new FileInputStream(path);
        this.file = new DefaultStreamedContent(stream, getExtension(fileName), fileName);
    }
    
    /**
     * Retrieve the file to download
     * @return file
     */
    public StreamedContent getFile() {
        return file;
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
    
    /**
     * Retrieve the message bean
     * @return messageBean
     */
    public MessageBean getMessageBean() {
        return messageBean;
    }

    /**
     * Set the message bean
     * @param messageBean 
     */
    public void setMessageBean(MessageBean messageBean) {
        this.messageBean = messageBean;
    }
}