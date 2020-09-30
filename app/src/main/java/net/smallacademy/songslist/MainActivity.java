package net.smallacademy.songslist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<Song> songs;
    private static String JSON_URL = "http://moodytest-env.eba-mmzgp9iv.eu-central-1.elasticbeanstalk.com/api/categories";

    Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.songsList);
        songs = new ArrayList<>();
            extractSongs();
        }

        private void extractSongs() {
            RequestQueue queue = Volley.newRequestQueue(this);
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, JSON_URL, null, new Response.Listener<JSONArray>() {

            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject songObject = response.getJSONObject(i);

                        Song song = new Song();
                        song.setTitle(songObject.getString("categories"));
//                        song.setArtists(songObject.getString("artists".toString()));
//                        song.setCoverImage(songObject.getString("cover_image"));
//                        song.setSongURL(songObject.getString("url"));
                        songs.add(song);

                        System.out.println(song);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                adapter = new Adapter(getApplicationContext(),songs);
                recyclerView.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tag", "HATALI : " + error.getMessage());
            }
        }){
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1bmlxdWVfbmFtZSI6Ikc3SEFpVGRrb3VzWnF3cDlOYlRkaitETllXWE5aSVFhaVJ1QzI1aVFoaDA9IiwibmJmIjoxNjAxNDA4NTU1LCJleHAiOjE2MDE0OTQ5NTUsImlhdCI6MTYwMTQwODU1NX0.KpjZ4T8XdDsb3KfgtOHWLGaR2hG0bVcTswutm-KtBcI");
                    params.put("Content-Type", "application/json");
                    return params;
                }
            };


        queue.add(jsonArrayRequest);
        System.out.print(jsonArrayRequest);


    }
}
