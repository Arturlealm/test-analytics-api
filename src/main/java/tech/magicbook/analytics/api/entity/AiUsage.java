package tech.magicbook.analytics.api.entity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "ai_usages")
public class AiUsage {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "end_user_id")
    private EndUser endUser;

    @Column(nullable = false)
    private String provider;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    private Long tokens;

    @Column(nullable = false, precision = 19, scale = 6)
    private BigDecimal cost;

    @Column(name = "occurred_at", nullable = false)
    private OffsetDateTime occurredAt;

    protected AiUsage() {
    }

    public AiUsage(
            UUID id,
            Application application,
            EndUser endUser,
            String provider,
            String model,
            Long tokens,
            BigDecimal cost,
            OffsetDateTime occurredAt) {

        this.id = id;
        this.application = application;
        this.endUser = endUser;
        this.provider = provider;
        this.model = model;
        this.tokens = tokens;
        this.cost = cost;
        this.occurredAt = occurredAt;
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

    public String getProvider() {
        return provider;
    }

    public String getModel() {
        return model;
    }

    public Long getTokens() {
        return tokens;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public OffsetDateTime getOccurredAt() {
        return occurredAt;
    }
}