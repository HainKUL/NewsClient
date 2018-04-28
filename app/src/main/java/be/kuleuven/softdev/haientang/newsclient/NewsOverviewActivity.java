package be.kuleuven.softdev.haientang.newsclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class NewsOverviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_overview);
        ButtonSports();
        ButtonChina();
        ButtonEconomy();
        TextView tv = (TextView) findViewById(R.id.NewsDemo);
        Requests("http://api.a17-sd606.studev.groept.be/database");
        //doTheWorkInSql();
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


    public void Requests(String url) {
        final TextView mTextView = (TextView) findViewById(R.id.NewsDemo);
// ...

// Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);


// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {

                    public void onResponse(String response) {

                        String words="";
                        try {
                            JSONArray jArr=new JSONArray(response);
                            for(int i=0;i<jArr.length();i++)
                            {
                                JSONObject jo=jArr.getJSONObject(i);
                                String id=jo.getString("firstName");
                                //String txt=jo.getString("txt");
                                words=words+id;


                            }




                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mTextView.setText(words);


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mTextView.setText("That didn't work!");
            }
        });


// Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    public void doTheWorkInSql()
    {
        final TextView mTextView = (TextView) findViewById(R.id.NewsDemo);
        Connection c=null;
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            c= DriverManager.getConnection("jdbc:mysql://studev.groept.be:3306/a17_sd606","a17_sd606","a17_sd606");
            Statement stmt=c.createStatement();
            ResultSet rs=stmt.executeQuery("SELECT * FROM news");
            while(rs.next())
            {
                mTextView.setText(rs.getString("id"));
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


}
