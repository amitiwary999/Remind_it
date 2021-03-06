package com.example.amit.remind_it;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.amit.remind_it.app.Prefs;
import com.example.amit.remind_it.dao.SampleDataBase;
import com.example.amit.remind_it.model.ItemModel;
//import com.example.amit.remind_it.model.Items;
//import com.example.amit.remind_it.realm.RealmController;
//import com.example.amit.remind_it.realm.RealmItemsAdapter;
import com.nanotasks.BackgroundWork;
import com.nanotasks.Completion;
import com.nanotasks.Tasks;

import java.util.ArrayList;
import java.util.List;

//import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    //ItemsAdapter adaptEr;
    com.example.amit.remind_it.adapter.ItemsAdapter itemsAdapter;
    RecyclerView recyclerView;
    SearchView searchView;
    SampleDataBase sampleDataBase;
    List<ItemModel> itemModelList;
    private String searchText=" ";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sampleDataBase = Room.databaseBuilder(MainActivity.this, SampleDataBase.class, "sample-db").build();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
      //  adaptEr = new ItemsAdapter(this);
        Tasks.executeInBackground(this, new BackgroundWork<Void>() {
            @Override
            public Void doInBackground() throws Exception {
                itemModelList = sampleDataBase.daoAccess().fetchAllData();
                return null;
            }
        }, new Completion<Void>() {
            @Override
            public void onSuccess(Context context, Void result) {
                itemsAdapter = new com.example.amit.remind_it.adapter.ItemsAdapter(MainActivity.this,itemModelList );
                recyclerView.setAdapter(itemsAdapter);
            }

            @Override
            public void onError(Context context, Exception e) {
                Log.d("Error", "can't read database");
            }
        });

        if (!Prefs.with(this).getPreLoad()) {
            //  setRealmData();
        }

       // RealmController.with(this).refresh();
        // get all persisted objects
        // create the helper adapter and notify data set changes
        // changes will be reflected automatically
       // setRealmAdapter(RealmController.with(this).getBooks());

     //   searchView = (MaterialSearchView) findViewById(R.id.search_view);
    /*    searchView.setVoiceSearch(false);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("query",query);
                adapter.clearAll();
                itemList.clear();

                addDataToList(itemList,query);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String query)
            {
                Log.d("query",query);
                adapter.clearAll();
                itemList.clear();

                addDataToList(itemList,query);
                return true;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {
                adapter.clearAll();
                addDataToList(itemList,null);
            }
        });*/



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,SaveNewItem.class));
            }
        });

      /*  DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);*/

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       // Toast.makeText(MainActivity.this,"check",Toast.LENGTH_LONG).show();
    }

//    public void setRealmAdapter(RealmResults<Items> books) {
//
//        RealmItemsAdapter realmAdapter = new RealmItemsAdapter(this.getApplicationContext(), books,true);
//        // Set the data and tell the RecyclerView to draw
//        adaptEr.setRealmAdapter(realmAdapter);
//        adaptEr.notifyDataSetChanged();
//    }
    @Override
    public void onResume(){
        super.onResume();
        //adaptEr.notifyDataSetChanged();

        // scroll the recycler view to bottom
//        recyclerView.scrollToPosition(RealmController.getInstance().getBooks().size() - 1);
        //   itemList.clear();
        //  addDataToList(itemList,null);
        //  adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //addDataToList(newText);
                return false;
            }
        });
      //  searchView.setQuery(searchText, false);
        return true;
        //MenuItem item = menu.findItem(R.id.search);
        //searchView.setMenuItem(item);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*Iif (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

     /*   if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void addDataToList( String query){
      //  String path = getFilesDir().getPath();
//        if(query == null) {
//            setRealmAdapter(RealmController.with(this).getBooks());
//        }
//        else {
//               setRealmAdapter(RealmController.with(this).queryedBooks(query));
//
//        }

    }


}
