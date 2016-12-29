package com.example.amit.remind_it;

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
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.amit.remind_it.app.Prefs;
import com.example.amit.remind_it.model.Items;
import com.example.amit.remind_it.realm.RealmController;
import com.example.amit.remind_it.realm.RealmItemsAdapter;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ItemsAdapter adaptEr;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adaptEr = new ItemsAdapter(this);
        recyclerView.setAdapter(adaptEr);
        if (!Prefs.with(this).getPreLoad()) {
            //  setRealmData();
        }

        RealmController.with(this).refresh();
        // get all persisted objects
        // create the helper adapter and notify data set changes
        // changes will be reflected automatically
        setRealmAdapter(RealmController.with(this).getBooks());

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
                startActivity(new Intent(getBaseContext(),SaveNewItem.class));
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

    public void setRealmAdapter(RealmResults<Items> books) {

        RealmItemsAdapter realmAdapter = new RealmItemsAdapter(this.getApplicationContext(), books,true);
        // Set the data and tell the RecyclerView to draw
        adaptEr.setRealmAdapter(realmAdapter);
        adaptEr.notifyDataSetChanged();
    }
    @Override
    public void onResume(){
        super.onResume();
        adaptEr.notifyDataSetChanged();

        // scroll the recycler view to bottom
        recyclerView.scrollToPosition(RealmController.getInstance().getBooks().size() - 1);
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

        //MenuItem item = menu.findItem(R.id.search);
        //searchView.setMenuItem(item);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

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

   /* public void addDataToList(List<Item> data, String query){
        String path = getFilesDir().getPath();
        String databaseName = "myDb";
        String password = "passw0rd";

        WaspDb db = WaspFactory.openOrCreateDatabase(path,databaseName,password);
        WaspHash itemsHash = db.openOrCreateHash("items");

        if(query == null) {
            List<Item> ld = itemsHash.getAllValues();
            if (ld == null) {
                Log.d("ld", "is null");
            }
            data.addAll(ld);
        }
        else {
            List<Item> ld = itemsHash.getAllValues();
            for(Item item:ld ){
                if(item.getItemName().toLowerCase().contains(query.toLowerCase())){
                    data.add(item);
                }
                else if(item.getLocation().toLowerCase().contains(query.toLowerCase())){
                    data.add(item);
                }
                else {
                    for(String s:item.getTags()){
                        if(s.toLowerCase().contains(query.toLowerCase())){
                            data.add(item);
                            break;
                        }
                    }
                }
            }
        }

    }*/

}
