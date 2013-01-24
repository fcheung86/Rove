package com.fourkins.rove.activity;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import org.apache.http.entity.StringEntity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

import com.fourkins.rove.R;
import com.fourkins.rove.application.Rove;
import com.fourkins.rove.users.User;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class NewUserActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_new_user, menu);
        return true;
    }

    public void registerNewUser(View view) throws NoSuchAlgorithmException {
        final EditText userNameView = (EditText) findViewById(R.id.userNameEditText);
        final EditText realNameView = (EditText) findViewById(R.id.realNameEditText);
        final EditText emailView = (EditText) findViewById(R.id.emailEditText);
        final EditText passwordView = (EditText) findViewById(R.id.passwordEditText);

        String userName = userNameView.getText().toString();
        String realName = realNameView.getText().toString();
        String email = emailView.getText().toString();
        String password = passwordView.getText().toString();

        try {
            User user = new User(userName, realName, email, password);
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

        }

        finish();
        
        Intent myIntent;
        myIntent = new Intent(NewUserActivity.this, com.fourkins.rove.activity.SplashScreenActivity.class);
        startActivity(myIntent);

    }
}
