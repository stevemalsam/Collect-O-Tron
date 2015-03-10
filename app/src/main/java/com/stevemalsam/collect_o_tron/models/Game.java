package com.stevemalsam.collect_o_tron.models;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by stevo on 3/9/15.
 */
public class Game {

    public static List<Game> Games = new ArrayList<>();

    public String name;
    public String platform;
    public boolean isCompleted;
    URL boxArtURL;
}
