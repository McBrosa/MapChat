/*
 * Created by Nathan Rosa on 2016.03.22  * 
 * Copyright © 2016 Nathan Rosa. All rights reserved. * 
 */
package com.mapchat.entitypackage;

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
 * @author Nathan
 */
@Entity
@Table(name = "file")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "File1.findAll", query = "SELECT f FROM File1 f"),
    @NamedQuery(name = "File1.findById", query = "SELECT f FROM File1 f WHERE f.id = :id"),
    @NamedQuery(name = "File1.findFilesByUserId", query = "SELECT f FROM File1 f WHERE f.userId.id = :userId"),
    @NamedQuery(name = "File1.findBySize", query = "SELECT f FROM File1 f WHERE f.size = :size"),
    @NamedQuery(name = "File1.findByFileLocation", query = "SELECT f FROM File1 f WHERE f.fileLocation = :fileLocation"),
    @NamedQuery(name = "File1.findByExtension", query = "SELECT f FROM File1 f WHERE f.extension = :extension"),
    @NamedQuery(name = "File1.findByTime", query = "SELECT f FROM File1 f WHERE f.time = :time")})
public class File1 implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "size")
    private int size;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1000)
    @Column(name = "file_location")
    private String fileLocation;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "extension")
    private String extension;
    @Basic(optional = false)
    @NotNull
    @Column(name = "time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date time;
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Group1 groupId;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private User userId;

    public File1() {
    }

    public File1(Integer id) {
        this.id = id;
    }

    public File1(Integer id, int size, String fileLocation, String extension, Date time) {
        this.id = id;
        this.size = size;
        this.fileLocation = fileLocation;
        this.extension = extension;
        this.time = time;
    }
    
    // This method is added to the generated code
    public File1(String extension, User id) {
        this.extension = extension;
        userId = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Group1 getGroupId() {
        return groupId;
    }

    public void setGroupId(Group1 groupId) {
        this.groupId = groupId;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

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

    @Override
    public String toString() {
        return "com.mapchat.entitypackage.File[ id=" + id + " ]";
    }
    
    public String getFilePath() {
        return Constants.ROOT_DIRECTORY + getFilename();
    }

    public String getFilename() {
        return getId() + "." + getExtension();
    }
    
    public String getThumbnailName() {
        return getId() + "_thumbnail." + getExtension();
    }
    
    public String getThumbnailFilePath() {
        return Constants.ROOT_DIRECTORY + getThumbnailName();
    }
}
