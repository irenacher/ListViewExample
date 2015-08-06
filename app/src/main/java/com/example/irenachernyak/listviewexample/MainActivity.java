package com.example.irenachernyak.listviewexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize array of movie names
        String [] movies = {"Pushing Daisies","Better off Ted", "Twin Peaks", "Freaks and Geeks", "Orphan Black", "Walking Dead",
                            "Breaking Bad", "The 400", "Alphas", "Life on Mars"};

        //create and initialize ListAdapter
        // for build_in row layouts like "simple_list_view_item_1" need to use ctor new ArrayAdapter<String>(this,android.R.layout.simple_list_view_item_1, movies);
        // for custom row layout (defined in row_layout.xml file) need to use ctor new ArrayAdapter<String>(this,android.R.layout.simple_list_view_item_1, R.id.textview1,  movies);
        // for custom adapter with custom row layout (defined in row_layout_2.xml) use ctor below:
        ListAdapter listAdapter = new MyListAdapter(this, movies);

        //find ListView which will be populated by listAdapter
        ListView listView = (ListView)findViewById(R.id.movies_list_view);
        listView.setAdapter(listAdapter);

        // setup handling of taps on the list rows
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String tvShowPicked = "You selected " + String.valueOf(parent.getItemAtPosition(position));
                Toast.makeText(MainActivity.this, tvShowPicked, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
