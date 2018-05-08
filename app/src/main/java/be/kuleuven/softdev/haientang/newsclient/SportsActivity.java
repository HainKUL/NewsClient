package be.kuleuven.softdev.haientang.newsclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.util.ArrayList;

public class SportsActivity extends AppCompatActivity {

    //id变量，用来存储用以传输到newsshow的id
    private int[] ids=new int[2];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sports_overviewctivity);
        RequestsTopTwoNews("http://api.a17-sd606.studev.groept.be/selectTpoTwoNews/"+"Sports");
        GoToNewsOne();
        GoToNewsTwo();
        ButtonHome();
    }

    public void GoToNewsOne() {
        TextView NewsOne = (TextView) findViewById(R.id.SportsNewsOne);
        NewsOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//switch to new activity
                Intent intent = new Intent(SportsActivity.this, NewsShowActivity.class);
                intent.putExtra("id",ids[0]);
                startActivity(intent);
            }
        });
    }

    public void GoToNewsTwo() {
        TextView NewsTwo = (TextView) findViewById(R.id.SportsNewsTwo);
        NewsTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//switch to new activity
                Intent intent = new Intent(SportsActivity.this, NewsShowActivity.class);
                intent.putExtra("id",ids[1]);
                startActivity(intent);
            }
        });
    }

    public void RequestsTopTwoNews(String url) {

        // the arraylist would store five textviews which is applied to demonstrate the news

        // assign the parameters of title textview
        final TextView title1 = (TextView) findViewById(R.id.SportsNewsOne);
        final TextView title2 = (TextView) findViewById(R.id.SportsNewsTwo);

        final ArrayList<TextView> titleViewList = new ArrayList<TextView>();
        titleViewList.add(title1);
        titleViewList.add(title2);

        //assign the parameters of  tags textview
        final TextView tagView1 = (TextView) findViewById(R.id.SportsTagOne);
        final TextView tagView2 = (TextView) findViewById(R.id.SportsTagTwo);

        final ArrayList<TextView> textViewList = new ArrayList<TextView>();
        textViewList.add(tagView1);
        textViewList.add(tagView2);


// Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {

                    public void onResponse(String response) {

                        String words="";
                        try {
                            JSONArray jArr=new JSONArray(response);
                            for(int i=0;i<2;i++) //here we just select the tpo 10
                            {
                                JSONObject jo=jArr.getJSONObject(i);
                                String newsTitle=jo.getString("Title");
                                String newsTag=jo.getString("Tag");
                                ids[i]=jo.getInt("idNews");
                                titleViewList.get(i).setText(newsTitle);
                                textViewList.get(i).setText(newsTag);

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

    public void ButtonHome()
    {
        ImageView SearchBut=(ImageView) findViewById(R.id.sportsToHome);
        SearchBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//switch to new activity
                Intent intent = new Intent(SportsActivity.this, NewsOverviewActivity.class);
                startActivity(intent);
            }
        });
    }
}