package com.example.prm392.entity.Relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.prm392.entity.Account;
import com.example.prm392.entity.Role;

import java.util.List;

public class RoleWithUsers {
    @Embedded
    private Role role;

    @Relation(
            parentColumn = "id",
            entityColumn = "role"
    )
    public List<Account> accounts;
}
