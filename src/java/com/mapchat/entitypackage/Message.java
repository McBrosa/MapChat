/*
 * Created by Nathan Rosa on 2016.03.22  * 
 * Copyright © 2016 Nathan Rosa. All rights reserved. * 
 */
package com.mapchat.entitypackage;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
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
 * This is the entity class for the Message object
 * @author Sean Arcayan
 */
@Entity
@Table(name = "message")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Message.findAll", query = "SELECT m FROM Message m"),
    @NamedQuery(name = "Message.findById", query = "SELECT m FROM Message m WHERE m.id = :id"),
    @NamedQuery(name = "Message.findByTime", query = "SELECT m FROM Message m WHERE m.time = :time"),
    @NamedQuery(name = "Message.findByGroupId", query = "SELECT m from Message m WHERE m.groupId = :gid")})
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "message")
    private String message;
    @Basic(optional = false)
    @NotNull
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
     * Constructor
     */
    public Message() {
    }

    /**
     * Constructor
     * @param id 
     */
    public Message(Integer id) {
        this.id = id;
    }

    /**
     * Constructor
     * @param id the message id
     * @param message the message body
     * @param time the timestamp of the message
     */
    public Message(Integer id, String message, Date time) {
        this.id = id;
        this.message = message;
        this.time = time;
    }

    /**
     * Get the message id
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Set the message id
     * @param id 
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Get the message body
     * @return message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Set the message body
     * @param message 
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Get the time the message was sent
     * @return the time
     */
    public Date getTime() {
        return time;
    }

    /**
     * Set the time the message was sent
     * @param time 
     */
    public void setTime(Date time) {
        this.time = time;
    }

    /**
     * Get the group that the message is in
     * @return groupId the group
     */
    public Groups getGroupId() {
        return groupId;
    }

    /**
     * Set the group that the message is in
     * @param groupId 
     */
    public void setGroupId(Groups groupId) {
        this.groupId = groupId;
    }

    /**
     * The user that wrote the message
     * @return userId
     */
    public User getUserId() {
        return userId;
    }

    /**
     * Set the user that wrote the message
     * @param userId 
     */
    public void setUserId(User userId) {
        this.userId = userId;
    }

    @Override
    /**
     * Create a hash code for the message
     */
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    /**
     * Create a hashcode for the message
     */
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Message)) {
            return false;
        }
        Message other = (Message) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    /**
     * Convert the message to a string. This is used when the message is printed
     * out on the dashboard (Chat.xhtml)
     * 
     * @return the string representation of the message
     */
    public String toString() {
        StringBuilder sb = new StringBuilder("[ ");
        sb.append(userId.getFirstName());
        sb.append(" ] : ");
        sb.append(message);
        return sb.toString();
    }    
} // end class
