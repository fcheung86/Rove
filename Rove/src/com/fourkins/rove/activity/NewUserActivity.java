package com.fourkins.rove.activity;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import org.apache.http.entity.StringEntity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

import com.fourkins.rove.R;
import com.fourkins.rove.application.Rove;
import com.fourkins.rove.users.User;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class NewUserActivity extends Activity {

    private EditText userNameView;
    private EditText realNameView;
    private EditText emailView;
    private EditText passwordView;
    private EditText confirmPasswordView;

    private String mUserName;
    private String mRealName;
    private String mEmail;
    private String mPassword;
    private String mConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        userNameView = (EditText) findViewById(R.id.userNameEditText);
        realNameView = (EditText) findViewById(R.id.realNameEditText);
        emailView = (EditText) findViewById(R.id.emailEditText);
        passwordView = (EditText) findViewById(R.id.passwordEditText);
        confirmPasswordView = (EditText) findViewById(R.id.confirmPasswordEditText);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_new_user, menu);
        return true;
    }

    public void attemptRegister(View view) {
        userNameView.setError(null);
        realNameView.setError(null);
        emailView.setError(null);
        passwordView.setError(null);
        confirmPasswordView.setError(null);

        mUserName = userNameView.getText().toString();
        mEmail = emailView.getText().toString();
        mRealName = realNameView.getText().toString();
        mPassword = passwordView.getText().toString();
        mConfirmPassword = confirmPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check real name
        if (TextUtils.isEmpty(mRealName)) {
            realNameView.setError(getString(R.string.error_field_required));
            focusView = realNameView;
            cancel = true;
        } else if (TextUtils.isEmpty(mUserName)) {
            userNameView.setError(getString(R.string.error_field_required));
            focusView = userNameView;
            cancel = true;
        }
        // Check email
        else if (TextUtils.isEmpty(mEmail)) {
            emailView.setError(getString(R.string.error_field_required));
            focusView = emailView;
            cancel = true;
        } else if (!mEmail.contains("@")) {
            emailView.setError(getString(R.string.error_invalid_email));
            focusView = emailView;
            cancel = true;
        }
        // Check password
        else if (TextUtils.isEmpty(mPassword)) {
            passwordView.setError(getString(R.string.error_field_required));
            focusView = passwordView;
            cancel = true;
        } else if (!TextUtils.equals(mPassword, mConfirmPassword)) {
            confirmPasswordView.setError(getString(R.string.error_password_mismatch));
            focusView = confirmPasswordView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            registerNewUser();
        }

    }

    public void registerNewUser() {

        try {
            User user = new User(mUserName, mRealName, mEmail, mPassword);
            StringEntity entity = new StringEntity(user.getJson().toString());
            AsyncHttpClient client = new AsyncHttpClient();
            client.post(this, Rove.SERVER_BASE_URL + "/users", entity, "text/plain", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String response) {
                    // this is the async callback, for now we aren't doing anything with it
                    // going forward, we can use this to know that the post is updated and add
                    // it to the feed list
                    System.out.println(response);
                }
            });
        } catch (UnsupportedEncodingException e) {

        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        finish();

        Intent myIntent;
        myIntent = new Intent(NewUserActivity.this, com.fourkins.rove.activity.SplashScreenActivity.class);
        startActivity(myIntent);

    }
}
