package be.kuleuven.softdev.haientang.newsclient;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    ImageView homeIcon,thumbUpIcon;
    int newsID,likesNr;
    TextView newsTitle,newsTags,newsContent,newsLikes;
    EditText commentBoard;
    Button submitBut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_show);

        homeIcon = (ImageView) findViewById(R.id.home);
        thumbUpIcon = (ImageView) findViewById(R.id.likesIcon);
        newsID = getIntent().getExtras().getInt("newsID");
        newsTitle=(TextView) findViewById(R.id.Title);
        newsTags=(TextView) findViewById(R.id.Tags);
        newsContent = (TextView) findViewById(R.id.Content);
        newsLikes=(TextView) findViewById(R.id.likesNr);
        commentBoard = (EditText) findViewById(R.id.CommentBoard);
        submitBut = (Button) findViewById(R.id.ButSubmit);

        displayNews();
        addLike();
        addComments();//meanwhile, refresh comments list
        iconHome();//back to news_overview
    }

    public void displayNews() { //get news content from json server
        String url="http://api.a17-sd606.studev.groept.be/selectNewsToDisplay/"+newsID;

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @TargetApi(Build.VERSION_CODES.N)
                    public void onResponse(String response) {
                        try {
                            JSONArray jArr=new JSONArray(response);
                                JSONObject jo=jArr.getJSONObject(0);

                                String ttl=jo.getString("title");
                                String tg=jo.getString("tags");
                                String cnt=jo.getString("content");
                                likesNr=jo.getInt("likes");

                                newsTitle.setText(ttl);
                                newsTags.setText(tg);
                                newsContent.setText(Html.fromHtml(cnt, Html.FROM_HTML_MODE_LEGACY));
                                newsLikes.setText(""+likesNr);//set integer value into TextView
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Oops,please try again later!", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }

    public void addLike() {
        thumbUpIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likesNr++;
                String url="http://api.a17-sd606.studev.groept.be/addLikes/"+likesNr+"/"+newsID;

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            public void onResponse(String response) {
                                newsLikes.setText(""+likesNr);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Oops,please try again later!", Toast.LENGTH_SHORT).show();
                    }
                });
                queue.add(stringRequest);


            }
        });
    }

    public void addComments() {
        submitBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url="http://api.a17-sd606.studev.groept.be/addComments/"+newsID+"/"+commentBoard.getText().toString();

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            public void onResponse(String response) {
                                refreshCommentsList(commentBoard.getText().toString());
                                commentBoard.setText("");
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Oops,please try again later!", Toast.LENGTH_SHORT).show();
                    }
                });
                queue.add(stringRequest);
            }
        });
    }

    public void refreshCommentsList(String comment) {//in database, sort by comment id is equivalent to sorting by time. the last id is the newset comment
        String url="http://api.a17-sd606.studev.groept.be/refreshComments/"+newsID;
        // create a new textview
        final TextView newComment = new TextView(this);
        // set some properties of rowTextView or something
        newComment.setText(comment);
        // add the textview to the linearlayout
        LinearLayout ll = (LinearLayout) findViewById(R.id.myLinearLayout);
        ll.addView(newComment);
    }

    public void iconHome() {
        homeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//switch to news_overview activity
                Intent intent = new Intent(NewsShowActivity.this, NewsOverviewActivity.class);
                startActivity(intent);
            }
        });
    }
}

