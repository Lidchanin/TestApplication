package com.bignerdranch.android.testapplication;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.android.testapplication.maps.MapsActivity;
import com.bignerdranch.android.testapplication.posts.activity.PostsActivity;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.widget.ProfilePictureView;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MenuActivity extends AppCompatActivity {
    private String image;

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
                        try {
                            JSONObject resp = response.getJSONObject();
                            JSONObject picture = resp.getJSONObject("picture");
                            JSONObject data = picture.getJSONObject("data");
                            String urlSrc = data.optString("url");
                            ImageView imageIV = (ImageView) findViewById(R.id.profilePictureImageView);
                            //TODO download image
//                            new ImageDownloader(imageIV).execute(urlSrc);
                            ImageManager.fetchImage(urlSrc, imageIV);
                            String name = resp.optString("name");
                            TextView nameTextView = (TextView) findViewById(R.id.profileNameTextView);
                            nameTextView.setText(String.valueOf(name));
                            //TODO error with logIn
//                            String birthday = resp.optString("birthday");
//                            TextView birthdayTextView = (TextView) findViewById(R.id.profileBirthdayTextView);
//                            String[] tempArr;
//                            tempArr = String.valueOf(birthday).split("/");
//                            birthdayTextView.setText(tempArr[1] + " " + tempArr[0] + " " + tempArr[2]);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
        Bundle parameters = new Bundle();
        //parameters.putString("fields", "name,picture.width(500).height(500),birthday");
        parameters.putString("fields", "name,picture.width(500).height(500)");
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
