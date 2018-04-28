package be.kuleuven.softdev.haientang.newsclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ChinaActivity extends AppCompatActivity {

    public String content=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_china);
        GoToNewsOne();
    }

    public void GoToNewsOne() {
        TextView NewsOne = (TextView) findViewById(R.id.ChinaNewsOne);
        NewsOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//switch to new activity

                Intent intent = new Intent(ChinaActivity.this, NewsShowActivity.class);
                Requests("http://api.a17-sd606.studev.groept.be/database");
                intent.putExtra("Content",content);
                startActivity(intent);

            }
        });
    }

    public void Requests(String url)
    {
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {

                    public void onResponse(String response) {
                        try {
                            JSONArray jArr=new JSONArray(response);
                            for(int i=0;i<jArr.length();i++)
                            {
                                JSONObject jo=jArr.getJSONObject(i);
                                String txt=jo.getString("firstName");
                                //String txt=jo.getString("txt");
                                content=content+txt;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                content="That didn't work!";
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

}
