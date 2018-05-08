package be.kuleuven.softdev.haientang.newsclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class NewsOverviewActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_overview);

        ButtonSports();
        ButtonChina();
        ButtonEconomy();
        ButtonSearch();
        RequestsTopTenNews("http://api.a17-sd606.studev.groept.be/selectTopTenNews");
    }

    public void ButtonSearch()
    {
        ImageView SearchBut=(ImageView) findViewById(R.id.searchIcon);
        SearchBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//switch to new activity
                Intent intent = new Intent(NewsOverviewActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });
    }

    public void ButtonSports() {
        Button SportsBut = (Button) findViewById(R.id.Sports);
        SportsBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//switch to new activity
                Intent intent = new Intent(NewsOverviewActivity.this, SportsActivity.class);
                startActivity(intent);
            }
        });
    }

    public void ButtonEconomy()
    {
        Button EconomyBut = (Button) findViewById(R.id.Economy);
        EconomyBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//switch to new activity
                Intent intent = new Intent(NewsOverviewActivity.this, EconomyActivity.class);
                startActivity(intent);
            }
        });
    }

    public void ButtonChina()
    {
        Button ChinaBut = (Button) findViewById(R.id.China);
        ChinaBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//switch to new activity
                Intent intent = new Intent(NewsOverviewActivity.this, ChinaActivity.class);
                startActivity(intent);
            }
        });
    }


    public void RequestsTopTenNews(String url) {

        // the arraylist would store five textviews which is applied to demonstrate the news

        // assign the parameters of textview
        final TextView tn1 = (TextView) findViewById(R.id.TopNewsOne);
        final TextView tn2 = (TextView) findViewById(R.id.TopNewsTwo);
        final TextView tn3 = (TextView) findViewById(R.id.TopNewsThree);
        final TextView tn4 = (TextView) findViewById(R.id.TopNewsFour);
        final TextView tn5 = (TextView) findViewById(R.id.TopNewsFive);
        final ArrayList<TextView> textViewList = new ArrayList<TextView>();
        textViewList.add(tn1);
        textViewList.add(tn2);
        textViewList.add(tn3);
        textViewList.add(tn4);
        textViewList.add(tn5);

// Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {

                    public void onResponse(String response) {

                        String words="";
                        try {
                            JSONArray jArr=new JSONArray(response);
                            for(int i=0;i<5;i++) //here we just select the tpo 10
                            {
                                JSONObject jo=jArr.getJSONObject(i);
                                String NewsContent=jo.getString("Title");
                                textViewList.get(i).setText(NewsContent);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
    //之后再增加tag 等东西

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {}
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);

    }


}
