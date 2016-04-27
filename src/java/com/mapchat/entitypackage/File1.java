/*
 * Created by MapChat Development Team
 * Edited by Nathan Rosa, Corey McQuay
 * Last Modified: 2016.03.28
 * Copyright © 2016 MapChat Development Team. All rights reserved.
 */
package com.mapchat.entitypackage;

//Imports
import com.mapchat.managers.Constants;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Nathan Rosa
 * @author Corey McQuay
 * 
 * 4/25/16
 */

//Bean Properties
@Entity
@Table(name = "file")
@XmlRootElement
@NamedQueries({//Search Queries
    @NamedQuery(name = "File1.findAll", query = "SELECT f FROM File1 f"),
    @NamedQuery(name = "File1.findById", query = "SELECT f FROM File1 f WHERE f.id = :id"),
    @NamedQuery(name = "File1.findFilesByUserId", query = "SELECT f FROM File1 f WHERE f.userId.id = :userId"),
    @NamedQuery(name = "File1.findBySize", query = "SELECT f FROM File1 f WHERE f.size = :size"),
    @NamedQuery(name = "File1.findByFileLocation", query = "SELECT f FROM File1 f WHERE f.fileLocation = :fileLocation"),
    @NamedQuery(name = "File1.findByExtension", query = "SELECT f FROM File1 f WHERE f.extension = :extension"),
    @NamedQuery(name = "File1.findByTime", query = "SELECT f FROM File1 f WHERE f.time = :time"),
    @NamedQuery(name = "File1.findByGroupId", query = "SELECT f from File1 f WHERE f.groupId = :gid")})
/**
 * Class that represents the file in the database 
 */
public class File1 implements Serializable {
    //Properties of the file in the database
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "size")
    private int size;
    @Basic(optional = false)
    @Size(min = 1, max = 1000)
    @Column(name = "file_location")
    private String fileLocation;
    @Basic(optional = false)
    @Size(min = 1, max = 20)
    @Column(name = "extension")
    private String extension;
    @Basic(optional = false)
    @Column(name = "time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date time;
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Groups groupId;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private User userId;

    /**
     * Default Constructor
     */
    public File1() {
    }

    /**
     * Constructor that takes in the id
     * 
     * @param id id of the file
     */
    public File1(Integer id) {
        this.id = id;
    }
    
    /**
     * Constructor of the File which takes in more parameters 
     * @param id file's id in the database
     * @param size size of the file 
     * @param fileLocation where the file is located 
     * @param extension //extension of the file 
     * @param time /time where file is uploaded
     */
    public File1(Integer id, int size, String fileLocation, String extension, Date time) {
        //Sets globals to what gets passed in
        this.id = id;
        this.size = size;
        this.fileLocation = fileLocation;
        this.extension = extension;
        this.time = time;
    }
    
        // This method is added to the generated code
    /**
     * Constructor that takes in the extension and ID of the file
     * @param extension files extension
     * @param id file id
     */
    public File1(String extension, User id) 
    {   //Sets the globals to get passed it
        this.extension = extension;
        userId = id;
    }

    /**
     * Constructor that takes in the extension, used id, and group id
     * @param extension extension of file
     * @param uid user id 
     * @param gid group id
     */
    public File1(String extension, User uid, Groups gid) {
        //Takes the globals and sets them to what gets passed in
        this.extension = extension;
        userId = uid;
        groupId = gid;
    }
    
    /**
     * Getter for the file's id
     * @return file's current id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Setter for the file's id
     * @param id new file id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Getter for the size global variable
     * @return size of the file
     */
    public int getSize() {
        return size;
    }

    /**
     * Setter for the size method 
     * @param size new size of the file
     */
    public void setSize(int size) {
        this.size = size;
    }
    
    /**
     * Getter for the file location
     * @return the file location
     */
    public String getFileLocation() {
        return fileLocation;
    }

    /**
     * Setter for the file location
     * @param fileLocation the new file location
     */
    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    /**
     * Gets the extension of the file
     * @return the extension of the file
     */
    public String getExtension() {
        return extension;
    }

    /**
     * Setter of the extension
     * @param extension the new extension of the file
     */
    public void setExtension(String extension) {
        this.extension = extension;
    }

    /**
     * Getter of the time of the file
     * @return the time of the file
     */
    public Date getTime() {
        return time;
    }

    /**
     * Setter of the time of the location
     * @param time The time of the location
     */
    public void setTime(Date time) {
        this.time = time;
    }

    /**
     * Gets the group id of the file
     * @return Group Id of the file
     */
    public Groups getGroupId() {
        return groupId;
    }

    /**
     * Sets the group id of the file
     * @param groupId new group id of the file
     */
    public void setGroupId(Groups groupId) {
        this.groupId = groupId;
    }

    /**
     * Gets the user id of the file
     * @return the user id of the file
     */
    public User getUserId() {
        return userId;
    }

    /**
     * Sets the new user id of the file
     * @param userId userId of the file
     */
    public void setUserId(User userId) {
        this.userId = userId;
    }

    /**
     * Represent the hashcode of the file object
     * @return int of the hash of the file 
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    /**
     * Override of the equals method of the file
     * @param object the comparable object
     * @return true if the same, false otherwise.
     */
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof File1)) {
            return false;
        }
        File1 other = (File1) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    /**
     * Overriden implementation of the to string method of a file
     * @return 
     */
    @Override
    public String toString() {
        return "com.mapchat.entitypackage.File[ id=" + id + " ]";
    }
    
    /**
     * Gets the file path of file in a string.
     * @return The file path of the file name 
     */
    public String getFilePath() {
        return Constants.ROOT_DIRECTORY + getFilename();
    }

    /**
     * Sting representation of the file name
     * @return the string of the file name
     */
    public String getFilename() {
        return getId() + "." + getExtension();
    }
    
    /**
     * String representation of the thumbnail name
     * @return The string representation of thumbnailname
     */
    public String getThumbnailName() {
        return getId() + "_thumbnail." + getExtension();
    }    
    
    /**
     * String name of the icon
     * @return  The string representation of the icon name
     */
    public String getIconName() {
        return getId() + "_icon." + getExtension();
    }    
    
    /**
     * The thumbnail path string
     * @return the thumbnail path in a string
     */
    public String getThumbnailFilePath() {
        return Constants.ROOT_DIRECTORY + getThumbnailName();
    }
    
    /**
     * The icon file path in a string.
     * @return String representation of the icon file path
     */
    public String getIconFilePath() {
        return Constants.ROOT_DIRECTORY + getIconName();
    }
}
