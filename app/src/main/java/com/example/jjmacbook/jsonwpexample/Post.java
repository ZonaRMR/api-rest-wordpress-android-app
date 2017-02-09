package com.example.jjmacbook.jsonwpexample;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.Map;

public class Post extends AppCompatActivity {

    TextView title;
    WebView content;
    ProgressDialog progressDialog;
    Gson gson;
    Map<String, Object> mapPost;
    Map<String, Object> mapTitle;
    Map<String, Object> mapContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        final String id = getIntent().getExtras().getString("id");

        title = (TextView) findViewById(R.id.textViewTitle);
        content = (WebView) findViewById(R.id.content);

        progressDialog = new ProgressDialog(Post.this);
        progressDialog.setMessage("Cargando...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        String url = "http://www.hojiblanca.com/wp-json/wp/v2/posts/" + id + "?fields=title,content";

        StringRequest myRequest = new StringRequest(Request.Method.GET, url,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        gson = new Gson();
                        mapPost = (Map<String, Object>) gson.fromJson(response, Map.class);
                        mapTitle = (Map<String, Object>) mapPost.get("title");
                        mapContent = (Map<String, Object>) mapPost.get("content");

                        title.setText(mapTitle.get("rendered").toString());
                        content.loadData(mapContent.get("rendered").toString(), "text/html; charset=utf-8", "utf-8");

                        progressDialog.dismiss();
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(Post.this, id, Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(myRequest);

    }
}
