package com.bignerdranch.android.testapplication;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.internal.ImageDownloader;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.ProfilePictureView;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.widget.ShareButton;

import org.json.JSONException;
import org.json.JSONObject;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        //get profile data
        GraphRequest request = GraphRequest.newGraphPathRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/",
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        JSONObject resp = response.getJSONObject();
                        //avatar
                        String id = resp.optString("id");
                        ProfilePictureView pictureView = (ProfilePictureView)findViewById(R.id.fbimg);
                        pictureView.setProfileId(id);
                        //name
                        String name = resp.optString("name");
                        TextView nameTextView = (TextView)findViewById(R.id.profileNameTextView);
                        nameTextView.setText(String.valueOf(name));
                        //birthday
                        String birthday = resp.optString("birthday");
                        TextView birthdayTextView = (TextView)findViewById(R.id.profileBirthdayTextView);
                        birthdayTextView.setText(String.valueOf(birthday));
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "name,id,birthday");
        request.setParameters(parameters);
        request.executeAsync();

        //facebook Share(without added foto,video)
        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("https://developers.facebook.com"))
                .build();
        ShareButton shareButton = (ShareButton)findViewById(R.id.shareButton);
        shareButton.setShareContent(content);


    }
    public void mapsButton(View view) {
        Intent intent = new Intent(MenuActivity.this, MapsActivity.class);
        startActivity(intent);
    }
}
