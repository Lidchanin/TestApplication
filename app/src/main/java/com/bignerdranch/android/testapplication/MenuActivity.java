package com.bignerdranch.android.testapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.bignerdranch.android.testapplication.maps.MapsActivity;
import com.bignerdranch.android.testapplication.posts.activity.PostsActivity;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.widget.ProfilePictureView;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;

import org.json.JSONObject;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        getSupportActionBar().hide();
        GraphRequest request = GraphRequest.newGraphPathRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/",
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        JSONObject resp = response.getJSONObject();
                        String id = resp.optString("id");
                        ProfilePictureView pictureView = (ProfilePictureView) findViewById(R.id.fbimg);
                        pictureView.setProfileId(id);
                        String name = resp.optString("name");
                        TextView nameTextView = (TextView) findViewById(R.id.profileNameTextView);
                        nameTextView.setText(String.valueOf(name));
                        String birthday = resp.optString("birthday");
                        TextView birthdayTextView = (TextView) findViewById(R.id.profileBirthdayTextView);
                        String[] tempArr;
                        tempArr = String.valueOf(birthday).split("/");
                        birthdayTextView.setText(tempArr[1] + " " + tempArr[0] + " " + tempArr[2]);
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "name,id,birthday");
        request.setParameters(parameters);
        request.executeAsync();

        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("https://developers.facebook.com"))
                .build();
        ShareButton shareButton = (ShareButton) findViewById(R.id.shareButton);
        shareButton.setShareContent(content);

    }

    public void mapsButton(View view) {
        Intent intent = new Intent(MenuActivity.this, MapsActivity.class);
        startActivity(intent);
    }

    public void postsButton(View view) {
        Intent intent = new Intent(MenuActivity.this, PostsActivity.class);
        startActivity(intent);
    }
}
