package com.stackroute.oops.league.dao;

import com.stackroute.oops.league.exception.PlayerAlreadyExistsException;
import com.stackroute.oops.league.exception.PlayerNotFoundException;
import com.stackroute.oops.league.model.Player;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface PlayerDao {

    boolean addPlayer(Player player) throws PlayerAlreadyExistsException, FileNotFoundException, IOException;

    List<Player> getAllPlayers() throws FileNotFoundException, IOException;

    Player findPlayer(String playerId) throws PlayerNotFoundException, FileNotFoundException, IOException;


}
