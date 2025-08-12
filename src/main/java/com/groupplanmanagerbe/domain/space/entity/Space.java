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
@Table(
        name = "spaces",
        indexes = @Index(name = "idx_space_deleted", columnList = "deleted")
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Space extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    @OneToMany(mappedBy = "space", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SpaceMember> members = new ArrayList<>();

    @Builder
    public Space(String name, String profileImageKey) {
        this.name = name;
    }

    public static Space of(String name) {
        return Space.builder()
                .name(name)
                .build();
    }

    public void addMember(SpaceMember member) {
        members.add(member);
        member.setSpace(this);
    }

    public void updateSpaceInfo(String name) {
        if (name != null && !name.isBlank()) {
            updateName(name);
        }
    }

    public void softDelete() {
        this.deleted = true;
        members.forEach(SpaceMember::softDeleted);
    }

    public void removeMember(SpaceMember target) {
        members.remove(target);
        target.setSpace(null);
    }

    public SpaceMember getMember(Long userId) {
        return members.stream()
                .filter(m -> m.getUser().getId().equals(userId))
                .findFirst()
                .get();
    }

    private void updateName(String name) {
        this.name = name;
    }
}
