package com.paraizo.mysocial.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    // stored as a hash (e.g. BCrypt), never plain text
    @Column(nullable = false)
    private String password;

    private String name;

    @Column(length = 500)
    private String bio;

    private String profileImageUrl;

    private String phone;

    // used as the default center point for the "posts near me" feed filter,
    // and as a fallback pin if the user hasn't posted anywhere yet
    private Double latitude;
    private Double longitude;

    private LocalDate birthdate;

    // controls whether other users can see this profile/posts without an accepted follow
    @Column(nullable = false)
    @Builder.Default
    private boolean isPrivate = false;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
