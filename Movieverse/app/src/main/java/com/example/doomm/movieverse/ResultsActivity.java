package com.example.doomm.movieverse;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResultsActivity extends AppCompatActivity {
    Intent intent;
    Bundle bundle;
    TableLayout table;
    int page_start = 0;
    int page_limit = 5;
    int totalLength = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        intent = getIntent();
        bundle = intent.getExtras();
        table = (TableLayout) findViewById(R.id.result_table);
        setTable();
        Button prev = (Button) findViewById(R.id.prev);
        Button next = (Button) findViewById(R.id.next);

        next.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                int totalLimit = page_start + page_limit;
                if (totalLength > totalLimit) {
                    page_start += page_limit;
                    clearTable();
                    setTable();
                }
                else {
                    Toast.makeText(getApplicationContext(), "no more movies to show", Toast.LENGTH_LONG);
                }
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int floor = page_start - page_limit;
                if(floor >= 0)  {
                    page_start -= page_limit;
                    clearTable();
                    setTable();
                }
                else {
                    Toast.makeText(getApplicationContext(), "no more movies to show", Toast.LENGTH_LONG);

                }
            }
        });

    }

    private void setTable() {
        if(bundle != null) {
            System.out.println("BUNDLE IS NOT NULL");
            Bundle dataBundle = bundle.getBundle("SEARCH_DATA");
            int i = page_start;
            int current = 0;
            totalLength = dataBundle.keySet().size();
            for (String key : dataBundle.keySet()) {
                if (current < i) {
                    System.out.println("current is not at start: " + current);
                    current++;
                    continue;
                }
                if(i == page_start + page_limit) {
                    System.out.println("reached limit");
                    break;
                }
                System.out.println("adding key: " + key);
                i++;
                System.out.println("RESULTS:");
                System.out.println(key + " : " + dataBundle.getString(key));

                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, String> movieMap = new HashMap<String, String>();
                System.out.println(dataBundle.getString(key));

                // remove weird characters
                String data = dataBundle.getString(key);
                Pattern pt = Pattern.compile("[^a-zA-Z0-9\"{:,. }]");
                Matcher match = pt.matcher(data);
                while(match.find()) {
                    String s = match.group();
                    data = data.replaceAll(s, "");
                }
                System.out.println(data);
                try {
                    movieMap = objectMapper.readValue(data, Map.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                TableRow row = new TableRow(this);
                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                TableRow.LayoutParams titleLP = new TableRow.LayoutParams(60, TableRow.LayoutParams.WRAP_CONTENT);
                titleLP.weight = 1;
                titleLP.gravity = Gravity.RIGHT;
                row.setLayoutParams(lp);

                TextView title = new TextView(this);
                title.setLayoutParams(titleLP);
                title.setText(movieMap.get("title"));
                System.out.println(movieMap.get("title"));

                TextView year = new TextView(this);
                year.setLayoutParams(titleLP);
                year.setText(movieMap.get("year"));
                System.out.println(movieMap.get("year"));


                TextView director = new TextView(this);
                director.setLayoutParams(titleLP);
                director.setText(movieMap.get("director"));
                System.out.println(movieMap.get("director"));


                TextView genre = new TextView(this);
                genre.setLayoutParams(titleLP);
//                System.out.println("GENRES: " + movieMap.get("genres"));
//                String genres = movieMap.get("genres");
//                Map<String, String> genreMap = new HashMap<String, String>();
//
//                try {
//                      genreMap = objectMapper.readValue(movieMap.get("genres"), Map.class);
////                    genreMap = objectMapper.readValue(genres, Map.class);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                genre.setText(movieMap.get("genre"));
                System.out.println(movieMap.get("genre"));


                TextView star = new TextView(this);
                star.setLayoutParams(titleLP);
//                String stars = movieMap.get("stars");
//                Map<String, String> starMap = new HashMap<String, String>();
//                try {
//                      starMap = objectMapper.readValue(movieMap.get("stars"), Map.class);
////                    starMap = objectMapper.readValue(stars, Map.class);
//                } catch(IOException e) {
//                    e.printStackTrace();
//                }
                star.setText(movieMap.get("star"));
                System.out.println(movieMap.get("star"));


                row.addView(title);
                row.addView(year);
                row.addView(director);
                row.addView(genre);
                row.addView(star);

                table.addView(row);
            }
        }
    }
    private void clearTable() {
        int childCount = table.getChildCount();
        if (childCount > 1) {
            table.removeViews(1, childCount - 1);
        }
    }
}
