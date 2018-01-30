package br.org.venturus.venturus4tech;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
            String time = mData.get(position).getString("sent");
            holder.mMessage.setText(msg);
            holder.mUsername.setText(username);
            holder.mTime.setText(time);
            holder.mUserInitial.setText(username.substring(0,1));
        } catch (JSONException e) {
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
}
