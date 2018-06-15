package com.example.amit.remind_it.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.amit.remind_it.R;
import com.example.amit.remind_it.model.ItemModel;

import java.util.List;

/**
 * Created by meeera on 15/6/18.
 */

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemViewHolder>{

    Context context;
    List<ItemModel> itemModelList;
    public ItemsAdapter(Context context, List<ItemModel> itemModels){
       this.context = context;
       itemModelList = itemModels;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.items_cards, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.textName.setText(itemModelList.get(position).getName());
        holder.textLocation.setText(itemModelList.get(position).getLocation());
    }

    @Override
    public int getItemCount() {
        return itemModelList.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{
        public CardView card;
        public TextView textName;
        public TextView textAuthor;
        public TextView textLocation;
        public ImageView imageBackground;

        public ItemViewHolder(View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.card_view);
            textName = itemView.findViewById(R.id.item_name);
            textAuthor = itemView.findViewById(R.id.textView3);
            textLocation = itemView.findViewById(R.id.last_location);
            imageBackground = itemView.findViewById(R.id.item_image);
        }
    }
}
