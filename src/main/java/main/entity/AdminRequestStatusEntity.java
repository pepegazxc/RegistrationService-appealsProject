package main.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "admin_request_status")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminRequestStatusEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String status;
}
