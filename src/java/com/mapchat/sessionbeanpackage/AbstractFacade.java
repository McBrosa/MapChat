/**
 * Created by MapChat Development Team
 * Edited by Nate Rosa, Corey McQuay
 * Last Modified: 2016.03.22
 * Copyright © 2016 MapChat Development Team. All rights reserved.
 */
package com.mapchat.sessionbeanpackage;

//imports
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import com.mapchat.jsfclassespackage.util.JsfUtil;
import javax.validation.Validator;

/**
 * The Facade class of a general object T
 * 
 * @author Corey McQuay
 * @param <T> What the abstract facade will be based off
 */
public abstract class AbstractFacade<T> {

    private Class<T> entityClass;

    /**
     * Constructor 
     * 
     * @param entityClass The class that the Abstract Facade will be based off.
     */
    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    /**
     * Gets the Enitity Manager
     * @return The enitity manager
     */
    protected abstract EntityManager getEntityManager();

    /**
     * Creates an entity in the facade
     * @param entity the entity needed to be created
     */
    public void create(T entity) {      
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();
    Set<ConstraintViolation<T>> constraintViolations = validator.validate(entity);
    if(constraintViolations.size() > 0){
        Iterator<ConstraintViolation<T>> iterator = constraintViolations.iterator();
        while(iterator.hasNext()){
            ConstraintViolation<T> cv = iterator.next();
            System.err.println(cv.getRootBeanClass().getName()+"."+cv.getPropertyPath() + " " +cv.getMessage());

            JsfUtil.addErrorMessage(cv.getRootBeanClass().getSimpleName()+"."+cv.getPropertyPath() + " " +cv.getMessage());
        }
    }else{
        getEntityManager().persist(entity);
    }
    }

    /**
     * Edits the selected entity type in the facade.
     * @param entity  The entity type
     */
    public void edit(T entity) {
        getEntityManager().merge(entity);
    }

    /**
     * Removes the selected entity in the facade.
     * @param entity 
     */
    public void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    
    /**
     * Finds the object in the entity
     * @param id the object id that needs to be found
     * @return The entity manager responsible for the object
     */
    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    /**
     * Find all objects related in the entity class
     * @return List of all objects related to the Entity class
     */
    public List<T> findAll() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }

    /**
     * Finds a range of items in the entity class based of a range id index
     * @param range the range to get the entity onjects from.
     * @return The range of items from the entity.
     */
    public List<T> findRange(int[] range) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    /**
     * The number of items found by the facade 
     * @return The number of items found in the query.
     */
    public int count() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }
    
}
