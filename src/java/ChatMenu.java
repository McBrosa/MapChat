/*
 * Created by Sean Arcayan on 2016.04.18  * 
 * Copyright © 2016 Sean Arcayan. All rights reserved. * 
 */
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.MenuModel;
 
/**
 *
 * @author Sean
 */
@ManagedBean(name = "chatMenu")
public class ChatMenu {
     
    private MenuModel model;
 
    @PostConstruct
    public void init() {
        model = new DefaultMenuModel();

        DefaultMenuItem item = new DefaultMenuItem("Create Chatroom");
        item.setIcon("ui-icon-plusthick");
        //item.setCommand("#{chatMenu.create}");
        // show the create chatroom modal
        item.setOnclick("PF('createChatroom').show();");
        model.addElement(item);
         
        item = new DefaultMenuItem("Delete Chatroom");
        item.setIcon(" ui-icon-trash");
        //item.setCommand("#{chatMenu.delete}");
        // show the delete chatroom modal
        item.setOnclick("PF('deleteChatroom').show();");
        model.addElement(item);
         
        item = new DefaultMenuItem("Settings");
        item.setIcon("ui-icon-gear");
        // show the settings modal
        item.setOnclick("PF('settingsChatroom').show();"); 
        model.addElement(item);
 
    }
 
    public MenuModel getModel() {
        return model;
    }   
     
    public void save() {
        addMessage("Success", "Data saved");
    }
     
    public void update() {
        addMessage("Success", "Data updated");
    }
     
    public void delete() {
        addMessage("Success", "Data deleted");
    }
     
    public void addMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}
