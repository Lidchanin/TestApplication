package com.bignerdranch.android.testapplication.posts.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.bignerdranch.android.testapplication.R;
import com.bignerdranch.android.testapplication.posts.adapter.ThreeHorizontalTextViewsAdapter;
import com.bignerdranch.android.testapplication.posts.adapter.ThreeStrings;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PostsActivity extends Activity {

    private int currentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);

        Bundle params = new Bundle();
        params.putString("fields", "from,story,message");
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/feed",
                params,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {

                        final List<ThreeStrings> threeStringsList = new ArrayList<>();
                        try {
                            JSONObject resp = response.getJSONObject();
                            JSONArray data = resp.getJSONArray("data");
                            JSONArray dataList = new JSONArray(data.toString());
                            int datLen = dataList.length();
                            String[] from = new String[datLen];
                            String[] story = new String[datLen];
                            String[] message = new String[datLen];
                            String[] id = new String[datLen];
                            for (int i = 0; i < dataList.length(); i++) {
                                JSONObject temp1 = data.getJSONObject(i);
                                if (data.getJSONObject(i).has("from")) {
                                    JSONObject temp2 = temp1.getJSONObject("from");
                                    from[i] = temp2.optString("name");
                                }
                                if (data.getJSONObject(i).has("story")) {
                                    story[i] = temp1.optString("story");
                                }
                                if (data.getJSONObject(i).has("message")) {
                                    message[i] = temp1.optString("message");
                                }
                                ThreeStrings threeStrings = new ThreeStrings(from[i], story[i], message[i]);
                                threeStringsList.add(threeStrings);
                                ListView listView = (ListView) findViewById(R.id.listView);
                                ThreeHorizontalTextViewsAdapter threeHorizontalTextViewsAdapter = new ThreeHorizontalTextViewsAdapter(
                                        PostsActivity.this,
                                        R.layout.three_horizontal_text_views_layout,
                                        threeStringsList);
                                listView.setAdapter(threeHorizontalTextViewsAdapter);
//                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                                        currentPosition = position;
//                                        Intent intent = new Intent(PostsActivity.this, .class);
//                                        intent.putExtra("pos", currentPosition);
//                                        startActivity(intent);
//                                    }
//                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();
    }
}