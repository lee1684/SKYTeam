package kr.co.ssalon.domain.entity;

import jakarta.persistence.*;

@Entity
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private Meeting meeting;
    private String decoration;

    protected Ticket() {}

    public static Ticket create_ticket() {
        Ticket ticket = new Ticket();
        return ticket;
    }
}
