package com.stackclone.stackoverflow_clone.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        name = "question_views",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "question_id"})
)
public class QuestionView {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "question_id")
    private Question question;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

}
