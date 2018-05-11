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

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    //declare globle variables here
    EditText firstNameTxt,surnameTxt,emailTxt,passwd,rePasswd;
    TextView multiText;
    Button submitBut;
    String URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //get references to the buttons and textbox
        firstNameTxt = (EditText) findViewById(R.id.firstName);
        surnameTxt = (EditText) findViewById(R.id.surname);
        emailTxt = (EditText) findViewById(R.id.email);
        passwd = (EditText) findViewById(R.id.passwd);
        rePasswd = (EditText) findViewById(R.id.repeatPasswd);
        multiText=(TextView) findViewById(R.id.multiline);
        submitBut = (Button) findViewById(R.id.butSubmit);

        //dynamically check email format in java
        emailTxt.addTextChangedListener(new TextWatcher() {//haien.tang@student.kuleuven.be or r0650137@kuleuven.be
            String reg="^[a-zA-Z0-9]+[-|_|.]?[a-zA-Z0-9]+[@]{1}[a-zA-Z0-9]+[.]{1}[a-zA-Z]+[.]?[a-zA-Z]+";//"+" means [1,infinite] times
            //Pattern p= Pattern.compile(reg);
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable s) {
                boolean bool=Pattern.matches(reg,emailTxt.getText().toString());
                //Matcher m=p.matcher(emailTxt.getText().toString());
                //if(m.matches())
                if(bool)
                    emailTxt.setTextColor(Color.BLACK);
                else
                    emailTxt.setTextColor(Color.RED);
            }
        });

        //dynamically check passwd format in java
        passwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                String password=passwd.getText().toString();
                boolean isUpper = Pattern.matches("^[A-Z].+",password);//start with uppercase
                boolean isDigitNoSpace = Pattern.matches(".+[0-9]+.+",password);//contains digit(s) and no space
                boolean isLength = (password.length()>7&&password.length()<17);//8<=lenght<=16
                if(!isUpper||!isDigitNoSpace||!isLength) {
                    multiText.setTextColor(Color.RED);
                }else
                    multiText.setTextColor(Color.BLACK);
            }
        });

        submitBut.setOnClickListener(new View.OnClickListener() {
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
                else if (emailTxt.getCurrentTextColor()!=Color.BLACK){
                    Toast.makeText(getApplicationContext(), "Please enter valid email!", Toast.LENGTH_SHORT).show();
                }
                //3.check passwd format
                else if (multiText.getCurrentTextColor()!=Color.BLACK){
                    Toast.makeText(getApplicationContext(),"Please enter valid password", Toast.LENGTH_SHORT).show();
                }
                //4.check passwd consistence
                else if(!passwd.getText().toString().equals(rePasswd.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Your password doesn't match!",Toast.LENGTH_SHORT).show();
                }
                //5.check emails duplication in database
                else
                    checkEmailDuplication();
            }
        });
    }

    private void checkEmailDuplication(){
        String url="http://api.a17-sd606.studev.groept.be/checkEmailDuplication/"+emailTxt.getText().toString();
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jArr=new JSONArray(response);
                            if(jArr.length()!=0){//email already existed
                                Toast.makeText(getApplicationContext(), "Email already existed!", Toast.LENGTH_SHORT).show();
                            }else if(jArr.length()==0){////email not existed
                                Requests("http://api.a17-sd606.studev.groept.be/UsersRegister/");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegisterActivity.this, "Oops,please try again later!", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }

    public void Requests(String url) {
        URL=url+firstNameTxt.getText().toString()+"/"+surnameTxt.getText().toString()+"/"+emailTxt.getText().toString()+"/"+passwd.getText().toString();
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(RegisterActivity.this, "Registration succeed!", Toast.LENGTH_SHORT).show();
                        switchToLogin();//new method to switch to login dialog
                    }
                }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(RegisterActivity.this, "Oops,please try again later!", Toast.LENGTH_SHORT).show();
                }
            });
        queue.add(stringRequest);// Add the request to the RequestQueue.
    }

    public void switchToLogin() {
        AlertDialog.Builder mBuilder=new AlertDialog.Builder(this);//create alert dialog
        View mView=getLayoutInflater().inflate(R.layout.dialog_login,null);//referencing the alert dialog to the login dialog
        //define the view inside the login layout
        final EditText mEmail=(EditText) mView.findViewById(R.id.etEmail);
        final EditText mpasswd=(EditText) mView.findViewById(R.id.etPasswd);
        Button mLogin=(Button) mView.findViewById(R.id.butLogin);

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mEmail.getText().toString().isEmpty()&&!mpasswd.getText().toString().isEmpty())
                {
                    LoginCheck(mEmail.getText().toString(),mpasswd.getText().toString());
                }else{
                    Toast.makeText(RegisterActivity.this, "Please fill in any empty fields!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mBuilder.setView(mView);
        AlertDialog dialog=mBuilder.create();
        dialog.show();
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
