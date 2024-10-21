package com.example.prm392.entity.Relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.prm392.entity.Account;
import com.example.prm392.entity.Cart;
import com.example.prm392.entity.Comment;
import com.example.prm392.entity.Order;
import com.example.prm392.entity.Policy;

import java.util.List;

public class UserWithOrdersPoliciesCartsComments {
    @Embedded
    private Account account;

    @Relation(
            parentColumn = "id",
            entityColumn = "account_id"
    )
    public List<Order> orders;

    @Relation(
            parentColumn = "id",
            entityColumn = "account_id"
    )
    public List<Policy> policies;

    @Relation(
            parentColumn = "id",
            entityColumn = "account_id"
    )
    public Cart carts;

    @Relation(
            parentColumn = "id",
            entityColumn = "account_id"
    )
    public List<Comment> comments;

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
