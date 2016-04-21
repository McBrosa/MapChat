/*
 * Created by Nathan Rosa on 2016.03.22  * 
 * Copyright © 2016 Nathan Rosa. All rights reserved. * 
 */
package com.mapchat.managers;

import com.mapchat.entitypackage.File1;
import com.mapchat.entitypackage.Groups;
import com.mapchat.entitypackage.User;
import com.mapchat.sessionbeanpackage.File1Facade;
import com.mapchat.sessionbeanpackage.GroupsFacade;
import com.mapchat.sessionbeanpackage.UserFacade;
//import java.awt.AlphaComposite;
//import java.awt.Graphics2D;
//import java.awt.RenderingHints;
//import java.awt.geom.Ellipse2D;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.inject.Named;
import org.imgscalr.Scalr;
import org.primefaces.model.UploadedFile;

@Named(value = "fileManager")
@ManagedBean
@SessionScoped
public class FileManager implements Serializable{

    // Instance Variables (Properties)
    private UploadedFile file;
    
    
    private String message = "";
    
    /**
     * The instance variable 'userFacade' is annotated with the @EJB annotation.
     * This means that the GlassFish application server, at runtime, will inject in
     * this instance variable a reference to the @Stateless session bean UserFacade.
     */
    @EJB
    private UserFacade userFacade;

    @EJB
    private GroupsFacade groupsFacade;
    /**
     * The instance variable 'fileFacade' is annotated with the @EJB annotation.
     * This means that the GlassFish application server, at runtime, will inject in
 this instance variable a reference to the @Stateless session bean File1Facade.
     */
    @EJB
    private File1Facade fileFacade;

    public File1Facade getFileFacade() {
        return fileFacade;
    }

    public void setFileFacade(File1Facade fileFacade) {
        this.fileFacade = fileFacade;
    }

    // Returns the uploaded file
    public UploadedFile getFile() {
        return file;
    }

    // Obtains the uploaded file
    public void setFile(UploadedFile file) {
        this.file = file;
    }

    // Returns the message
    public String getMessage() {
        return message;
    }

    // Obtains the message
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * "Profile?faces-redirect=true" asks the web browser to display the
     * Profile.xhtml page and update the URL corresponding to that page.
     * @return Profile.xhtml or nothing
     */

    public String upload() {
        if (file.getSize() != 0) {
            copyFile(file);
            message = "";
            return "Profile?faces-redirect=true";
        } else {
            message = "You need to upload a file first!";
            return "";
        }
    }
    
    public String cancel() {
        message = "";
        return "Profile?faces-redirect=true";
    }

    public FacesMessage copyFile(UploadedFile file) {
        try {
            deletePhoto();
            
            InputStream in = file.getInputstream();
            
            File tempFile = inputStreamToFile(in, Constants.TEMP_FILE);
            in.close();

            FacesMessage resultMsg;

            String user_name = (String) FacesContext.getCurrentInstance()
                    .getExternalContext().getSessionMap().get("username");

            User user = userFacade.findByUsername(user_name);
            //Group1 group = groupFacade
            // Need to implement when groups are a thing
            // Insert photo record into database
//            String extension = file.getContentType();
//            extension = extension.startsWith("image/") ? extension.subSequence(6, extension.length()).toString() : "png";
            List<File1> fileList = fileFacade.findFilesByUserID(user.getId());
            if (!fileList.isEmpty()) {
                fileFacade.remove(fileList.get(0));
            }

            fileFacade.create(new File1("png", user));
            File1 photo = fileFacade.findFilesByUserID(user.getId()).get(0);
            in = file.getInputstream();
            File uploadedFile = inputStreamToFile(in, photo.getFilename());
            BufferedImage icon = ImageIO.read(uploadedFile);
            BufferedImage rounded = makeRoundedCorner(icon);
            File circle = new File(uploadedFile.getName().substring(0, uploadedFile.getName().lastIndexOf('.')) + ".png");
            ImageIO.write(rounded, "png", circle);
            photo.setExtension("png");
            saveThumbnail(circle, photo);
            resultMsg = new FacesMessage("Success!", "File Successfully Uploaded!");
            return resultMsg;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new FacesMessage("Upload failure!",
            "There was a problem reading the image file. Please try again with a new photo file.");
    }
    
    private BufferedImage makeRoundedCorner(BufferedImage image) {
        int w = image.getWidth();
        int h = image.getHeight();
        int s = 0; 
        if (w <= h) 
        {
            s = w;
        }
        else
        {
            s = h;
        }
        BufferedImage output = new BufferedImage(s, s, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2 = output.createGraphics();

        g2.fill(new Rectangle2D.Double(0, 0, s, s));
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OUT, 1.0f));
        g2.fill(new Ellipse2D.Double(0, 0, s, s));
        g2.drawImage(image, 0, 0, null);

        g2.dispose();

        return output;
    }

