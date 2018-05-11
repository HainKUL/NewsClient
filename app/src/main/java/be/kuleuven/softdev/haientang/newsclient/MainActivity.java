package be.kuleuven.softdev.haientang.newsclient;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

public class MainActivity extends AppCompatActivity {

    //declare globle variables
    Button loginBut,registerBut,guestBut;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //1. get references to the buttons
        loginBut=(Button) findViewById(R.id.butLogin);
        registerBut=(Button) findViewById(R.id.butRegister);
        guestBut=(Button) findViewById(R.id.butGuest);

        //2. set click listeners to the buttons
        loginBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//here to create other dialog
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
                            LoginCheck(mEmail.getText().toString(),mpasswd.getText().toString());
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

        registerBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        guestBut.setOnClickListener(new View.OnClickListener() {//need to pass information telling the database the guest
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,NewsOverviewActivity.class);
                startActivity(intent);
            }
        });

    }

    public void LoginCheck(String emailCheck,String passwdCheck) {
        String url="http://api.a17-sd606.studev.groept.be/LoginCheck/"+emailCheck+"/"+passwdCheck;
        RequestQueue queue= Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jArr=new JSONArray(response);
                            if(jArr.length()==0){//email or passwd wrong
                                Toast.makeText(getApplicationContext(), "Please enter correct Email or password!", Toast.LENGTH_SHORT).show();
                            }else if(jArr.length()==1){
                                Toast.makeText(getApplicationContext(), "Login successful!", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(getApplicationContext(),NewsOverviewActivity.class);
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