package be.kuleuven.softdev.haientang.newsclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

public class NewsOverviewActivity extends AppCompatActivity {
    ArrayList<ImageView> TNImages = new ArrayList<ImageView>();
    ArrayList<TextView> TNTitles = new ArrayList<TextView>();
    ArrayList<TextView> TNTags = new ArrayList<TextView>();
    ArrayList<TextView> TNDates = new ArrayList<TextView>();
    ImageView SearchIcon;
    Button SportsBut,EconomyBut,ChinaBut;
    int[] ids;
    //int i;// make gloable to avoid final
    //int[] likesNr;//to store id and likesNr for each Top News

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_overview);


        init();//initialise all references

        //new method to display top 5 breaking news on newsOverview
        requestsBreakingNews("http://api.a17-sd606.studev.groept.be/selectBreakingNews");

        ButtonSearch();
        ButtonSports();
        ButtonEconomy();
        ButtonChina();

        clickOnEachNews();
    }

    private void init(){
        SearchIcon=(ImageView) findViewById(R.id.searchIcon);
        SportsBut = (Button) findViewById(R.id.Sports);
        EconomyBut = (Button) findViewById(R.id.Economy);
        ChinaBut = (Button) findViewById(R.id.China);

        TNImages.add((ImageView) findViewById(R.id.TNImage1));
        TNImages.add((ImageView) findViewById(R.id.TNImage2));
        TNImages.add((ImageView) findViewById(R.id.TNImage3));
        TNImages.add((ImageView) findViewById(R.id.TNImage4));
        TNImages.add((ImageView) findViewById(R.id.TNImage5));

        TNTitles.add((TextView) findViewById(R.id.TNTitle1));
        TNTitles.add((TextView) findViewById(R.id.TNTitle2));
        TNTitles.add((TextView) findViewById(R.id.TNTitle3));
        TNTitles.add((TextView) findViewById(R.id.TNTitle4));
        TNTitles.add((TextView) findViewById(R.id.TNTitle5));

        TNTags.add((TextView) findViewById(R.id.TNTag1));
        TNTags.add((TextView) findViewById(R.id.TNTag2));
        TNTags.add((TextView) findViewById(R.id.TNTag3));
        TNTags.add((TextView) findViewById(R.id.TNTag4));
        TNTags.add((TextView) findViewById(R.id.TNTag5));

        TNDates.add((TextView) findViewById(R.id.TNDate1));
        TNDates.add((TextView) findViewById(R.id.TNDate2));
        TNDates.add((TextView) findViewById(R.id.TNDate3));
        TNDates.add((TextView) findViewById(R.id.TNDate4));
        TNDates.add((TextView) findViewById(R.id.TNDate5));

        ids=new int[5];
        //likesNr=new int[5];
    }

    public void requestsBreakingNews(String url) {//display top 5 breaking news on newsOverview
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jArr=new JSONArray(response);
                            for(int i=0;i<5;i++) {
                                JSONObject jo=jArr.getJSONObject(i);
                                String newsTitle=jo.getString("title");
                                String newsTags=jo.getString("tags");
                                String newsDate=jo.getString("date");

                                ids[i]=jo.getInt("newsID");
                                //likesNr[i]=jo.getInt("likes");
                                TNTitles.get(i).setText(newsTitle);
                                TNTags.get(i).setText(newsTags);
                                TNDates.get(i).setText(newsDate);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }}
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {}
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void ButtonSearch()
    {
        SearchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewsOverviewActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });
    }

    public void ButtonSports() {
        SportsBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewsOverviewActivity.this, CategoryActivity.class);
                intent.putExtra("category","sports");
                startActivity(intent);
            }
        });
    }

    public void ButtonEconomy() {
        EconomyBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewsOverviewActivity.this, CategoryActivity.class);
                intent.putExtra("category","economy");
                startActivity(intent);
            }
        });
    }

    public void ButtonChina() {
        ChinaBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewsOverviewActivity.this, CategoryActivity.class);
                intent.putExtra("category","china");
                startActivity(intent);
            }
        });
    }

    public void clickOnEachNews(){
        for(int i=0;i<5;i++){
            final int j = i;
            TNTitles.get(i).setOnClickListener(new View.OnClickListener() {//search full news by id( and title)
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(getApplicationContext(),NewsShowActivity.class);
                    intent.putExtra("newsID",ids[j]);
                    //intent.putExtra("newsLikes",likesNr[i]);
                    startActivity(intent);
                }
            });
        }
        //i=j;

    }
}
