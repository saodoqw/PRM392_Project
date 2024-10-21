package com.example.prm392.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.prm392.entity.Account;
import com.example.prm392.entity.Relations.UserWithOrdersPoliciesCartsComments;

@Dao
public abstract interface AccountDAO {
    @Query("SELECT * FROM account WHERE full_name = :username AND password = :password")
    Account checkLogin(String username, String password);

    @Transaction
    @Query("SELECT * FROM account WHERE id = :accountId")
    UserWithOrdersPoliciesCartsComments getUserWithOrdersPoliciesCartsComments(int accountId);

    @Insert
    void insert(Account account);

    @Query("SELECT * FROM account WHERE full_name = :username")
    Account checkUsername(String username);
}
