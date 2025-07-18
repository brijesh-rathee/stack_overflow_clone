package com.stackclone.stackoverflow_clone.entity;

import com.stackclone.stackoverflow_clone.enums.BadgeType;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "badges")
public class Badge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "badge_type", nullable = false)
    private BadgeType type;

    @ManyToMany(mappedBy = "badges")
    private Set<User> users = new HashSet<>();
}
