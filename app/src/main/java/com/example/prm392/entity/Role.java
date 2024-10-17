package com.example.prm392.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(tableName = "role")
public class Role extends BaseEntity {
    @ColumnInfo(name = "role_name", typeAffinity = ColumnInfo.TEXT)
    private String roleName;

    public Role(long id, String roleName) {
        super(id);
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
