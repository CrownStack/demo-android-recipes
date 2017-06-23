package com.app.socialintegration.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.socialintegration.R;
import com.app.socialintegration.activities.GoogleMapActivity;
import com.app.socialintegration.activities.SocialActivity;

import java.util.ArrayList;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    final private Context mContext;
    private ArrayList<String> mRecipe = new ArrayList<>();
    public RecipeAdapter(Context context, ArrayList<String> recipe) {
        this.mContext = context;
        this.mRecipe = recipe;
    }

    @Override
    public RecipeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindData(mRecipe.get(position), position);
    }

    public int getItemCount() {return mRecipe.size();}

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        final private LinearLayout linearLayoutItem;
        final private TextView textViewItem;
        int position ;

        public ViewHolder(View view) {
            super(view);
            linearLayoutItem = (LinearLayout) view.findViewById(R.id.ll_item);
            textViewItem = (TextView) view.findViewById(R.id.text_view_item);
            linearLayoutItem.setOnClickListener(this);
        }

        private void bindData(String item, int position) {
            this.position = position;
            textViewItem.setText(item);
        }

        @Override
        public void onClick(View v) {
            String item = mRecipe.get(position);
            if(item.contains("SocialActivity")) mContext.startActivity(new Intent(mContext, SocialActivity.class));
            else if(item.contains("GoogleMapActivity")) mContext.startActivity(new Intent(mContext, GoogleMapActivity.class));
        }
    }
}
