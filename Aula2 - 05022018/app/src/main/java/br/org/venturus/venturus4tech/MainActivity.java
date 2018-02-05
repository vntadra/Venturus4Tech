package br.org.venturus.venturus4tech;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
                if (TextUtils.isEmpty(nickText)) {
                    Log.d("VNT", "Nickname not provided");
                } else {
                    SocketManager.getInstance().getSocket()
                            .once(Socket.EVENT_CONNECT, new Emitter.Listener() {
                        @Override
                        public void call(Object... args) {
                            openChatActivity(nickText);
                        }
                    });
                    SocketManager.getInstance().getSocket()
                            .once(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
                        @Override
                        public void call(final Object... args) {
                            final String message;
                            if (args.length > 0 && args[0] instanceof Exception) {
                                message = ((Exception) args[0]).getCause().getLocalizedMessage();
                            } else {
                                message = "Unknown Connection Error";
                            }

                            Log.e("VNT", "Error: " + message);

                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG)
                                            .show();;
                                }
                            });
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