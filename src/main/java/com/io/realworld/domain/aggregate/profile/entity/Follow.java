package com.io.realworld.domain.aggregate.profile.entity;

import com.io.realworld.domain.aggregate.user.entity.User;
import lombok.*;

import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="follows", uniqueConstraints = {
        @UniqueConstraint(name="follow_followee_pair_unique",columnNames = {"followee","follower"})
})
@Entity
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "followee")
    private User followee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name="follower")
    private User follower;
}


