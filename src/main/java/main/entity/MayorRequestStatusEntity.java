package main.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "mayor_request_status")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MayorRequestStatusEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "status_name")
    private String status;
}
