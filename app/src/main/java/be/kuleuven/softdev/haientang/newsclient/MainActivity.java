package be.kuleuven.softdev.haientang.newsclient;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    Button loginBut,registerBut,guestBut,imageBut,phpBut;
    TextView adverTV;
    ImageView profile;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initAllRef();

        clickButtonLogin();
        clickButtonRegister();
        clickButtonGuest();
        clickTextViewAdvertise();
    }

    private void initAllRef() {
        loginBut=(Button) findViewById(R.id.butLogin);
        registerBut=(Button) findViewById(R.id.butRegister);
        guestBut=(Button) findViewById(R.id.butGuest);
        adverTV=(TextView) findViewById(R.id.advertiser);
        profile=(ImageView) findViewById(R.id.profile);

    }

    private void clickButtonLogin() {
        loginBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder=new AlertDialog.Builder(MainActivity.this);//create alert dialog
                //create a new view,login view(mView) instead of main_activity view
                View mView=getLayoutInflater().inflate(R.layout.dialog_login,null);//referencing the alert dialog to the login dialog
                //define the view inside the login layout
                final EditText mEmail=(EditText) mView.findViewById(R.id.etEmail);
                final EditText mpasswd=(EditText) mView.findViewById(R.id.etPasswd);
                Button mLogin=(Button) mView.findViewById(R.id.butLogin);

                mLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!mEmail.getText().toString().isEmpty()&&!mpasswd.getText().toString().isEmpty()) {
                            loginCheck(mEmail.getText().toString(),mpasswd.getText().toString());
                        }else{
                            Toast.makeText(MainActivity.this, "Please fill in any empty fields!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                mBuilder.setView(mView);
                AlertDialog dialog=mBuilder.create();
                dialog.show();
            }
        });
    }

    private void clickButtonRegister() {
        registerBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void clickButtonGuest() {
        guestBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//send guest info to database
                String URL="http://api.a17-sd606.studev.groept.be/guestsLogin";
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(getApplicationContext(), "You can browser news as a guest now!", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(MainActivity.this,NewsOverviewActivity.class);
                                intent.putExtra("userID",0);
                                startActivity(intent);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Oops,please try again later!", Toast.LENGTH_SHORT).show();
                    }
                });
                queue.add(stringRequest);// Add the request to the RequestQueue.
            }
        });
    }

    private void clickTextViewAdvertise() {
        adverTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),EditorActivity.class);
                startActivity(intent);
            }
        });
    }

    public void loginCheck(String emailCheck,String passwdCheck) {
        String url="http://api.a17-sd606.studev.groept.be/loginCheck/"+emailCheck+"/"+passwdCheck;

        RequestQueue queue= Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jArr=new JSONArray(response);
                            JSONObject jo=jArr.getJSONObject(0);
                            if(jArr.length()==0){//email or passwd wrong
                                Toast.makeText(getApplicationContext(), "Please enter correct Email or password!", Toast.LENGTH_SHORT).show();
                            }else if(jArr.length()==1){
                                Toast.makeText(getApplicationContext(), "Login successful!", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(getApplicationContext(),NewsOverviewActivity.class);
                                int userID=jo.getInt("userID");
                                intent.putExtra("userID",userID);
                                //showImageByName("http://a17-sd606.studev.groept.be/User/",userID);
                                startActivity(intent);
                            }
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
}