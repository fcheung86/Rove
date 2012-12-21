package com.fourkins.rove.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.fourkins.rove.R;
import com.fourkins.rove.sqlite.posts.Post;
import com.fourkins.rove.sqlite.posts.PostsManager;

public class NewPostActivity extends Activity {

    private PostsManager mPostsManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        mPostsManager = new PostsManager(this);

        // hide the action bar
        ActionBar actionBar = getActionBar();
        actionBar.hide();
    }

    public void submitMessage(View view) {
        final EditText userText = (EditText) findViewById(R.id.edit_user);
        final EditText latitudeText = (EditText) findViewById(R.id.edit_latitude);
        final EditText longitudeText = (EditText) findViewById(R.id.edit_longitude);
        final EditText messageText = (EditText) findViewById(R.id.edit_message);

        String user = (String) userText.getText().toString();
        double latitude = Long.parseLong(latitudeText.getText().toString());
        double longitude = Long.parseLong(longitudeText.getText().toString());
        String message = (String) messageText.getText().toString();

        Post post = new Post(user, latitude, longitude, message);
        mPostsManager.insertPost(post);

        finish();
    }
}
