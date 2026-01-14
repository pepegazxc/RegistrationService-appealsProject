package main.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

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

    @CreationTimestamp
    @Column(name = "created_at")
    Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    Timestamp updatedAt;

    public static class Builder{
        UsersEntity users = new UsersEntity();

        public Builder name(String name){
            users.name = name;
            return this;
        }

        public Builder surname(String surname){
            users.surname = surname;
            return this;
        }

        public Builder userIdentifier(String userIdentifier){
            users.userIdentifier = userIdentifier;
            return this;
        }

        public Builder cipherEmail(String cipherEmail){
            users.cipherEmail = cipherEmail;
            return this;
        }

        public Builder cipherPhoneNumber(String cipherPhoneNumber){
            users.cipherPhoneNumber = cipherPhoneNumber;
            return this;
        }

        public Builder hashPassword(String hashPassword){
            users.hashPassword = hashPassword;
            return this;
        }

        public Builder createdAt(Timestamp createdAt){
            users.createdAt = createdAt;
            return this;
        }

        public UsersEntity build(){
            return users;
        }
    }
}
