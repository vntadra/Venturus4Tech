package br.org.venturus.venturus4tech;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.socket.emitter.Emitter;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ChatAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Button mSendButton;
    private String mNickname;
    private EditText mMsgInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mRecyclerView = (RecyclerView) findViewById(R.id.chat_recycler_view);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new ChatAdapter();

        mRecyclerView.setAdapter(mAdapter);

        mSendButton = (Button) findViewById(R.id.send_button);

        mMsgInput = (EditText) findViewById(R.id.msg_input);

        mNickname = getIntent().getStringExtra("nickname");

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                JSONObject message = new JSONObject();
                try {
                    message.put("message", mMsgInput.getText().toString());
                    message.put("author", mNickname);
                    SocketManager.getInstance().getSocket().emit("message", message);
                } catch (JSONException e) {
                }
            }

        });

        SocketManager.getInstance().getSocket().on("messages", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                final JSONArray messages = (JSONArray) args[0];
                final List<JSONObject> listMessages = new ArrayList<>();
                for (int i=0; i<messages.length(); i++) {
                    try {
                        JSONObject message = messages.getJSONObject(i);
                        listMessages.add(message);
                    } catch (JSONException e) {
                        // ignore
                    }
                }

                ChatActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.addAllMsgs(listMessages);
                        playSound();
                        mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount()-1);
                    }
                });

            }
        });

        SocketManager.getInstance().getSocket().on("message", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                final JSONObject message = (JSONObject) args[0];
                ChatActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.addMsg(message);
                        playSound();
                        mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount()-1);
                    }
                });

            }
        });
    }

    @Override
    public void onBackPressed() {
        logout();
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.chat_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_sound:
                toggleSoundPreference();
                return true;
            case R.id.action_close:
                logout();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logout() {
        SocketManager.getInstance().getSocket().disconnect();
        mAdapter.addAllMsgs(new ArrayList<JSONObject>());
        mAdapter.notifyDataSetChanged();
    }


    public boolean isSoundEnabled() {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getBoolean("sound", true);
    }


    public void toggleSoundPreference() {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("sound", !isSoundEnabled());
        editor.apply();
    }


    private void playSound() {
        if (!isSoundEnabled())
            return;

        MediaPlayer mp = MediaPlayer.create(this, R.raw.msg_sound);
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.release();
            }
        });
        mp.start();
    }
}
