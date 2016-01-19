package com.example.irenachernyak.listviewexample;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Base64;
import android.util.Log;
import android.util.Xml;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by irenachernyak on 1/14/16.
 */
public class DataService extends IntentService {


    public DataService()
    {
        super("");
    }
    public DataService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        final ResultReceiver rec = (ResultReceiver) intent.getParcelableExtra("rec");

        Calendar c = new GregorianCalendar();



        String surl = "http://sandbox.restorationrobotics.com/artashair/artasconsultapp.svc/rest/AllPhysicians";
        try {
            URL url = new URL(surl);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");

            con.connect();
            InputStream is = con.getInputStream();

            // Start parsing JSON

            BufferedReader r = new BufferedReader(new InputStreamReader(is));
            StringBuilder total = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line);
            }

            // this string is expected by parser as START_DOCUMENT so need to add this to the string returned from the server
            String startdoc = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
            String xmlString = startdoc + total.toString();

            String encodedJsonString = "";
            Log.i("xmlString", xmlString);

            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(new StringReader(xmlString));

            while (parser.next() != XmlPullParser.END_DOCUMENT) {
                int event = parser.getEventType();
                if (event == XmlPullParser.START_TAG){
                    String tagname = parser.getName();
                    if(tagname.equals("string")) {
                        if (parser.next() == XmlPullParser.TEXT) {
                            encodedJsonString = parser.getText();
                        }
                        break;
                    }
                }
            }

            if(encodedJsonString.length() > 0) {

                Log.i("encodedJsonString", encodedJsonString);
                byte[] valueDecoded = Base64.decode(encodedJsonString, Base64.DEFAULT);
                String jsonString = new String(valueDecoded);
                Log.i("decodedJsonString", jsonString);


                try {
//                    // that is for getting data changes (not actually encoded string)
//                    JSONObject jObj = new JSONObject(encodedJsonString);
//                    JSONArray allchanges = jObj.getJSONArray("changes");
//                    for (int i=0; i < allchanges.length(); i++) {
//                        JSONObject obj = allchanges.getJSONObject(i);
//                         Log.i(obj.getString("change"), obj.getString("value"));
//                    }

                        JSONObject jObj = new JSONObject(jsonString);
                        JSONObject allphys = jObj.getJSONObject("physicians");
                        JSONArray physicians = allphys.getJSONArray("physician");
                        ArrayList<String> physNames = new ArrayList<String>();
                        if (physicians.length() > 0) {


                            for (int i = 0; i < physicians.length(); i++) {

                                JSONObject obj = physicians.getJSONObject(i);
                                physNames.add(obj.getString("name"));
                            }
                        }
                        Log.i("DOCTORS: ", physNames.toString());

                    // send results back to activity
                    Bundle b = new Bundle();
                    b.putStringArrayList("physicians", physNames);
                    rec.send(0, b);

                    } catch (JSONException ex) {
                    ex.printStackTrace();
                }
            }

        } catch(MalformedURLException e){
            e.printStackTrace();
        } catch(IOException ioe){
            ioe.printStackTrace();
        }catch(Exception ex){
            ex.printStackTrace();
        }



    }

    private byte[] convertInputStreamToByteArray(InputStream inputStream)
    {
        byte[] bytes= null;

        try
        {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            byte data[] = new byte[1024];
            int count;

            while ((count = inputStream.read(data)) != -1)
            {
                bos.write(data, 0, count);
            }

            bos.flush();
            bos.close();
            inputStream.close();

            bytes = bos.toByteArray();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return bytes;
    }

}
