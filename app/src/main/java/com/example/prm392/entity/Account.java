package com.example.prm392.entity;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import java.sql.Date;

@Entity(tableName = "account",
foreignKeys = {
        @androidx.room.ForeignKey(
                entity = Role.class,
                parentColumns = "id",
                childColumns = "userRoleId",
                onDelete = androidx.room.ForeignKey.CASCADE
        )
})
public class Account extends BaseEntity{
    @ColumnInfo(name = "full_name", typeAffinity = ColumnInfo.TEXT)
    private String username;
    @ColumnInfo(name = "password", typeAffinity = ColumnInfo.TEXT)
    private String password;
    @ColumnInfo(name = "phone", typeAffinity = ColumnInfo.TEXT)
    private String phone;
    @ColumnInfo(name = "address", typeAffinity = ColumnInfo.TEXT)
    private String address;
    @ColumnInfo(name = "image", typeAffinity = ColumnInfo.TEXT)
    private String image;
    @ColumnInfo(name = "userRoleId",index = true)
    private long userRoleId;

    public Account(long id, Long createdAt, Long updatedAt, Long deletedAt, String createdBy, String updatedBy, String deletedBy, String username,String password, String phone, String address, String image, long userRoleId) {
        super(id, createdAt, updatedAt, deletedAt, createdBy, updatedBy, deletedBy);
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.image = image;
        this.userRoleId = userRoleId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public long getUserRoleId() {
        return userRoleId;
    }

    public void setUserRoleId(long userRoleId) {
        this.userRoleId = userRoleId;
    }
}
