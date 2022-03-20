package com.example.parsexml;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
ListView lv;
ArrayList<HashMap<String,String>> horoscopeList= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv=findViewById(R.id.list);
        new LoadShowXML().execute();
    }

    @SuppressLint("StaticFieldLeak")
    public class LoadShowXML extends AsyncTask<String,Void,String>
    {
      HttpHandler ho;   // handler object



        @Override
        protected String doInBackground(String... params) {
            String res="";
            try {
                String url = "http://10.0.2.2:8080/xmlData/horoscope.xml";
                ho = new HttpHandler();
                InputStream isStream = ho.CallServer(url);
                if(isStream!=null)
                    res = ho.StreamToString(isStream);
                Log.d("dataXml: ", res);
                parseXML(res);
            }
            catch (Exception e){
                Log.d("doInBackground", String.valueOf(e));
            }
            return res;
        }

        @Override
        protected void onPostExecute(String s){
            super.onPostExecute(s);
            ListAdapter adapter = new SimpleAdapter(MainActivity.this, horoscopeList,
                    R.layout.list_item,
                    new String[]{"Name", "Range", "Text"},
                    new int[]{R.id.name, R.id.range, R.id.text});
            lv.setAdapter(adapter);
            lv.setOnItemClickListener((adapterView, view, position, id) -> {
                String name = ((TextView)view.findViewById(R.id.name)).getText().toString();
                String range = ((TextView)view.findViewById(R.id.range)).getText().toString();
                String text = ((TextView)view.findViewById(R.id.text)).getText().toString();

                Bundle b = new Bundle();
                b.putString("Name", name);
                b.putString("Range", range);
                b.putString("Text", text);
                Intent in = new Intent(getApplicationContext(), DetailsActivity.class);
                in.putExtras(b);
                startActivity(in);

            });
        }

    }
    public void parseXML(String xmlString) {
        try {
            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
            xmlFactoryObject.setNamespaceAware(true);
            XmlPullParser myParser = xmlFactoryObject.newPullParser();
            myParser.setInput(new StringReader(xmlString));
            String name = "", range = "", text = "";
            int eventType = myParser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT){
                if(eventType == XmlPullParser.START_TAG){
                    String tagName = myParser.getName();
                    if(tagName.equals("Name")){
                        if(myParser.next() == XmlPullParser.TEXT) {
                            name = myParser.getText();
                            Log.d("Name: ", name);
                        }
                    }if(tagName.equals("Range")){
                        if(myParser.next() == XmlPullParser.TEXT) {
                            range = myParser.getText();
                            Log.d("Range: ", range);
                        }
                    }if(tagName.equals("Text")){
                        if(myParser.next() == XmlPullParser.TEXT) {
                            text = myParser.getText();
                            Log.d("Text: ", text);
                        }
                        if(!name.equals("") || !range.equals("") || !text.equals("")){
                            HashMap <String, String> map = new HashMap <>();
                            map.put("Name", name);
                            map.put("Range", range);
                            map.put("Text", text);
                            horoscopeList.add(map);
                        }
                    }
                }else if(eventType == XmlPullParser.END_TAG){
                    Log.d("XmlPullParser: ", "END_TAG");
                }
                eventType = myParser.next();
            }


        }catch (Exception e){
            Log.d("Log: ", "Error parsing XML");
        }
    }

}