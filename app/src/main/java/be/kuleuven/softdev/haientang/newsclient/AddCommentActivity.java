package be.kuleuven.softdev.haientang.newsclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AddCommentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comment);
        ButtonBack();
    }

    public void ButtonBack() {
        final Button ReviewBut = (Button) findViewById(R.id.ButtonUpdate);
        final TextView tvcomment=(TextView) findViewById(R.id.EditReview) ;
        ReviewBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//switch to new activity
                Intent intent = new Intent(AddCommentActivity.this, NewsShowActivity.class);
                String comment=null;
                comment=tvcomment.getText().toString();
                intent.putExtra("comment",comment);
                startActivity(intent);
            }
        });
    }
}
