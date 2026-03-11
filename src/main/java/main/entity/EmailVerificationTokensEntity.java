package main.entity;

import jakarta.persistence.*;

import lombok.*;

import java.sql.Timestamp;

@Entity
@Table(name = "email_verification_tokens")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailVerificationTokensEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UsersEntity user;

    @Column(name = "hash_token")
    private String hashToken;

    @Column(name = "expires_at")
    private Timestamp expiresAt;

    @Column(name = "created_at")
    private Timestamp createdAt;

    private Boolean used;
}
