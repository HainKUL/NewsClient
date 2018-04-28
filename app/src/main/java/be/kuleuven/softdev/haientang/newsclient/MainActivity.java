package be.kuleuven.softdev.haientang.newsclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    //declare globle variables
    Button loginBut,registerBut,guestBut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //1. get references to the buttons
        loginBut=(Button) findViewById(R.id.butLogin);
        registerBut=(Button) findViewById(R.id.butRegister);
        guestBut=(Button) findViewById(R.id.butGuest);

        //2. set click listeners to the buttons
        loginBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
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
                Intent intent=new Intent(MainActivity.this,GuestActivity.class);
                startActivity(intent);
            }
        });

    }
}
