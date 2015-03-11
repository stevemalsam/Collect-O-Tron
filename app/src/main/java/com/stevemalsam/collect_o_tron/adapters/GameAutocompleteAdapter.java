package com.stevemalsam.collect_o_tron.adapters;

import android.content.Context;
import android.util.Log;
import android.util.Xml;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by stevo on 3/10/15.
 */
public class GameAutocompleteAdapter extends ArrayAdapter<String> implements Filterable {

    private ArrayList<String> resultList;
    private ArrayList<GameData> gameList;

    public GameAutocompleteAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public String getItem(int position) {
        return resultList.get(position);
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    // Retrieve the autocomplete results.
                    resultList = autocomplete(constraint.toString());

                    // Assign the data to the FilterResults
                    filterResults.values = resultList;
                    filterResults.count = resultList.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                }
                else {
                    notifyDataSetInvalidated();
                }
            }};
        return filter;
    }

    private static final String LOG_TAG = "ExampleApp";

    private static final String GAMES_API_BASE = "http://thegamesdb.net/api/GetGamesList.php";
    private static final String TYPE_AUTOCOMPLETE = "?name=";

    private static final String API_KEY = "YOUR_API_KEY";

    private ArrayList<String> autocomplete(String input) {
        HttpURLConnection conn = null;
        StringBuilder xmlResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(GAMES_API_BASE + TYPE_AUTOCOMPLETE);
            sb.append(URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();

            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(conn.getInputStream(), conn.getContentEncoding());
            resultList = parseXML(parser);

        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

//        try {
//            // Create a JSON object hierarchy from the results
//            JSONObject jsonObj = new JSONObject(xmlResults.toString());
//            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");
//
//            // Extract the Place descriptions from the results
//            resultList = new ArrayList<String>(predsJsonArray.length());
//            for (int i = 0; i < predsJsonArray.length(); i++) {
//                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
//            }
//        } catch (JSONException e) {
//            Log.e(LOG_TAG, "Cannot process JSON results", e);
//        }

        return resultList;
    }

    private ArrayList<String> parseXML(XmlPullParser parser) throws XmlPullParserException,IOException {
        int eventType = parser.getEventType();
        GameData currentGame = null;

        while (eventType != XmlPullParser.END_DOCUMENT) {
            String name = null;
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    gameList = new ArrayList<>();
                    resultList = new ArrayList<>();
                    break;
                case XmlPullParser.START_TAG:
                    name = parser.getName();
                    if (name.equals("Game")) {
                        currentGame = new GameData();
                    } else if (currentGame != null) {
                        if (name.equals("id")) {
                            currentGame.id = Integer.parseInt(parser.nextText());
                        } else if (name.equals("GameTitle")) {
                            currentGame.GameTitle = parser.nextText();
                        } else if (name.equals("Platform")) {
                            currentGame.Platform = parser.nextText();
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:
                    name = parser.getName();
                    if (name.equalsIgnoreCase("Game") && currentGame != null) {
                        resultList.add(currentGame.GameTitle);
                        gameList.add(currentGame);
                    }
            }
            eventType = parser.next();
        }

        return resultList;
    }

    private class GameData {
        public int id;
        public String GameTitle;
        public String Platform;

    }
}
