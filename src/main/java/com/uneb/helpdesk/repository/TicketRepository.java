package com.uneb.helpdesk.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.uneb.helpdesk.model.Ticket;

@Repository
public interface TicketRepository extends PagingAndSortingRepository<Ticket, Long>{

	//////////////////////////////
	Ticket findByOne(Long ticketId);
	
}
