package com.example.amit.remind_it.realm;

import android.content.Context;

import com.example.amit.remind_it.model.Items;

import io.realm.RealmResults;

/**
 * Created by amit on 29/12/16.
 */

public class RealmItemsAdapter extends RealmModelAdapter<Items> {

    public RealmItemsAdapter(Context context, RealmResults<Items> realmResults, boolean automaticUpdate) {

        super(context, realmResults, automaticUpdate);
    }
}
