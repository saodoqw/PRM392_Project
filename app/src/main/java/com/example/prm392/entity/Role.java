package com.example.prm392.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.example.prm392.entity.Enums.RoleName;

@Entity(tableName = "role")
public class Role extends BaseEntity {
    @ColumnInfo(name = "role_name", typeAffinity = ColumnInfo.TEXT)
    private RoleName roleName;

    public Role(long id, RoleName roleName) {
        super(id);
        this.roleName = roleName;
    }

    public RoleName getRoleName() {
        return roleName;
    }

    public void setRoleName(RoleName roleName) {
        this.roleName = roleName;
    }
}
