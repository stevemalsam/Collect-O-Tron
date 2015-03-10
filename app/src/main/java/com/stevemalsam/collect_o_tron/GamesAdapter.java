package com.stevemalsam.collect_o_tron;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.stevemalsam.collect_o_tron.models.Game;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by stevo on 3/10/15.
 */
public class GamesAdapter extends ArrayAdapter<Game> {

    public GamesAdapter(Context context, int resource, List<Game> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        return super.getView(position, convertView, parent);

        View v = convertView;
        ViewHolder holder;
        if(v == null) {
            LayoutInflater inflater;
            inflater = LayoutInflater.from(getContext());
            v = inflater.inflate(R.layout.list_game_item, parent, false);
            holder = new ViewHolder(v);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        Game game = getItem(position);
        holder.gameName.setText(game.name);
        holder.gamePlatform.setText(game.platform);
        holder.isCompleted.setChecked(game.isCompleted);

        return v;
    }

    static class ViewHolder {
        @InjectView(R.id.game_name) TextView gameName;
        @InjectView(R.id.game_platform) TextView gamePlatform;
        @InjectView(R.id.is_completed) CheckBox isCompleted;

        public ViewHolder(View v) {
            ButterKnife.inject(this, v);
        }
    }
}
