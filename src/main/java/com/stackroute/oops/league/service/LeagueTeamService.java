package com.stackroute.oops.league.service;


import com.stackroute.oops.league.exception.*;
import com.stackroute.oops.league.model.Player;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface LeagueTeamService {
    boolean addPlayer(Player player) throws PlayerAlreadyExistsException, IOException;

    String registerPlayerToLeague(String playerId, String password, LeagueTeamTitles teamTitle)
            throws PlayerNotFoundException, TeamAlreadyFormedException, PlayerAlreadyAllottedException, IOException;

    List<Player> getAllRegisteredPlayers();

    int getExistingNumberOfPlayersInTeam(LeagueTeamTitles teamTitle) throws IOException;

    String allotPlayersToTeam(String adminName, String password, LeagueTeamTitles teamTitle)
            throws TeamAlreadyFormedException, PlayerNotFoundException, IOException;
}
