package main.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "mayor_request")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MayorRequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UsersEntity user;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private MayorRequestStatusEntity status;

    private String token;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "reviewed_at")
    private LocalDateTime reviewed_at;

    @Column(name = "is_used")
    private Boolean isUsed;

    @Column(name = "expired_at")
    private LocalDateTime expiredAt;
}
