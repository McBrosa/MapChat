/*
 * Created by Nathan Rosa on 2016.03.22  * 
 * Copyright © 2016 Nathan Rosa. All rights reserved. * 
 */
package com.mapchat.entitypackage;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Nathan
 */
@Entity
@Table(name = "groups")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Groups.findAll", query = "SELECT g FROM Groups g"),
    @NamedQuery(name = "Groups.findById", query = "SELECT g FROM Groups g WHERE g.id = :id"),
    @NamedQuery(name = "Groups.findByGroupName", query = "SELECT g FROM Groups g WHERE g.groupName = :groupName")})
public class Groups implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "group_name")
    private String groupName;
    /*@ManyToMany(cascade = CascadeType.ALL, mappedBy = "groups")   TODO remove
    private Collection<UserGroup> userGroupCollection;*/
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "groupId")
    private Collection<File1> fileCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "groupId")
    private Collection<Message> messageCollection;

    /**
     * Base constructor
     */
    public Groups() {
    }

    /**
     * The constructor
     * @param id The group's id
     */
    public Groups(Integer id) {
        this.id = id;
    }

    /**
     * The constructor
     * @param id The group's id
     * @param groupName The group's name
     */
    public Groups(Integer id, String groupName) {
        this.id = id;
        this.groupName = groupName;
    }

    /**
     * Gets the group's id
     * @return The group's id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the group's id
     * @param id The group's id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets the group's name
     * @return THe group name
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * Sets the group name
     * @param groupName  The new group name
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    /*@XmlTransient
    public Collection<UserGroup> getUserGroupCollection() {
        return userGroupCollection;
    }

    public void setUserGroupCollection(Collection<UserGroup> userGroupCollection) {
        this.userGroupCollection = userGroupCollection;
    }*/

    /**
     * Gets the fileCollection
     * @return The fileCollection
     */
    @XmlTransient
    public Collection<File1> getFileCollection() {
        return fileCollection;
    }

    /**
     * Sets the fileCollection
     * @param fileCollection The new fileCollection
     */
    public void setFileCollection(Collection<File1> fileCollection) {
        this.fileCollection = fileCollection;
    }

    /**
     * Returns the messageCollection
     * @return The messageCollection
     */
    @XmlTransient
    public Collection<Message> getMessageCollection() {
        return messageCollection;
    }

    /**
     * Sets the messageCollection
     * @param messageCollection The new messageCollection
     */
    public void setMessageCollection(Collection<Message> messageCollection) {
        this.messageCollection = messageCollection;
    }

    /**
     * Returns the hash code of this Object
     * @return The hash code of this Oject
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    /**
     * Checks to see if two Groups objects are the same
     * @param object The other Groups object to compare
     * @return True if the two Groups have the same id value
     */
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Groups)) {
            return false;
        }
        Groups other = (Groups) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    /**
     * Returns the String version of the group, the group name
     * @return THe name fo the group
     */
    @Override
    public String toString() {
        return groupName;
    }
    
}
