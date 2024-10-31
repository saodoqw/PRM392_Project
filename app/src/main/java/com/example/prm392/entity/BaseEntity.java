package com.example.prm392.entity;

import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

import lombok.experimental.SuperBuilder;

@SuperBuilder
public abstract class BaseEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;
    @ColumnInfo(name = "created_at", defaultValue = "CURRENT_TIMESTAMP")
    private Long createdAt;
    @ColumnInfo(name = "updated_at")
    private Long updatedAt;
    @ColumnInfo(name = "deleted_at")
    private Long deletedAt;
    @ColumnInfo(name = "created_by")
    private String createdBy;
    @ColumnInfo(name = "updated_by")
    private String updatedBy;
    @ColumnInfo(name = "deleted_by")
    private String deletedBy;

    public BaseEntity(long id, Long createdAt, Long updatedAt, Long deletedAt, String createdBy, String updatedBy, String deletedBy) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
        this.deletedBy = deletedBy;
    }

    public BaseEntity(long id) {
        this.id = id;
    }

    public BaseEntity() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Long deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
    }
}
