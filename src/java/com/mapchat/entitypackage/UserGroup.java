/*
 * Created by MapChat Development Team
 * Edited by Nathan Rosa, Anthony Barbee
 * Last Modified: 2016.03.28
 * Copyright © 2016 MapChat Development Team. All rights reserved.
 */
package com.mapchat.entitypackage;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Nathan Rosa
 * @author Anthony Barbee
 */
@Entity
@Table(name = "user_group")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UserGroup.findAll", query = "SELECT u FROM UserGroup u"),
    @NamedQuery(name = "UserGroup.findById", query = "SELECT u FROM UserGroup u WHERE u.id = :id"),
    @NamedQuery(name = "UserGroup.findByUserId", query = "SELECT u FROM UserGroup u WHERE u.userId = :userId"),
    @NamedQuery(name = "UserGroup.findByGroupId", query = "SELECT u FROM UserGroup u WHERE u.groupId = :groupId"),
    @NamedQuery(name = "UserGroup.findByIds", query = "SELECT u FROM UserGroup u WHERE u.userId = :userId AND u.groupId = :groupId")})
public class UserGroup implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "user_id")
    private Integer userId;
    /*@JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToMany(targetEntity=User.class)
    private Collection<User> user;*/
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "group_id")
    private Integer groupId;
    /*@JoinColumn(name = "group_id", referencedColumnName = "id")
    @ManyToMany(targetEntity=Groups.class)
    private Collection<Groups> groups;*/

    
    /**
     * UserGroup Constructor (Empty)
     */
    public UserGroup() {
    }

    /**
     * Getter method for id tag of UserGroup
     * @return id of UserGroup
     */
    public Integer getId() {
        return id;
    }

    /**
     * Setter method for id tag of UserGroup
     * @param id - UserGroup's id
     */
    public void setId(Integer id) {
        this.id = id;
    }
    
    /**
     * Constructor for a UserGroup with an id tag
     * @param userId - userId tag of UserGroup
     */
    public UserGroup(Integer userId) {
        this.userId = userId;
    }

    /**
     * Getter method for the UserGroup's userId
     * @return UserGroup's userId
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * Setter method for the UserGroup's userId
     * @param userId - UserGroup's set userId
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    
    
    /**
     * Getter method for the UserGroup's groupId
     * @return UserGroup's userId
     */
    public Integer getGroupId() {
        return groupId;
    }

    /**
     * Getter method for the UserGroup's groupId
     * @param groupsId - the UserGroup's set groudId
     */
    public void setGroupId(Integer groupsId) {
        this.groupId = groupsId;
    }

    /**
     * HashCode function for indexing UserGroups
     * @return hashed UserGroup's id
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    /**
     * Method for comparing two UserGroups together/ a UserGroup to an Object
     * @param obj - object being compared to
     * @return T/F (depending)
     */
    @Override
    public boolean equals(Object obj) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(obj instanceof UserGroup)) {
            return false;
        }
        UserGroup other = (UserGroup) obj;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    /**
     * Method for obtaining a String representation of a UserGroup
     * @return String UserGroup
     */
    @Override
    public String toString() {
        return "com.mapchat.entitypackage.UserGroup[ id=" + id + " ]";
    }
    
}
