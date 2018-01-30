package br.org.venturus.venturus4tech;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

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
                if (nickText.isEmpty()) {
                    Log.e("VNT", "Nickname Vazio!!!");
                } else {
                    SocketManager.getInstance().getSocket().once(Socket.EVENT_CONNECT, new Emitter.Listener() {
                        @Override
                        public void call(Object... args) {
                            openChatActivity(nickText);
                        }
                    });

                    SocketManager.getInstance().getSocket().connect();

                }
            }
        });

    }

    private void openChatActivity(String nickname) {
        Intent i = new Intent(this, ChatActivity.class);
        i.putExtra("nickname", nickname);
        startActivity(i);
    }
}