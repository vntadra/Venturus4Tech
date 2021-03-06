package br.org.venturus.venturus4tech;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by vntguca on 12/07/17.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private List<JSONObject> mData;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View itemView;
        public TextView mUserInitial;
        public TextView mUsername;
        public TextView mMessage;
        public TextView mTime;


        public ViewHolder(View v) {
            super(v);
            itemView = v;
            mUserInitial = (TextView) v.findViewById(R.id.userInitial);
            mUsername = (TextView) v.findViewById(R.id.user);
            mMessage = (TextView) v.findViewById(R.id.message);
            mTime = (TextView) v.findViewById(R.id.time);
        }
    }

    public ChatAdapter() {
        mData = new ArrayList<>();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_list_item, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            String username = mData.get(position).getString("author");
            String msg = mData.get(position).getString("message");

            // Parse time
            String receivedTime = mData.get(position).getString("time");
            SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                    Locale.getDefault());
            Date date = parser.parse(receivedTime);
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss",
                    Locale.getDefault());
            String formattedTime = formatter.format(date);

            holder.mMessage.setText(msg);
            holder.mUsername.setText(username);
            holder.mTime.setText(formattedTime);

            if (username != null && username.length() >= 2) {
                holder.mUserInitial.setText(username.substring(0, 1));
            }
        } catch (JSONException | ParseException e) {
            holder.mMessage.setText(e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void addMsg(JSONObject msg) {
        mData.add(msg);
        notifyDataSetChanged();
    }

    public void addAllMsgs(List<JSONObject> msgs) {
        mData.addAll(msgs);
        notifyDataSetChanged();
    }
}
