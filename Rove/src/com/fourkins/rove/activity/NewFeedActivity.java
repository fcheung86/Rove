package com.fourkins.rove.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.fourkins.rove.R;
import com.fourkins.rove.sqlite.posts.Post;
import com.fourkins.rove.sqlite.posts.PostsManager;

public class NewFeedActivity extends Activity {

    private PostsManager mPostsManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);

        mPostsManager = new PostsManager(this);
    }

    public void submitMessage(View view) {
        final EditText user_text = (EditText) findViewById(R.id.edit_user);
        final EditText latitude_text = (EditText) findViewById(R.id.edit_latitude);
        final EditText longitude_text = (EditText) findViewById(R.id.edit_longitude);
        final EditText message_text = (EditText) findViewById(R.id.edit_message);

        String user = (String) user_text.getText().toString();
        double latitude = Long.parseLong(latitude_text.getText().toString());
        double longitude = Long.parseLong(longitude_text.getText().toString());
        String message = (String) message_text.getText().toString();

        Post new_post = new Post(user, latitude, longitude, message);
        mPostsManager.insertPost(new_post);
        finish();
    }
}
