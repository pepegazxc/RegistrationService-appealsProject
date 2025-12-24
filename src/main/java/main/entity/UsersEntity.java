package main.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name = "users")
@Getter
@Setter
public class UsersEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    String name;

    String surname;

    @Column(name = "user_identifier")
    String userIdentifier;

    @Column(name = "cipher_email")
    String cipherEmail;

    @Column(name = "cipher_phone_number")
    String cipherPhoneNumber;

    @Column(name = "hash_password")
    String hashPassword;

    @Column(name = "created_at")
    Timestamp createdAt;

    @Column(name = "updated_at")
    Timestamp updatedAt;

}
