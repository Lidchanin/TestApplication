package com.bignerdranch.android.testapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MarkerActivity extends AppCompatActivity {
    private String message;
    private String image;
    private String createdTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker);

        Intent intent = getIntent();
        final int position = intent.getIntExtra("position", 0);

        GraphRequest request = GraphRequest.newGraphPathRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/feed",
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        try {
                            JSONObject resp = response.getJSONObject();
                            JSONArray data = resp.getJSONArray("data");
                            JSONObject temp = data.getJSONObject(position);
                            //message
                            if (data.getJSONObject(position).has("message")) {
                                message = temp.optString("message");
                                TextView messageTV = (TextView) findViewById(R.id.postMessageTextView);
                                messageTV.setText(String.valueOf(message));
                            } else {
                                TextView messageTV = (TextView) findViewById(R.id.postMessageTextView);
                                messageTV.setVisibility(View.INVISIBLE);
                            }
                            //picture
                            if (data.getJSONObject(position).has("full_picture")) {
                                image = temp.optString("full_picture");
                                ImageView imageIV = (ImageView) findViewById(R.id.postImageView);
                                new ImageDownloader(imageIV).execute(image);
                            } else {
                                //if post does not a picture
                                ImageView picture = (ImageView) findViewById(R.id.postImageView);
                                picture.setVisibility(View.INVISIBLE);
                            }
                            //created time
                            if (data.getJSONObject(position).has("created_time")) {
                                createdTime = temp.optString("created_time");
                                TextView createdTimeTV = (TextView) findViewById(R.id.createdTimeTextView);
                                //Разобраться с выводом даты в нормальном виде(split("T") работает некорректно)
                                createdTimeTV.setText(String.valueOf(createdTime));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "message,full_picture,created_time");
        request.setParameters(parameters);
        request.executeAsync();

    }


}