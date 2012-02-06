package com.supinfo.ticketmanager.web.converter;

import com.supinfo.ticketmanager.entity.ProductOwner;
import com.supinfo.ticketmanager.service.UserService;

import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 * Created by IntelliJ IDEA.
 * User: Alexis
 * Date: 06/02/12
 * Time: 13:25
 * To change this template use File | Settings | File Templates.
 */
@FacesConverter(forClass = ProductOwner.class)
public class ProductOwnerConverter implements Converter {

    @EJB
    private UserService userService;

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        return userService.findUserByUsername(s);
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        return ((ProductOwner) o).getUsername();
    }
}
