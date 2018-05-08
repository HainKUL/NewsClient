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

import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private String[] mStrs = {"aaa", "bbb", "ccc", "airsaid"};
    private SearchView mSearchView;
    private ListView mListView;
    @Override

    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButtonHome();
        mSearchView = (SearchView) findViewById(R.id.searchView);
        mListView = (ListView) findViewById(R.id.listView);
        mListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mStrs));
        mListView.setTextFilterEnabled(true);

        // 设置搜索文本监听
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                mStrs=null;
                //request(query);

                return false;
            }

            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText)){
                    mListView.setFilterText(newText);
                }else{
                    mListView.clearTextFilter();
                }
                return false;
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0)
                {
                    Intent myIntent=new Intent(view.getContext(),NewsOverviewActivity.class);
                    startActivityForResult(myIntent,0);
                }
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
                            JSONArray jArr=new JSONArray(response);
                            for(int i=0;i<5;i++) //here we just select the tpo 10
                            {
                                JSONObject jo=jArr.getJSONObject(i);
                                String NewsTitle=jo.getString("Title");
                                mStrs[i]=NewsTitle;
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

