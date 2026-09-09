package tech.magicbook.analytics.api.entity;

import java.time.OffsetDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity 
@Table (name = "applications")
public class Application {
    
    @Id 
    private UUID id;

    @Column (nullable = false, unique = true)
    private String name;

    private String description;

    @Column (name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    protected Application(){
        // Construtor sem argumentos
    }

    public Application(UUID id, String name, String description, OffsetDateTime createdAt){
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
    }

     public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

}
