package be.kuleuven.softdev.haientang.newsclient;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryActivity extends AppCompatActivity {

    static private int[] ids=new int[2];
    private String category=new String();
    private TextView title;
    final private ArrayList<newsInfo> newsList=new ArrayList<>();

    private ListView listView;
    private  SimpleAdapter simpleAdapter;
    final static private ArrayList<Map<String,Object>> datas=new ArrayList<>();


    //private List<Map<String,Object>> mData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        category=getIntent().getExtras().getString("category");
        title=(TextView) findViewById(R.id.CategoryTitle);
        title.setText(category);
        RequestsTopTwoNews("http://api.a17-sd606.studev.groept.be/selectTopTwoNews/"+category);

        listView=(ListView) findViewById(R.id.newsList);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //点击listview中的内容转到相关地方
                Intent myIntent = new Intent(view.getContext(), NewsShowActivity.class);
                myIntent.putExtra("newsID", ids[position]);
                startActivityForResult(myIntent, position);
            }
        });



        generateSimpleAdapter();



        /*SimpleAdapter adapter = new SimpleAdapter(this,getData(),R.layout.activity_category,
                new String[]{"newsTitle","newsTag","newsImage"},
                new int[]{R.id.newsTitle,R.id.newsTag,R.id.newsImage});
        setListAdapter(adapter);*/

        ButtonHome();
    }



    /*public void GoToNewsOne() {
        TextView NewsOne = (TextView) findViewById(R.id.CategoryNewsOne);
        NewsOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//switch to new activity
                Intent intent = new Intent(CategoryActivity.this, NewsShowActivity.class);
                intent.putExtra("newsID",ids[0]);
                startActivity(intent);
            }
        });
    }

    public void GoToNewsTwo() {
        TextView NewsTwo = (TextView) findViewById(R.id.CategoryNewsTwo);
        NewsTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//switch to new activity
                Intent intent = new Intent(CategoryActivity.this, NewsShowActivity.class);
                intent.putExtra("newsID",ids[1]);
                startActivity(intent);
            }
        });
    }*/

    public void RequestsTopTwoNews(String url) {

// Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {

                    public void onResponse(String response) {

                        try {
                            JSONArray jArr=new JSONArray(response);
                            for(int i=0;i<2;i++) //here we just select the tpo 10
                            {
                                JSONObject jo=jArr.getJSONObject(i);
                                String newsTitle=jo.getString("title");
                                String newsTag=jo.getString("tags");
                                ids[i]=jo.getInt("newsID");

                                Map<String,Object> data = new HashMap<>();
                                data.put("pic",R.drawable.home);
                                data.put("text",newsTitle);
                                datas.add(data);

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
        ImageView SearchBut=(ImageView) findViewById(R.id.Ima_Category);
        SearchBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//switch to new activity
                Intent intent = new Intent(CategoryActivity.this, NewsOverviewActivity.class);
                startActivity(intent);
            }
        });
    }

    private void generateSimpleAdapter(){
        simpleAdapter = new SimpleAdapter(this,datas,
                R.layout.item,new String[]{"pic","text"},
                new int[]{R.id.imageView,R.id.title});

        listView.setAdapter(simpleAdapter);
    }



}
