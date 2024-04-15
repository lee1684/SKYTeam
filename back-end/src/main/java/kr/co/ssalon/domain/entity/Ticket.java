package kr.co.ssalon.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@Builder
@AllArgsConstructor
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private Meeting meeting;
    private String decoration;

    protected Ticket() {}

    public static Ticket createTicket() {
        // Change to builder
        Ticket ticket = new Ticket();
        return ticket;
    }
}