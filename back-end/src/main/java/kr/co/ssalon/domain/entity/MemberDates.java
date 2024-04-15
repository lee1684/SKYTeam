package kr.co.ssalon.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Embeddable
@Getter
public class MemberDates {

    @Column(updatable = false)
    private LocalDateTime joinDate;
    private LocalDateTime lastLoginDate;


    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        joinDate = now;
        lastLoginDate = now;
    }

    @PreUpdate
    public void preUpdate() {
        lastLoginDate = LocalDateTime.now();
    }

}
