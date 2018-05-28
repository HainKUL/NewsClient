package be.kuleuven.softdev.haientang.newsclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;

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

public class SearchActivity extends AppCompatActivity {

    private ArrayList<String> mStrs = new ArrayList<>();
    private ArrayList<Integer> ids = new ArrayList<>();
    private SearchView mSearchView;
    private ListView mListView;
    private int userID;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButtonHome();
        mSearchView = (SearchView) findViewById(R.id.searchView);
        mListView = (ListView) findViewById(R.id.listView);
        final ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mStrs);
        mListView.setAdapter(adapter);
        mListView.setTextFilterEnabled(true);
        userID=getIntent().getExtras().getInt("userID");

        // 设置搜索文本监听
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {

                mStrs.clear();
                ids.clear();
                request("http://api.a17-sd606.studev.groept.be/search/"+query);
                adapter.notifyDataSetChanged();
                return false;
            }

            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText)){

                }else{

                    //mListView.clearTextFilter();
                }
                return false;
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //点击listview中的内容转到相关地方

                    Intent myIntent=new Intent(view.getContext(),NewsShowActivity.class);
                    myIntent.putExtra("newsID",ids.get(position));
                    myIntent.putExtra("userID",userID);
                    startActivityForResult(myIntent,position);
            }
        });
    }

    public void request(String url)
    {
        RequestQueue queue = Volley.newRequestQueue(this);
// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {

                    public void onResponse(String response) {


                        try {
                            //mStrs.clear();
                            JSONArray jArr=new JSONArray(response);
                            for(int i=0;i<jArr.length();i++) //here we just select the tpo 10
                            {
                                JSONObject jo=jArr.getJSONObject(i);
                                String NewsTitle=jo.getString("title");
                                int id=jo.getInt("newsID");
                                if(!NewsTitle.equals("null"))
                                {
                                    ids.add(id);
                                    mStrs.add(NewsTitle);
                                }

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
        ImageView SearchBut=(ImageView) findViewById(R.id.searchToHome);
        SearchBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//switch to new activity
                Intent intent = new Intent(SearchActivity.this, NewsOverviewActivity.class);
                startActivity(intent);
            }
        });
    }

}

