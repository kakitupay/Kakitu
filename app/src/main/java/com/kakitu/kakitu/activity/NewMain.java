package com.kakitu.kakitu.activity;

import android.app.DownloadManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Environment;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kakitu.kakitu.R;
import com.kakitu.kakitu.adapter.AccountRecyclerAdapter;

import com.kakitu.kakitu.model.Account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NewMain extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String LOG_TAG = NewMain.class.getSimpleName();

    private List<Account> accountList = new ArrayList<>();
    private AccountRecyclerAdapter accountRecyclerAdapter;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.nav_view)
    NavigationView navigationView;

    private View header;
    private ImageView imageView;

    private String phone;
    private Bitmap bitmap;
    private String savePath = Environment.getExternalStorageDirectory().getPath() + "/Kakitu/QR/";
    private String QR_GEN_TAG = "GenerateQRCode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        header = navigationView.getHeaderView(0);
        TextView textView = header.findViewById(R.id.textView);
        imageView = header.findViewById(R.id.imageView);


        accountRecyclerAdapter = new AccountRecyclerAdapter(accountList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(accountRecyclerAdapter);


        String code = sessionManager.getScannedCode();
        if (!sessionManager.getPhone().isEmpty()) {
            phone = sessionManager.getPhone();
            Log.d("Phone", phone);
            if (sessionManager.getEncodedString().isEmpty()) {
                getEncoded(phone);
                if (generateQR(sessionManager.getEncodedString()) == null) {
                    Toast.makeText(getApplicationContext(), "NULL", Toast.LENGTH_SHORT).show();
                } else {
                    bitmap = generateQR(sessionManager.getEncodedString());
                    imageView.setImageBitmap(bitmap);
                }

            } else {
                Toast.makeText(getApplicationContext(), sessionManager.getEncodedString(), Toast.LENGTH_SHORT).show();
                bitmap = generateQR(sessionManager.getEncodedString());
                imageView.setImageBitmap(bitmap);
            }

            textView.setText(phone);
        } else {

        }

        Intent intent = getIntent();
        if (!code.isEmpty() && intent.getStringExtra("EXTRA_NAME").equals(DecoderActivity.class.getSimpleName())) {
            Log.d("Code", code);
            if (!phone.contains("254")) {
                phone = "254" + phone.substring(1);
                Log.d("Phone", phone);
                sendCode(code, phone);
            } else {
                Log.d("Phone", phone);
                sendCode(code, phone);
            }

        }


        prepareAccountListItems();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.d(NewMain.class.getSimpleName(), "In on Resume");
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

        } else if (id == R.id.nav_account) {

        } else if (id == R.id.nav_transactions) {
            startActivity(new Intent(getApplicationContext(), Transactions.class));

        } else if (id == R.id.nav_share) {
            Toast.makeText(this, "Coming soon", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_download) {
            Toast.makeText(this, "Coming soon", Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void sendCode(final String decodedCode, final String phone) {
        StringRequest request = new StringRequest(Request.Method.POST, "http://kakitu.co.ke/decode.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("qrstr", decodedCode);
                params.put("phone", phone);

                return params;
            }
        };


    }

    private void getEncoded(final String phone) {
        Log.d(LOG_TAG, "Enoded started");
        StringRequest request = new StringRequest(DownloadManager.Request.Method.GET, "http://kakitu.co.ke/encode.php?Account=" + phone, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                sessionManager.setEncodedString(response);
                Log.d(LOG_TAG, "Encoding finished");

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();


            }
        });

    }

    /**
     * Add sample account items
     */
    private void prepareAccountListItems() {
        Account account = new Account("Notifications", R.drawable.ic_action_notification);
        accountList.add(account);

        account = new Account("Balance", R.drawable.ic_action_balance);
        accountList.add(account);

        account = new Account("Pay via KakituPOS", R.drawable.qr_code);
        accountList.add(account);

        account = new Account("Phone Numbers", R.drawable.ic_action_phone);
        accountList.add(account);
//
//        account = new Account("Loans", R.drawable.ic_action_loans);
//        accountList.add(account);

    }

    private Bitmap generateQR(String inputValue) {
        Bitmap bitmap = null;
        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        int smallerDimension = width < height ? width : height;
        smallerDimension = smallerDimension * 3 / 4;
        Log.d(QR_GEN_TAG, String.valueOf(smallerDimension));


        try {
            bitmap = qrgEncoder.encodeAsBitmap();

        } catch (WriterException e) {
            Log.d(QR_GEN_TAG, e.getMessage());
        }

        return bitmap;

    }
}
