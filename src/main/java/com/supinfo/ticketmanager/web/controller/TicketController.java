package com.supinfo.ticketmanager.web.controller;

import com.supinfo.ticketmanager.entity.ProductOwner;
import com.supinfo.ticketmanager.entity.Ticket;
import com.supinfo.ticketmanager.entity.TicketPriority;
import com.supinfo.ticketmanager.entity.TicketStatus;
import com.supinfo.ticketmanager.service.TicketService;
import com.supinfo.ticketmanager.service.UserService;
import fr.bargenson.util.faces.ControllerHelper;
import org.primefaces.component.dialog.Dialog;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@Named
@RequestScoped
public class TicketController implements Serializable {
	
	private static final long serialVersionUID = 354054054054L;
	
	
	protected static final String ADD_TICKET_OUTCOME = "newTickets?faces-redirect=true";

	@EJB
	private TicketService ticketService;
	
	@EJB
	private UserService userService;
	
	@Inject
	private ControllerHelper controllerHelper;
	
	private Ticket ticket;
	private transient DataModel<Ticket> newTicketsModel;
	private transient List<SelectItem> priorityItems;
    
    private boolean dialogAddTicketOpen;

    @PostConstruct
    public void init() {
        ticket = new Ticket();
    }
	
	public DataModel<Ticket> getNewTicketsModel() {
		if(newTicketsModel == null) {
			newTicketsModel = new ListDataModel<Ticket>(ticketService.getTicketsByStatus(TicketStatus.NEW));
		}
		return newTicketsModel;
	}
	
	public String addTicket() throws Exception {
        Dialog dialogAddTicket = (Dialog) FacesContext.getCurrentInstance().getViewRoot().findComponent("dialogAddTicket");
        dialogAddTicket.setVisible(true);

        String username = controllerHelper.getUserPrincipal().getName();
        ProductOwner reporter = (ProductOwner) userService.findUserByUsername(username);

        ticket.setReporter(reporter);

        try {
            ticketService.addTicket(ticket);

            dialogAddTicket.setVisible(false);
        }
        catch(Exception ex) {
            dialogAddTicket.setVisible(true);

            throw ex;
        }

        return null;
	}
	
	public List<SelectItem> getPriorityItems() {
		if(priorityItems == null) {
			ResourceBundle bundle = controllerHelper.getResourceBundle("msg");
			priorityItems = new ArrayList<SelectItem>();
			for (TicketPriority priority : TicketPriority.values()) {
				priorityItems.add(new SelectItem(priority, bundle.getString(priority.getBundleKey())));
			}
		}
		return priorityItems;
	}

	public Ticket getTicket() {
		return ticket;
	}
	
	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}

    public boolean isDialogAddTicketOpen() {
        return dialogAddTicketOpen;
    }

    public void setDialogAddTicketOpen(boolean dialogAddTicketOpen) {
        this.dialogAddTicketOpen = dialogAddTicketOpen;
    }
}
