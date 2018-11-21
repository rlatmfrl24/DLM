package com.soulkey.mdlm.Presenter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.soulkey.mdlm.WebActivity;
import com.soulkey.mdlm.R;

import java.util.List;

public class LinkAdapter extends RecyclerView.Adapter<LinkAdapter.LinkViewHolder> {
    private List<String> List_data;

    public LinkAdapter(List<String> dataset){
        List_data = dataset;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LinkViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v =  LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_item, viewGroup, false);
        LinkViewHolder vh = new LinkViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull LinkViewHolder linkViewHolder, int i) {
        linkViewHolder.mTextView.setText(List_data.get(i));
    }

    @Override
    public int getItemCount() {
        return List_data.size();
    }

    public class LinkViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public LinkViewHolder(@NonNull final View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.ItemTextView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("muta", String.valueOf(mTextView.getText()));
                    List_data.remove(mTextView.getText().toString());
                    notifyItemRemoved(getAdapterPosition());
                    Intent intent = new Intent(itemView.getContext(), WebActivity.class);
                    intent.putExtra("url", String.valueOf(mTextView.getText()));
                    Activity origin = (Activity)itemView.getContext();
                    origin.startActivity(intent);
                    //itemView.getContext().startActivity(intent);
                }
            });
        }
    }
}
