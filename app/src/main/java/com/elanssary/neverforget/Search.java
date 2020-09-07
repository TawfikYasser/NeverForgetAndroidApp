package com.elanssary.neverforget;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class Search extends AppCompatActivity {
    private DatabaseReference mDatabaseReference;
    private RecyclerView mSearchRec;
    private EditText mEditSearch;
    private SwipeRefreshLayout mSwipeRef;
    private String searchQuery;
    private AdapterMem adapterMem ;
    private Button mS;
    private Toolbar mToolbar;
    private ApplicationData mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mData = new ApplicationData(this);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(mData.getName());
        //mEditSearch = findViewById(R.id.search_et);
        mToolbar = findViewById(R.id.toolbar_search);
        setSupportActionBar(mToolbar);
        mSearchRec = findViewById(R.id.search_recycler);
        if (getSupportActionBar() != null) {
            if(mData.getLang()){
                getSupportActionBar().setTitle("بحث");
            }else{
                getSupportActionBar().setTitle("Search");
            }
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    //This function to stop get data.
    @Override
    public void onStop() {
        super.onStop();
        if(adapterMem != null){
            adapterMem.stopListening();

        }
    }
    //ENd of onStop

    // This function to start get data.
    @Override
    public void onStart() {
        super.onStart();
        if(adapterMem !=null){
            adapterMem.startListening();

        }


    }

    private void firebaseSearch(String searchText){
        String q = searchText.toLowerCase();
        Query FirebaseQ = mDatabaseReference.orderByChild("mTitle").startAt(searchText).endAt(searchText +"\uf8ff");
        final FirebaseRecyclerOptions<Memory> options =
                new FirebaseRecyclerOptions.Builder<Memory>()
                        .setQuery(FirebaseQ,Memory.class)
                        .build();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        mSearchRec.setLayoutManager(linearLayoutManager);
        adapterMem = new AdapterMem(options,this);
        adapterMem.startListening();
        mSearchRec.setAdapter(adapterMem);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem sItem = menu.findItem(R.id.search_menu);
        SearchView searchView = (SearchView) sItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                firebaseSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                firebaseSearch(newText);
                return false;
            }
        });
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_menu:
                return true;

            default:
                return false;
        }
    }

}
