package com.kakitu.kakitu.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuAdapter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.kakitu.kakitu.R;

import com.kakitu.kakitu.utils.Urls;

import java.util.HashMap;
import java.util.Map;


public class SmsLogin extends AppCompatActivity {

    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    @BindView(R.id.phone)
    EditText phoneEdit;
    @BindView(R.id.verification_code)
    EditText verificationEdit;

    @BindView(R.id.btn_sign_in)
    Button signInButton;
    @BindView(R.id.btn_get_code)
    Button getCodeButton;

    @BindView(R.id.msg)
    TextView messageText;

    @BindView(R.id.progress)
    ProgressBar progressBar;

    private String passKey;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_login);

        if (sessionManager.isLoggedIn()) {
            Intent intent = new Intent(SmsLogin.this,
                    NewMain.class);
            intent.putExtra("EXTRA_NAME", SmsLogin.class.getSimpleName());

            startActivity(intent);
            finish();
        }

    }

    @OnClick(R.id.btn_get_code)
    void getCode() {
        passKey = randomAlphaNumeric(5);
        sessionManager.setVerificationCode(passKey);
        Log.d("PassKEy", passKey);
        String phone = phoneEdit.getText().toString();
        sessionManager.setPhone(phone);
        submitPhone(phone, passKey);

    }

    @OnClick(R.id.btn_sign_in)
    void signIn() {
        String code = verificationEdit.getText().toString();
        if (!sessionManager.getVerificationCode().isEmpty() && code.matches(sessionManager.getVerificationCode())) {
            sessionManager.setLogin(true);
            Intent intent = new Intent(SmsLogin.this,
                    NewMain.class);
            intent.putExtra("EXTRA_NAME", SmsLogin.class.getSimpleName());

            startActivity(intent);
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "Invalid code", Toast.LENGTH_LONG).show();
        }
    }

    public void submitPhone(final String phone, final String message) {
        progressBar.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, Urls.GET_CODE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Server response", response);
                progressBar.setVisibility(View.GONE);
                phoneEdit.setVisibility(View.GONE);
                verificationEdit.setVisibility(View.VISIBLE);
                getCodeButton.setVisibility(View.GONE);
                signInButton.setVisibility(View.VISIBLE);
                messageText.setVisibility(View.VISIBLE);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error:", error.getMessage());
                progressBar.setVisibility(View.GONE);

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("phone", phone);
                params.put("message", message);
                return params;
            }
        };

    }


    public static String randomAlphaNumeric(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }
}
