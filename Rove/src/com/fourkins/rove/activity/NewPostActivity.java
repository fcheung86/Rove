package com.fourkins.rove.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.fourkins.rove.R;
import com.fourkins.rove.fragments.MapFragment;
import com.fourkins.rove.sqlite.posts.Post;
import com.fourkins.rove.sqlite.posts.PostsManager;

public class NewPostActivity extends Activity {

    private PostsManager mPostsManager;

    double latitudefromMap = 0;
    double longitudefromMap = 0;
    int fromMap = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        Intent intent = getIntent();
        if (intent.getIntExtra(MapFragment.fromMap, 0) == 1) {
            fromMap = intent.getIntExtra(MapFragment.fromMap, 0);
            latitudefromMap = intent.getDoubleExtra(MapFragment.intentLat, 0);
            longitudefromMap = intent.getDoubleExtra(MapFragment.intentLong, 0);
            
            final EditText latitudeText = (EditText) findViewById(R.id.edit_latitude);
            final EditText longitudeText = (EditText) findViewById(R.id.edit_longitude);
            latitudeText.setText(String.valueOf(latitudefromMap));
            longitudeText.setText(String.valueOf(longitudefromMap));
        }

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
        
        double latitude = 0;
        double longitude = 0;

        
        if (fromMap == 1) {
            latitude = latitudefromMap;
            longitude = longitudefromMap;
        } else {        
            latitude = Long.parseLong(latitudeText.getText().toString());
            longitude = Long.parseLong(longitudeText.getText().toString());            
        }
        
        String user = (String) userText.getText().toString();
        String message = (String) messageText.getText().toString();
        
        Post post = new Post(user, latitude, longitude, message);
        mPostsManager.insertPost(post);

        finish();
    }
}
