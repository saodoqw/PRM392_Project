package com.example.prm392.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.prm392.entity.Account;
import com.example.prm392.entity.Relations.RoleWithUsers;
import com.example.prm392.entity.Relations.UserWithOrdersPoliciesCartsComments;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

import java.util.List;

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

    @Query("SELECT * FROM account WHERE id = :id")
    Account getAccountById(int id);

    @Query("SELECT * FROM account WHERE userRoleId = 2")
    List<Account> getAccountsUser();

    @Query("SELECT * FROM account WHERE phone = :phone")
    Account checkPhone(String phone);

    @Query("UPDATE account SET password = :password WHERE id = :id")
    void updatePassword(long id, String password);

}
