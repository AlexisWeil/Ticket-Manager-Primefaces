package com.supinfo.ticketmanager.web.controller;

import com.supinfo.ticketmanager.entity.ProductOwner;
import com.supinfo.ticketmanager.entity.Ticket;
import com.supinfo.ticketmanager.entity.TicketPriority;
import com.supinfo.ticketmanager.entity.TicketStatus;
import com.supinfo.ticketmanager.service.TicketService;
import com.supinfo.ticketmanager.service.UserService;
import fr.bargenson.util.faces.ControllerHelper;

import javax.annotation.PostConstruct;
import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@ManagedBean
@ViewScoped
public class TicketController implements Serializable {
	
	private static final long serialVersionUID = 354054054054L;
	
	protected static final String ADD_TICKET_OUTCOME = null;

	@Inject
	private TicketService ticketService;

	@Inject
	private UserService userService;

	@Inject
	private ControllerHelper controllerHelper;
	
	private Ticket ticket;
    private Ticket ticketToShow;
	private transient DataModel<Ticket> newTicketsModel;
    private List<Ticket> newTickets;
	private List<SelectItem> priorityItems;
    private List<SelectItem> productOwnersItems;
    
    private boolean dialogAddTicketOpen;

    @PostConstruct
    public void init() {
        ticket = new Ticket();
        newTickets = ticketService.getTicketsByStatus(TicketStatus.NEW);

        ResourceBundle bundle = controllerHelper.getResourceBundle("msg");
        priorityItems = new ArrayList<SelectItem>();
        priorityItems.add(new SelectItem("", "All"));
        for (TicketPriority priority : TicketPriority.values()) {
            priorityItems.add(new SelectItem(priority, bundle.getString(priority.getBundleKey())));
        }
        
        productOwnersItems = new ArrayList<SelectItem>();
        productOwnersItems.add(new SelectItem("", "All"));
        for(ProductOwner p : userService.findAllProductOwners()) {
            productOwnersItems.add(new SelectItem(p, p.getLastName() + " " + p.getFirstName()));
        }
    }
	
	public DataModel<Ticket> getNewTicketsModel() {
		if(newTicketsModel == null) {
			newTicketsModel = new ListDataModel<Ticket>(ticketService.getTicketsByStatus(TicketStatus.NEW));
		}
		return newTicketsModel;
	}
    
    public void openAddTicketDialog() {
        dialogAddTicketOpen = true;
        System.out.println("lala ?");
    }
	
	public String addTicket() throws Exception {
        System.out.println("Hello lol");

        String username = controllerHelper.getUserPrincipal().getName();
        ProductOwner reporter = (ProductOwner) userService.findUserByUsername(username);

        ticket.setReporter(reporter);

        ticketService.addTicket(ticket);

        dialogAddTicketOpen = false;

        UIComponent datatable = FacesContext.getCurrentInstance().getViewRoot().findComponent("PanelCorps");
        System.out.println("hihi : " + datatable.getClass().getName());

        return null;
	}

    public void selectTicket() {
        ConfigurableNavigationHandler cnh = (ConfigurableNavigationHandler) FacesContext.getCurrentInstance().getApplication().getNavigationHandler();
        cnh.performNavigation("/showTicket.jsf?ticketId=" + ticketToShow.getId() + "&faces-redirect=true");
    }
	
	public List<SelectItem> getPriorityItems() {
        System.out.println("lol " + priorityItems.size());
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

    public List<Ticket> getNewTickets() {
        return newTickets;
    }

    public void setNewTickets(List<Ticket> newTickets) {
        this.newTickets = newTickets;
    }

    public List<SelectItem> getProductOwnersItems() {
        return productOwnersItems;
    }

    public void setProductOwnersItems(List<SelectItem> productOwnersItems) {
        this.productOwnersItems = productOwnersItems;
    }

    public Ticket getTicketToShow() {
        return ticketToShow;
    }

    public void setTicketToShow(Ticket ticketToShow) {
        this.ticketToShow = ticketToShow;
    }
}
