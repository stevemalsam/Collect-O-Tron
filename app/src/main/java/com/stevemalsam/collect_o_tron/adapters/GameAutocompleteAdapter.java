package com.stevemalsam.collect_o_tron.adapters;

import android.content.Context;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by stevo on 3/10/15.
 */
public class GameAutocompleteAdapter extends BaseAdapter implements Filterable {

    private final Context mContext;
    private ArrayList<GameData> gameList;

    public GameAutocompleteAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return gameList.size();
    }

    @Override
    public GameData getItem(int position) {
        return gameList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater inflater;
            inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(android.R.layout.simple_list_item_2, parent, false);
        }

        ((TextView)convertView.findViewById(android.R.id.text1)).setText(getItem(position).GameTitle);
        ((TextView)convertView.findViewById(android.R.id.text2)).setText(getItem(position).Platform);

        return convertView;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    // Retrieve the autocomplete results.
                    gameList = autocomplete(constraint.toString());

                    // Assign the data to the FilterResults
                    filterResults.values = gameList;
                    filterResults.count = gameList.size();
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

    private ArrayList<GameData> autocomplete(String input) {
        HttpURLConnection conn = null;
        ArrayList<GameData> resultList = new ArrayList<>();
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

        return resultList;
    }

    private ArrayList<GameData> parseXML(XmlPullParser parser) throws XmlPullParserException,IOException {
        int eventType = parser.getEventType();
        GameData currentGame = null;

        while (eventType != XmlPullParser.END_DOCUMENT) {
            String name = null;
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    gameList = new ArrayList<>();
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
                        gameList.add(currentGame);
                    }
            }
            eventType = parser.next();
        }

        return gameList;
    }

    public class GameData {
        public int id;
        public String GameTitle;
        public String Platform;
    }
}
