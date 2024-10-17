package com.example.prm392.DAO;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.prm392.entity.Relations.UserWithOrdersPoliciesCartsComments;

@Dao
public abstract interface AccountDAO {
    @Transaction
    @Query("SELECT * FROM account WHERE id = :accountId")
    UserWithOrdersPoliciesCartsComments getUserWithOrdersPoliciesCartsComments(int accountId);
}
