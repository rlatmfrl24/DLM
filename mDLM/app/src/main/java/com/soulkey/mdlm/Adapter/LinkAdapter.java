package com.soulkey.mdlm.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.soulkey.mdlm.Activity.WebActivity;
import com.soulkey.mdlm.R;

public class LinkAdapter extends RecyclerView.Adapter<LinkAdapter.LinkViewHolder> {
    private JsonArray item_array;

    public LinkAdapter(JsonArray dataset){
        item_array = dataset;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LinkViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v =  LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_link, viewGroup, false);
        LinkViewHolder vh = new LinkViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull LinkViewHolder linkViewHolder, int i) {
        JsonObject item_data = item_array.get(i).getAsJsonObject();
        linkViewHolder.mTextView_title.setText(item_data.get("title").getAsString());
        linkViewHolder.mTextView_link.setText(item_data.get("link").getAsString());
    }

    @Override
    public int getItemCount() {
        return item_array.size();
    }

    public class LinkViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView_title;
        public TextView mTextView_link;
        public LinkViewHolder(@NonNull final View itemView) {
            super(itemView);
            mTextView_title = (TextView) itemView.findViewById(R.id.textview_title);
            mTextView_link = (TextView) itemView.findViewById((R.id.textview_link));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String selected_link = String.valueOf(mTextView_link.getText());
                    Log.d("muta", selected_link);
                    for(JsonElement je : item_array){
                        JsonObject item = je.getAsJsonObject();
                        if(item.get("link").getAsString().equals(selected_link)){
                            item_array.remove(je);
                            break;
                        }
                    }
                    notifyItemRemoved(getAdapterPosition());
                    Intent intent = new Intent(itemView.getContext(), WebActivity.class);
                    intent.putExtra("url", selected_link);
                    Activity origin = (Activity)itemView.getContext();
                    origin.startActivity(intent);
                    //itemView.getContext().startActivity(intent);
                }
            });
        }
    }
}
