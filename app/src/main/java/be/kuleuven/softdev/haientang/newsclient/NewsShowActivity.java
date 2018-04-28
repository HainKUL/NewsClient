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

public class NewsShowActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_show);
        Requests("http://api.a17-sd606.studev.groept.be/news");
        ButtonComment();
        setComment();
    }


    public void Requests(String url) {
        final TextView mTextView = (TextView) findViewById(R.id.Content);
// Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);


// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {

                    public void onResponse(String response) {

                        String words="l";
                        try {
                            JSONArray jArr=new JSONArray(response);
                            for(int i=0;i<jArr.length();i++)
                            {
                                JSONObject jo=jArr.getJSONObject(i);
                                String id=jo.getString("NewsContent");
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

    public void ButtonComment() {
        Button CommentBut = (Button) findViewById(R.id.ButtonComment);
        CommentBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//switch to new activity
                Intent intent = new Intent(NewsShowActivity.this, AddCommentActivity.class);
                startActivity(intent);

            }
        });
    }

    public void setComment()
    {
        final TextView tv=(TextView) findViewById(R.id.CommentBoard);
        String txt=null;
        txt=getIntent().getExtras().getString("comment");
        tv.setText(txt);

    }

}
