package com.example.amit.remind_it.realm;

import android.app.Activity;
import android.app.Application;
import android.support.v4.app.Fragment;

import com.example.amit.remind_it.model.Items;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by amit on 29/12/16.
 */

public class RealmController {

    private static RealmController instance;
    private final Realm realm;

    public RealmController(Application application) {
        realm = Realm.getInstance(application);
    }

    public static RealmController with(Fragment fragment) {

        if (instance == null) {
            instance = new RealmController(fragment.getActivity().getApplication());
        }
        return instance;
    }

    public static RealmController with(Activity activity) {

        if (instance == null) {
            instance = new RealmController(activity.getApplication());
        }
        return instance;
    }

    public static RealmController with(Application application) {

        if (instance == null) {
            instance = new RealmController(application);
        }
        return instance;
    }

    public static RealmController getInstance() {

        return instance;
    }

    public Realm getRealm() {

        return realm;
    }

    //Refresh the realm istance
    public void refresh() {

        realm.refresh();
    }

    //clear all objects from Book.class
    public void clearAll() {

        realm.beginTransaction();
        realm.clear(Items.class);
        realm.commitTransaction();
    }

    //find all objects in the Book.class
    public RealmResults<Items> getBooks() {

        return realm.where(Items.class).findAll();
    }

    //query a single item with the given id
    public Items getBook(String id) {

        return realm.where(Items.class).equalTo("id", id).findFirst();
    }

    //check if Book.class is empty
    public boolean hasBooks() {

        return !realm.allObjects(Items.class).isEmpty();
    }

    //query example
    public RealmResults<Items> queryedBooks(String flag) {

        return realm.where(Items.class)
                .contains("name", flag)
                .or()
                .contains("location", flag)
                .findAll();

    }
}
