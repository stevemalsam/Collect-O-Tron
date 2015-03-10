package com.stevemalsam.collect_o_tron;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.BaseAdapter;

import static android.support.v4.app.NavUtils.navigateUpFromSameTask;


/**
 * An activity representing a list of Games. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link GameDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p/>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link ListGamesFragment} and the item details`
 * (if present) is a {@link GameDetailFragment}.
 * <p/>
 * This activity also implements the required
 * {@link ListGamesFragment.Callbacks} interface
 * to listen for item selections.
 */
public class ListGamesActivity extends ActionBarActivity
        implements ListGamesFragment.Callbacks, GameDetailFragment.Callbacks {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_list);
        // Show the Up button in the action bar.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (findViewById(R.id.game_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            ((ListGamesFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.game_list))
                    .setActivateOnItemClick(true);
        }

        // TODO: If exposing deep links into your app, handle intents here.
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpFromSameTask(this);
            return true;
        } else if (id == R.id.action_add) {
            if (mTwoPane) {
                // In two-pane mode, show the detail view in this activity by
                // adding or replacing the detail fragment using a
                // fragment transaction.
                GameDetailFragment fragment = new GameDetailFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.game_detail_container, fragment)
                        .commit();
            } else {
                // In single-pane mode, simply start the detail activity
                // for the selected item ID.
                Intent detailIntent = new Intent(this, GameDetailActivity.class);
                startActivity(detailIntent);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Callback method from {@link ListGamesFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(String id) {
//        if (mTwoPane) {
//            // In two-pane mode, show the detail view in this activity by
//            // adding or replacing the detail fragment using a
//            // fragment transaction.
//            Bundle arguments = new Bundle();
//            arguments.putString(GameDetailFragment.ARG_ITEM_ID, id);
//            GameDetailFragment fragment = new GameDetailFragment();
//            fragment.setArguments(arguments);
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.game_detail_container, fragment)
//                    .commit();
//
//        } else {
//            // In single-pane mode, simply start the detail activity
//            // for the selected item ID.
//            Intent detailIntent = new Intent(this, GameDetailActivity.class);
//            detailIntent.putExtra(GameDetailFragment.ARG_ITEM_ID, id);
//            startActivity(detailIntent);
//        }
    }

    @Override
    public boolean shouldDisplayRating() {
        return false;
    }

    @Override
    public void onSave() {
        ListGamesFragment fragment = (ListGamesFragment)getSupportFragmentManager().findFragmentById(R.id.game_list);
        BaseAdapter adapter = (BaseAdapter) fragment.getListAdapter();
        adapter.notifyDataSetChanged();
    }
}
