package com.example.irenachernyak.listviewexample;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements DataServiceReceiver.Listener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         //create and initialize ListAdapter from physicians list
        String output = LoadFile("physicians.json", false);
        try {
            JSONObject jObj = new JSONObject(output);
            JSONObject allphys = jObj.getJSONObject("physicians");
            JSONArray physicians = allphys.getJSONArray("physician");

            if (physicians.length() > 0) {
                ArrayList<String> physNames = new ArrayList<String>();

                for (int i=0; i < physicians.length(); i++) {

                    JSONObject obj = physicians.getJSONObject(i);
                    physNames.add(obj.getString("name"));
                }

                ListAdapter listAdapter = new MyListAdapter(this,physNames.toArray(new String[physNames.size()]));

                //find ListView which will be populated by listAdapter
                ListView listView = (ListView) findViewById(R.id.phys_list_view);
                listView.setAdapter(listAdapter);

                // setup handling of taps on the list rows
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String doctorPicked = "You selected:  " + String.valueOf(parent.getItemAtPosition(position));
                        Toast.makeText(MainActivity.this, doctorPicked, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }catch(JSONException ex){
            ex.printStackTrace();
        }


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

    public String LoadFile(String fileName, boolean loadFromRawFolder)
    {
        String content = "";
        try{
            content = LoadFileFromAssets(getResources(), fileName, loadFromRawFolder);
        } catch(IOException ex) {
            String error = ex.getMessage();
            ex.printStackTrace();
        }

        return content;
    }

    private String LoadFileFromAssets(Resources resources, String fileName, boolean loadFromRawFolder) throws IOException
    {
        //Create a InputStream to read the file into
        InputStream iS;

        //get the file as a stream
        iS = resources.getAssets().open(fileName);


        //create a buffer that has the same size as the InputStream
        byte[] buffer = new byte[iS.available()];
        //read the text file as a stream, into the buffer
        iS.read(buffer);
        //create a output stream to write the buffer into
        ByteArrayOutputStream oS = new ByteArrayOutputStream();
        //write this buffer to the output stream
        oS.write(buffer);
        //Close the Input and Output streams
        oS.close();
        iS.close();

        //return the output stream as a String
        return oS.toString();
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        Log.i("*** MainActivity ***", "got result from service");
        ArrayList<String> physnames = resultData.getStringArrayList("physicians");
        Log.i("DOCTORS from Result: ", physnames.toString());
    }

    public void OnUpdateClicked(View view) {

        Intent intent = new Intent(this, DataService.class);

        DataServiceReceiver receiver = new DataServiceReceiver(new Handler());
        receiver.setListener(this);
        intent.putExtra("rec", receiver);
        startService(intent);
    }
}
