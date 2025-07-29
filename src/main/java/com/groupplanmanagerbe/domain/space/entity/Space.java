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

    @Column(name = "profile_image_key")
    private String profileImageKey;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    @OneToMany(mappedBy = "space", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<SpaceMember> members = new ArrayList<>();

    @Builder
    public Space(String name, String profileImageKey) {
        this.name = name;
        this.profileImageKey = profileImageKey;
    }

    public static Space of(String name, String profileImageKey) {
        return Space.builder()
                .name(name)
                .profileImageKey(profileImageKey)
                .build();
    }

    public void addMember(SpaceMember member) {
        members.add(member);
    }

    public void updateSpaceInfo(String name, String profileImageKey) {
        if (name != null && !name.isBlank()) {
            updateName(name);
        }

        if (profileImageKey != null && !profileImageKey.isBlank()) {
            updateProfileImageKey(profileImageKey);
        }
    }

    private void updateName(String name) {
        this.name = name;
    }

    private void updateProfileImageKey(String profileImageKey) {
        this.profileImageKey = profileImageKey;
    }
}
