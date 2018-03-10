package com.example.doomm.movieverse;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {
    private SearchActivity.SearchTask searchTask = null;
    private EditText titleET;
    HashMap<String, String> resultMap = new HashMap<String, String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        titleET = findViewById(R.id.movie_title);

        titleET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptSearch();
                    return true;
                }
                return false;
            }
        });
        Button searchButton =  findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSearch();
            }
        });

//        searchTask = new SearchActivity.SearchTask(titleET.getText().toString());
//        searchTask.execute((Void) null);
    }

    public void attemptSearch() {
        searchTask = new SearchActivity.SearchTask(titleET.getText().toString());
        searchTask.execute((Void) null);
    }

    public class SearchTask extends AsyncTask<Void, Void, Boolean> {
        private final String charset = "UTF-8";
        private final String url = "http://52.14.51.248:8080/project1/FullTextServlet";
//        private final String url = "http://localhost:8080/project1/FullTextServlet";

        private final String search;
        SearchTask(String search) {
            this.search = search;

        }

        @Override
        protected Boolean doInBackground(Void... params) {

            if(search.equals("")) {
                titleET.setError("Enter a search query");
                titleET.requestFocus();
            }
            else{
                String movie_title = "title="+search+"&ajax=false&android=true";
                try {
                    // POST search string to Tomcat
                    byte[] postData = movie_title.getBytes(charset);
                    URLConnection connection = new URL(url).openConnection();
                    connection.setDoOutput(true); // Triggers POST.
                    connection.setRequestProperty("Accept-Charset", charset);
                    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
                    connection.setRequestProperty("Content-Length", Integer.toString(postData.length));
                    try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
                        wr.write(postData);
                    }

                    // GET RESPONSE DATA
                    StringBuffer sb = new StringBuffer();
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                    System.out.println("OBTAIN SEARCH RESULT");
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        sb.append(inputLine);
                    }
                    String result = sb.toString();
                    System.out.println(result);
                    JSONObject obj = new JSONObject(result);
                    if (result.length() < 10) {
                        return false;
                    }
                    else {
                        Iterator keys = obj.keys();
                        while(keys.hasNext()) {
                            String key = (String) keys.next();
                            resultMap.put(key, obj.get(key).toString());
                        }
                        return true;
                    }

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();} catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {


            // successful login
            if (success) {
                Intent intent = new Intent(getApplicationContext(), ResultsActivity.class);
                Bundle bundle = new Bundle();
//                System.out.println(pair.getKey() + " = " + pair.getValue());

                Iterator it = resultMap.entrySet().iterator();
                for (String key : resultMap.keySet()) {
//                    Map.Entry pair = (Map.Entry)it.next();
//                    Bundle movieBundle = new Bundle();
                    ObjectMapper oMapper = new ObjectMapper();
//                    Map<String, String> movieMap = oMapper.convertValue(pair.getValue(), Map.class);
//                    Map<String, String> genres = new HashMap<String, String>();
//                    Map<String, String> stars = new HashMap<String, String>();
//
//                    try {
//                        genres = oMapper.readValue(movieMap.get("genres"), Map.class);
//                        stars = oMapper.readValue(movieMap.get("stars"), Map.class);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                    movieBundle.putString("title", movieMap.get("title"));
//                    movieBundle.putString("year", movieMap.get("year"));
//                    movieBundle.putString("director", movieMap.get("director"));
//                    movieBundle.putString("genre", genres.get("genre1"));
//                    movieBundle.putString("star", stars.get("star1"));
                    System.out.println("INSERTING MOVIE DATA: " + resultMap.get(key).getClass());
//                    try {
//                        HashMap<String, String> movieData = oMapper.readValue(resultMap.get(key), HashMap.class);
//                        String genreMap = movieData.get("genres");
//                        System.out.println(genreMap);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                    bundle.putString(key, resultMap.get(key));
//                    it.remove();
                }

                System.out.println(resultMap);
                System.out.println("MOVING TO RESULTS");
                intent.putExtra("SEARCH_DATA", bundle);
                startActivity(intent);

//                for (int i = 0; i < )
            } else {
                titleET.setError("No results found!");
                titleET.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {

        }


    }

    }
