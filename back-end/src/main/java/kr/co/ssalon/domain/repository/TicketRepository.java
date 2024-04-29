package kr.co.ssalon.domain.repository;

import kr.co.ssalon.domain.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket,Long> {
}
