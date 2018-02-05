package br.org.venturus.venturus4tech;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private Button mButton;
    private EditText mNickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButton = (Button) findViewById(R.id.enter_button);

        mNickname = (EditText) findViewById(R.id.nickname);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String nickText = mNickname.getText().toString();
                if (TextUtils.isEmpty(nickText)) {
                    Log.d("VNT", "Nickname not provided");
                }
                openChatActivity(nickText);
            }
        });

    }

    private void openChatActivity(String nickname) {
        Intent i = new Intent(this, ChatActivity.class);
        i.putExtra("nickname", nickname);
        startActivity(i);
    }
}