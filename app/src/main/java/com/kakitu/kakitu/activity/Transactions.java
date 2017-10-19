package com.kakitu.kakitu.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.kakitu.kakitu.R;
import com.kakitu.kakitu.adapter.AccountRecyclerAdapter;
import com.kakitu.kakitu.model.Account;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Transactions extends AppCompatActivity {

    private List<Account> accountList = new ArrayList<>();
    private AccountRecyclerAdapter accountRecyclerAdapter;

    @BindView(R.id.recycler_view_transactions)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_back);
        }
        accountRecyclerAdapter = new AccountRecyclerAdapter(accountList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(accountRecyclerAdapter);


        setUpItems();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(getApplicationContext(), NewMain.class);
                intent.putExtra("EXTRA_NAME", Transactions.class.getSimpleName());
                startActivity(intent);
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpItems() {
        Account account = new Account("Transactions");
        accountList.add(account);

        account = new Account("Blockchain");
        accountList.add(account);
    }

}
