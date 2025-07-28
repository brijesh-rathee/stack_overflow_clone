    package com.stackclone.stackoverflow_clone.entity;

    import com.stackclone.stackoverflow_clone.enums.UserRole;
    import jakarta.persistence.*;
    import lombok.*;
    import org.springframework.security.core.GrantedAuthority;
    import org.springframework.security.core.authority.SimpleGrantedAuthority;
    import org.springframework.security.core.userdetails.UserDetails;

    import java.time.LocalDateTime;
    import java.util.*;

    @Entity
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Table(name = "users")
    public class User implements UserDetails {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        private Long id;

        @Column(name = "username", nullable = false)
        private String username;

        @Column(name = "email",nullable = false)
        private String email;

        @Column(name = "password", nullable = false)
        private String password;

        @Column(name = "reputation", nullable = false)
        private int reputation;

        @Column(name = "bio")
        private String bio;

        @Column(name = "location")
        private String location;

        @Column(name = "image_url")
        private String url;

        @Column(name = "website")
        private String website;

        @Enumerated(value = EnumType.STRING)
        @Column(name = "role", nullable = false)
        private UserRole role;

        @Column(name = "created_at", nullable = false)
        private LocalDateTime createdAt;

        @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<Question> questions = new ArrayList<>();

        @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<Answer> answers = new ArrayList<>();

        @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<Comment> comments = new ArrayList<>();

        @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<Vote> votes = new ArrayList<>();

        @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<QuestionView> questionViews = new ArrayList<>();

        @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<Bookmark> bookmarks = new ArrayList<>();

        @ManyToMany(fetch = FetchType.EAGER)
        @JoinTable(
                name = "user_tags",
                joinColumns = @JoinColumn(name = "user_id"),
                inverseJoinColumns = @JoinColumn(name = "tag_id")
        )
        private Set<Tag> followedTags = new HashSet<>();

        @ManyToMany(fetch = FetchType.EAGER)
        @JoinTable(
                name = "user_questions",
                joinColumns = @JoinColumn(name = "user_id"),
                inverseJoinColumns = @JoinColumn(name = "question_id")
        )
        private Set<Question> followedQuestions = new HashSet<>();

        @ManyToMany
        @JoinTable(
                name = "user_badges",
                joinColumns = @JoinColumn(name = "user_id"),
                inverseJoinColumns = @JoinColumn(name = "badge_id")
        )
        private Set<Badge> badges = new HashSet<>();

        @ManyToMany
        @JoinTable(
                name = "user_following",
                joinColumns = @JoinColumn(name = "follower_id"),
                inverseJoinColumns = @JoinColumn(name = "following_id")
        )
        private Set<User> following = new HashSet<>();

        @ManyToMany(mappedBy = "following")
        private Set<User> followers = new HashSet<>();

        @PrePersist
        protected void onCreate() {
            this.createdAt = LocalDateTime.now();
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return List.of(new SimpleGrantedAuthority(role.name()));
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }
    }
