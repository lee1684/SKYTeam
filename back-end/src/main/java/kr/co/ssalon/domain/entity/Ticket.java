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
    // ***** 연관 메서드 *****
    public void changeMeeting(Meeting meeting) {
        this.meeting = meeting;
    }

    public static Ticket createTicket() {
        Ticket ticket = Ticket.builder().build();
        return ticket;
    }
}