    /**
     * upload a file from the chat div in the dashboard
     * @param file
     */
    public void uploadFileToGroup(UploadedFile file, Groups grp) {
        if (file != null) {
            //File1 uploadedFile = new File1();
            copyFileGroup(file, grp);
            
        }
    }    
    
    public FacesMessage copyFileGroup(UploadedFile file, Groups grp) {
        try {
            deletePhoto();
            
            InputStream in = file.getInputstream();
            
            // create new directory if it doesnt exist
            new File(Constants.ROOT_DIRECTORY + "/" + grp.getId()).mkdirs();
            
            File tempFile = inputStreamToFile(in, grp.getId() + "/" + file.getFileName());
            in.close();

            FacesMessage resultMsg;

            String user_name = (String) FacesContext.getCurrentInstance()
                    .getExternalContext().getSessionMap().get("username");

            User user = userFacade.findByUsername(user_name);
            String extension = file.getContentType();

            fileFacade.create(new File1(extension, user, grp));
            
            resultMsg = new FacesMessage("Success!", "File Successfully Uploaded!");
            return resultMsg;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new FacesMessage("Upload failure!",
            "There was a problem reading the image file. Please try again with a new photo file.");
    }
    
    private File inputStreamToFile(InputStream inputStream, String childName)
            throws IOException {
        // Read in the series of bytes from the input stream
        byte[] buffer = new byte[inputStream.available()];
        inputStream.read(buffer);

        // Write the series of bytes on file.
        File targetFile = new File(Constants.ROOT_DIRECTORY /* TODO add grp id here */, childName);

        OutputStream outStream;
        outStream = new FileOutputStream(targetFile);
        outStream.write(buffer);
        outStream.close();

        // Save reference to the current image.
        return targetFile;
    }

    private void saveThumbnail(File inputFile, File1 inputPhoto) {
        try {
            BufferedImage original = ImageIO.read(inputFile);
            BufferedImage thumbnail = Scalr.resize(original, Constants.THUMBNAIL_SZ);
            BufferedImage icon = Scalr.resize(original, Constants.ICON_SZ);
            ImageIO.write(thumbnail, inputPhoto.getExtension(),
                new File(Constants.ROOT_DIRECTORY, inputPhoto.getThumbnailName()));
             ImageIO.write(icon, inputPhoto.getExtension(),
                new File(Constants.ROOT_DIRECTORY, inputPhoto.getIconName()));
        } catch (IOException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deletePhoto() {
        FacesMessage resultMsg;
        String user_name = (String) FacesContext.getCurrentInstance()
                .getExternalContext().getSessionMap().get("username");

        User user = userFacade.findByUsername(user_name);

        List<File1> fileList = fileFacade.findFilesByUserID(user.getId());
        if (fileList.isEmpty()) {
            resultMsg = new FacesMessage("Error", "You do not have a photo to delete.");
        } else {
            File1 photo = fileList.get(0);
            try {
                Files.deleteIfExists(Paths.get(photo.getFilePath()));
                Files.deleteIfExists(Paths.get(photo.getThumbnailFilePath()));
                
                Files.deleteIfExists(Paths.get(Constants.ROOT_DIRECTORY+"tmp_file"));
                 
                fileFacade.remove(photo);
            } catch (IOException ex) {
                Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
            }

            resultMsg = new FacesMessage("Success", "File successfully deleted!");
        }
        FacesContext.getCurrentInstance().addMessage(null, resultMsg);
    }
    
    public String filePath()
    {
        String user_name = (String) FacesContext.getCurrentInstance()
                    .getExternalContext().getSessionMap().get("username");

        User user = userFacade.findByUsername(user_name);
        return "FileStorageLocation/" + fileFacade.findFilesByUserID(user.getId()).get(0).getThumbnailName();    
    }
 }
