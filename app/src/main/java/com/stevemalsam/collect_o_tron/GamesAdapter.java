package com.stevemalsam.collect_o_tron;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.RatingBar;
import android.widget.TextView;

import com.stevemalsam.collect_o_tron.models.Game;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.Optional;

/**
 * Created by stevo on 3/10/15.
 */
public class GamesAdapter extends ArrayAdapter<Game> {

    private final int resource;

    public GamesAdapter(Context context, int resource, List<Game> objects) {
        super(context, resource, objects);
        this.resource = resource;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
//        return super.getView(position, convertView, parent);

        View v = convertView;
        ViewHolder holder;
        if(v == null) {
            LayoutInflater inflater;
            inflater = LayoutInflater.from(getContext());
            v = inflater.inflate(resource, parent, false);
            holder = new ViewHolder(v);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        final Game game = getItem(position);
        holder.gameName.setText(game.name);
        holder.gamePlatform.setText(game.platform);
        if (holder.isCompleted != null) {
            holder.isCompleted.setChecked(game.isCompleted);
        } else if (holder.gameRating != null) {
            holder.gameRating.setRating(game.rating);
            holder.gameRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    game.rating = rating;
                }
            });
        }

        return v;
    }

    static class ViewHolder {
        @InjectView(R.id.game_name) TextView gameName;
        @InjectView(R.id.game_platform) TextView gamePlatform;
        @Optional @InjectView(R.id.is_completed) CheckBox isCompleted;
        @Optional @InjectView(R.id.game_rating) RatingBar gameRating;

        public ViewHolder(View v) {
            ButterKnife.inject(this, v);
        }
    }
}
