package br.org.venturus.venturus4tech;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        final String name = getIntent().getStringExtra("nickname");

        TextView textView = (TextView) findViewById(R.id.nickname);
        textView.setText(name);
    }
}
