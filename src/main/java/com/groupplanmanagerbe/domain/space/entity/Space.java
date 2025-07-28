package com.groupplanmanagerbe.domain.space.entity;

import com.groupplanmanagerbe.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "spaces")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Space extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "profile_url")
    private String profileUrl;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    @OneToMany(mappedBy = "space", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<SpaceMember> members = new ArrayList<>();

    @Builder
    public Space(String name, String profileUrl) {
        this.name = name;
        this.profileUrl = profileUrl;
    }

    public static Space of(String name, String profileUrl) {
        return Space.builder()
                .name(name)
                .profileUrl(profileUrl)
                .build();
    }

    public void addMember(SpaceMember member) {
        members.add(member);
    }
}
