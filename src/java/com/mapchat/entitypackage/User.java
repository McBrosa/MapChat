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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * This is the class for the User entity
 * @author Nathan
 */
@Entity
@Table(name = "user")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u"),
    @NamedQuery(name = "User.findById", query = "SELECT u FROM User u WHERE u.id = :id"),
    @NamedQuery(name = "User.findByUsername", query = "SELECT u FROM User u WHERE u.username = :username"),
    @NamedQuery(name = "User.findByPassword", query = "SELECT u FROM User u WHERE u.password = :password"),
    @NamedQuery(name = "User.findByFirstName", query = "SELECT u FROM User u WHERE u.firstName = :firstName"),
    @NamedQuery(name = "User.findByMiddleName", query = "SELECT u FROM User u WHERE u.middleName = :middleName"),
    @NamedQuery(name = "User.findByLastName", query = "SELECT u FROM User u WHERE u.lastName = :lastName"),
    @NamedQuery(name = "User.findBySecurityQuestion", query = "SELECT u FROM User u WHERE u.securityQuestion = :securityQuestion"),
    @NamedQuery(name = "User.findBySecurityAnswer", query = "SELECT u FROM User u WHERE u.securityAnswer = :securityAnswer"),
    @NamedQuery(name = "User.findByLocationX", query = "SELECT u FROM User u WHERE u.locationX = :locationX"),
    @NamedQuery(name = "User.findByLocationY", query = "SELECT u FROM User u WHERE u.locationY = :locationY"),
    @NamedQuery(name = "User.findByPhone", query = "SELECT u FROM User u WHERE u.phone = :phone"),
    @NamedQuery(name = "User.findByEmail", query = "SELECT u FROM User u WHERE u.email = :email")})
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "username")
    private String username;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 256)
    @Column(name = "password")
    private String password;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "first_name")
    private String firstName;
    @Size(max = 32)
    @Column(name = "middle_name")
    private String middleName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "last_name")
    private String lastName;
    @Basic(optional = false)
    @NotNull
    @Column(name = "security_question")
    private int securityQuestion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "security_answer")
    private String securityAnswer;
    @Basic(optional = false)
    @NotNull
    @Column(name = "location_x")
    private double locationX;
    @Basic(optional = false)
    @NotNull
    @Column(name = "location_y")
    private double locationY;
    // @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message="Invalid phone/fax format, should be as xxx-xxx-xxxx")//if the field contains phone or fax number consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "phone")
    private String phone;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "email")
    private String email;
    /*@ManyToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Collection<UserGroup> userGroupCollection;*/
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userId")
    private Collection<File1> fileCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userId")
    private Collection<Message> messageCollection;

    /**
     * Constructor
     */
    public User() {
    }

    /**
     * Constructor 
     * @param id 
     */
    public User(Integer id) {
        this.id = id;
    }

    /**
     * Constructor
     * @param id
     * @param username
     * @param password
     * @param firstName
     * @param lastName
     * @param securityQuestion
     * @param securityAnswer
     * @param locationX
     * @param locationY
     * @param phone
     * @param email 
     */
    public User(Integer id, String username, String password, String firstName, String lastName, int securityQuestion, String securityAnswer, double locationX, double locationY, String phone, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.securityQuestion = securityQuestion;
        this.securityAnswer = securityAnswer;
        this.locationX = locationX;
        this.locationY = locationY;
        this.phone = phone;
        this.email = email;
    }

    /**
     * Get the user id
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Set the user id
     * @param id 
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Get the username of the user
     * @return 
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set the username
     * @param username 
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Get the password
     * @return 
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set the password
     * @param password 
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Get the firstname
     * @return firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Set the first name
     * @param firstName 
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Get the middle name
     * @return 
     */
    public String getMiddleName() {
        return middleName;
    }

    /**
     * Set the middle name
     * @param middleName 
     */
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    /**
     * Get the last name
     * @return lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Set the last name
     * @param lastName 
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Get the security question
     * @return securityQuestion
     */
    public int getSecurityQuestion() {
        return securityQuestion;
    }

    /**
     * set security question
     * @param securityQuestion 
     */
    public void setSecurityQuestion(int securityQuestion) {
        this.securityQuestion = securityQuestion;
    }

    /**
     * get the security answer
     * @return 
     */
    public String getSecurityAnswer() {
        return securityAnswer;
    }

    /**
     * set the security answer
     * @param securityAnswer 
     */
    public void setSecurityAnswer(String securityAnswer) {
        this.securityAnswer = securityAnswer;
    }

    /**
     * get the locationx
     * @return 
     */
    public double getLocationX() {
        return locationX;
    }

    /**
     * set the location x
     * @param locationX 
     */
    public void setLocationX(double locationX) {
        this.locationX = locationX;
    }

    /**
     * get the location y
     * @return 
     */
    public double getLocationY() {
        return locationY;
    }

    /**
     * set the location y
     * @param locationY 
     */
    public void setLocationY(double locationY) {
        this.locationY = locationY;
    }

    /**
     * get the phone number
     * @return 
     */
    public String getPhone() {
        return phone;
    }

    /**
     * set the phone number
     * @param phone 
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * get the email
     * @return 
     */
    public String getEmail() {
        return email;
    }

    /**
     * set the email
     * @param email 
     */
    public void setEmail(String email) {
        this.email = email;
    }

   /*@XmlTransient
    public Collection<UserGroup> getUserGroupCollection() {
        return userGroupCollection;
    }

    public void setUserGroupCollection(Collection<UserGroup> userGroupCollection) {
        this.userGroupCollection = userGroupCollection;
    }*/

    @XmlTransient
    public Collection<File1> getFileCollection() {
        return fileCollection;
    }

    /**
     * set the file collection
     * @param fileCollection 
     */
    public void setFileCollection(Collection<File1> fileCollection) {
        this.fileCollection = fileCollection;
    }

    @XmlTransient
    /**
     * get the message collection
     */
    public Collection<Message> getMessageCollection() {
        return messageCollection;
    }

    /**
     * set the message collection
     * @param messageCollection 
     */
    public void setMessageCollection(Collection<Message> messageCollection) {
        this.messageCollection = messageCollection;
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
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mapchat.entitypackage.User[ id=" + id + " ]";
    }
    
}
