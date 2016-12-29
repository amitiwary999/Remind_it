package com.example.amit.remind_it;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amit.remind_it.app.Prefs;
import com.example.amit.remind_it.model.Items;
import com.example.amit.remind_it.realm.RealmController;
import com.example.amit.remind_it.realm.RealmRecyclerViewAdapter;

import java.io.File;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmBaseAdapter;
import io.realm.RealmResults;

/**
 * Created by amit on 29/12/16.
 */

public class ItemsAdapter extends RealmRecyclerViewAdapter<Items> {
    Context context;
    private Realm realm;
    private LayoutInflater inflater;
  //  private List<Item> data;
    public ItemsAdapter(Context context) {

        this.context = context;
    }
    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate a new card view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_cards, parent, false);
        context=parent.getContext();
        return new CardViewHolder(view);
    }
    @Override
    public Items getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public RealmBaseAdapter<Items> getRealmAdapter() {
        return super.getRealmAdapter();
    }

    @Override
    public void setRealmAdapter(RealmBaseAdapter<Items> realmAdapter) {
        super.setRealmAdapter(realmAdapter);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        realm = RealmController.getInstance().getRealm();
        final Items book = getItem(position);
        final CardViewHolder holdr = (CardViewHolder) holder;
        holdr.textName.setText(book.getName());
        holdr.textLocation.setText(book.getLocation());
        File imgFile = new File(book.getImgPath());
        final String imagePath = book.getImgPath();
        final ImageView imageView =  holdr.imageBackground;
        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            imageView.setImageBitmap(myBitmap);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        }
        holdr.card.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure?")
                        .setCancelable(false)
                        .setTitle("Delete")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                RealmResults<Items> results = realm.where(Items.class).findAll();

                                // Get the book title to show it in toast message
                                Items b = results.get(position);
                                String title = b.getName();

                                // All changes to data must happen in a transaction
                                realm.beginTransaction();

                                // remove single match
                                results.remove(position);
                                realm.commitTransaction();

                                if (results.size() == 0) {
                                    Prefs.with(context).setPreLoad(false);
                                }

                                notifyDataSetChanged();

                                Toast.makeText(context, title + " is removed from Realm", Toast.LENGTH_SHORT).show();

                            }

                        }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return false;
            }
        });

    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {

        if (getRealmAdapter() != null) {
            return getRealmAdapter().getCount();
        }
        return 0;
    }
    public static class CardViewHolder extends RecyclerView.ViewHolder {

        public CardView card;
        public TextView textName;
        public TextView textAuthor;
        public TextView textLocation;
        public ImageView imageBackground;

        public CardViewHolder(View itemView) {
            // standard view holder pattern with Butterknife view injection
            super(itemView);

            card = (CardView) itemView.findViewById(R.id.card_view);
            textName = (TextView) itemView.findViewById(R.id.item_name);
            textAuthor = (TextView) itemView.findViewById(R.id.textView3);
            textLocation = (TextView) itemView.findViewById(R.id.last_location);
            imageBackground = (ImageView) itemView.findViewById(R.id.item_image);
        }
    }
}
