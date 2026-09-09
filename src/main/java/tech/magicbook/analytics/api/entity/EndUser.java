package tech.magicbook.analytics.api.entity;

import java.time.OffsetDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import tech.magicbook.analytics.api.entity.enums.AgeRange;
import tech.magicbook.analytics.api.entity.enums.EndUserStatus;

@Entity
@Table(name = "end_users")
public class EndUser {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;

    @Column(name = "external_id", nullable = false)
    private String externalId;

    @Enumerated(EnumType.STRING)
    @Column(name = "age_range", nullable = false)
    private AgeRange ageRange;

    @Column
    private String region;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EndUserStatus status;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "deleted_at")
    private OffsetDateTime deletedAt;

    protected EndUser() {
    }

    public EndUser(
            UUID id,
            Application application,
            String externalId,
            AgeRange ageRange,
            String region,
            EndUserStatus status,
            OffsetDateTime createdAt) {

        this.id = id;
        this.application = application;
        this.externalId = externalId;
        this.ageRange = ageRange;
        this.region = region;
        this.status = status;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public Application getApplication() {
        return application;
    }

    public String getExternalId() {
        return externalId;
    }

    public AgeRange getAgeRange() {
        return ageRange;
    }

    public String getRegion() {
        return region;
    }

    public EndUserStatus getStatus() {
        return status;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getDeletedAt() {
        return deletedAt;
    }

    public void delete() {
        this.status = EndUserStatus.DELETED;
        this.deletedAt = OffsetDateTime.now();
    }
}
