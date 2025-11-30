package com.gundan.terragold.entity;

import com.gundan.terragold.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private UserRole role;
    @Column(updatable = false)
    private java.time.LocalDateTime createdAt;
    private java.time.LocalDateTime updatedAt;
}
