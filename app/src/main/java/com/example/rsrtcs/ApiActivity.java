package com.example.rsrtcs;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ApiActivity extends AppCompatActivity {

    TextView textView;
    Button btnParse;
    RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api);

        textView = findViewById(R.id.textView1);
        btnParse = findViewById(R.id.parse);

        mQueue = Volley.newRequestQueue(this);

        btnParse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 jsonParse();
            }
        });
    }

    private void jsonParse() {

      //  String url = "http://122.15.66.234:8080/RFIDAPI";
       // String url = "https://jsonplaceholder.typicode.com/";
        String url = "https://projects.vishnusivadas.com/testing/read.php";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONObject response) {
                        textView.setText("Response is = " + response);
                        try {
                           // JSONArray array = response.getJSONArray("busType");
                            JSONArray array = response.getJSONArray("posts");

                            for (int i = 0; i < array.length(); i++){
                                JSONObject object = array.getJSONObject(i);

//                                String busTypeCd = object.getString("busTypeCd");
//                                String busTypeName = object.getString("busTypeName");

                                String busTypeCd = object.getString("userId");
                                String busTypeName = object.getString("title");

                              //  textView.append(busTypeCd + ", " + busTypeName + "\n\n");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
            }
        });
    }
}