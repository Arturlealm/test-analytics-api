package tech.magicbook.analytics.api.entity;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "feature_events")
public class FeatureEvent {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "end_user_id")
    private EndUser endUser;

    @Column(name = "feature_name", nullable = false)
    private String featureName;

    @Column(name = "occurred_at", nullable = false)
    private OffsetDateTime occurredAt;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "metadata", columnDefinition = "jsonb")
    private Map<String, Object> metadata;

    protected FeatureEvent() {
    }

    public FeatureEvent(
            UUID id,
            Application application,
            EndUser endUser,
            String featureName,
            OffsetDateTime occurredAt,
            Map<String, Object> metadata) {

        this.id = id;
        this.application = application;
        this.endUser = endUser;
        this.featureName = featureName;
        this.occurredAt = occurredAt;
        this.metadata = metadata;
    }

    public UUID getId() {
        return id;
    }

    public Application getApplication() {
        return application;
    }

    public EndUser getEndUser() {
        return endUser;
    }

    public String getFeatureName() {
        return featureName;
    }

    public OffsetDateTime getOccurredAt() {
        return occurredAt;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }
}