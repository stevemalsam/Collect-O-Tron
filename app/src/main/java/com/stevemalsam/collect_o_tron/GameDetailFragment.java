package com.stevemalsam.collect_o_tron;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;

import com.stevemalsam.collect_o_tron.adapters.GameAutocompleteAdapter;
import com.stevemalsam.collect_o_tron.models.Game;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;

/**
 * A fragment representing a single Game detail screen.
 * This fragment is either contained in a {@link ListGamesActivity}
 * in two-pane mode (on tablets) or a {@link GameDetailActivity}
 * on handsets.
 */
public class GameDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    @InjectView(R.id.game_name) AutoCompleteTextView gameName;
    @InjectView(R.id.game_platform) EditText gamePlatform;
    @InjectView(R.id.is_completed) CheckBox isCompleted;
    @InjectView(R.id.game_rating) RatingBar gameRating;

    private Callbacks mCallbacks;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public GameDetailFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

//        if (getArguments().containsKey(ARG_ITEM_ID)) {
//            // Load the dummy content specified by the fragment
//            // arguments. In a real-world scenario, use a Loader
//            // to load content from a content provider.
//            mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
//        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_save, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_save) {
            Game game = new Game();
            game.name = gameName.getText().toString();
            game.platform = gamePlatform.getText().toString();
            game.isCompleted = isCompleted.isChecked();
            game.rating = gameRating.getRating();

            Game.Games.add(game);

            mCallbacks.onSave();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_game_detail, container, false);
        ButterKnife.inject(this, rootView);
        gameName.setAdapter(new GameAutocompleteAdapter(getActivity()));
        gameName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GameAutocompleteAdapter.GameData item = (GameAutocompleteAdapter.GameData) parent.getItemAtPosition(position);
                gameName.setText(item.GameTitle);
                gamePlatform.setText(item.Platform);
            }
        });

        return rootView;
    }

//    @OnItemClick(R.id.game_name)
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        GameAutocompleteAdapter.GameData item = (GameAutocompleteAdapter.GameData) parent.getItemAtPosition(position);
//        gameName.setText(item.GameTitle);
//        gamePlatform.setText(item.Platform);
//    }
    public interface Callbacks {
        public void onSave();
    }
}
