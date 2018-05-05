package be.kuleuven.softdev.haientang.newsclient;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegisterActivity extends AppCompatActivity {//maybe add extra features, such as sending verification code by email

    //declare globle variables here
    EditText firstNameTxt,surnameTxt,emailTxt,passwd,rePasswd;
    Button submitBut;
    String URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //1. get references to the buttons and textbox
        firstNameTxt = (EditText) findViewById(R.id.firstName);
        surnameTxt = (EditText) findViewById(R.id.surname);
        emailTxt = (EditText) findViewById(R.id.email);
        passwd = (EditText) findViewById(R.id.passwd);
        rePasswd = (EditText) findViewById(R.id.repeatPasswd);
        submitBut = (Button) findViewById(R.id.butSubmit);

        //check email format in java
        emailTxt.addTextChangedListener(new TextWatcher() {//haien.tang@student.kuleuven.be
            String pattern="^[a-zA-Z0-9]+[-|_|.]?[a-zA-Z0-9]+[@]{1}[a-zA-Z0-9]+[.]{1}[a-zA-Z]+[.]?[a-zA-Z]+";//"+" means [1,infinite] times
            Pattern p= Pattern.compile(pattern);

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable s) {
                String email=emailTxt.getText().toString();
                Matcher m=p.matcher(email);
                if(m.matches())
                    emailTxt.setTextColor(Color.BLACK);
                else
                    emailTxt.setTextColor(Color.RED);
            }
        });
/*
        //check passwd format in java      at least one upper case with combination with numerial numbers,lenght longer than 8,
        passwd.addTextChangedListener(new TextWatcher() {
            String pattern="^[A-Z]+[@#$%^&+=!]+[a-z]+[a-zA-Z0-9]+";
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
*/
        //button on click,first check empty field, then check email format, check passwd consistence, check email in database
        submitBut.setOnClickListener(new View.OnClickListener() {
            Boolean emailOK=false;
            @Override
            public void onClick(View view) {
                //1. check empty fields
                if(firstNameTxt.getText().toString().isEmpty()
                        ||surnameTxt.getText().toString().isEmpty()
                        ||emailTxt.getText().toString().isEmpty()
                        ||passwd.getText().toString().isEmpty()
                        ||rePasswd.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please fill in any empty fields!", Toast.LENGTH_SHORT).show();
                }
                //2.check email format in java
                else if (emailTxt.getCurrentTextColor()==Color.RED){
                    Toast.makeText(getApplicationContext(), "Please enter valid email!", Toast.LENGTH_SHORT).show();
                }

                //3.check passwd consistence
                else if(!passwd.getText().toString().equals(rePasswd.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Your password doesn't match!",Toast.LENGTH_SHORT).show();
                }

                //4.check existing emails in database
                else if(!checkEmailsOK(emailTxt.getText().toString())){//new method, see below
                    Toast.makeText(getApplicationContext(), "Email already existed!", Toast.LENGTH_SHORT).show();
                }
                //5.send request to database
                else{
                    Requests("http://api.a17-sd606.studev.groept.be/UsersRegister/");
                    switchToLogin();//new method to switch to login dialog
                }}
        });
    }

    private boolean checkEmailsOK(String email){
        return true;
    }

    public void Requests(String url) {
        URL=url+firstNameTxt.getText()+"/"+surnameTxt.getText()+"/"+emailTxt.getText()+"/"+passwd.getText();
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(RegisterActivity.this, "Registration succeed!", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(RegisterActivity.this, "Oops,please try again later!", Toast.LENGTH_SHORT).show();
                }
            });
        queue.add(stringRequest);// Add the request to the RequestQueue.
    }

    public void switchToLogin()
    {
        AlertDialog.Builder mBuilder=new AlertDialog.Builder(this);//create alert dialog
        View mView=getLayoutInflater().inflate(R.layout.dialog_login,null);//referencing the alert dialog to the login dialog
        //define the view inside the login layout
        final EditText mEmail=(EditText) mView.findViewById(R.id.etEmail);
        final EditText mpasswd=(EditText) mView.findViewById(R.id.etPasswd);
        Button mLogin=(Button) mView.findViewById(R.id.butLogin);

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//here to switch to another activity or not
                if(!mEmail.getText().toString().isEmpty()&&!mpasswd.getText().toString().isEmpty())
                {
                    Toast.makeText(RegisterActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(RegisterActivity.this,NewsOverviewActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(RegisterActivity.this, "Please fill in any empty fields!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mBuilder.setView(mView);
        AlertDialog dialog=mBuilder.create();
        dialog.show();
    }
}
