package com.stackroute.oops.league.service;

import com.stackroute.oops.league.dao.PlayerDao;
import com.stackroute.oops.league.dao.PlayerDaoImpl;
import com.stackroute.oops.league.dao.PlayerTeamDao;
import com.stackroute.oops.league.dao.PlayerTeamDaoImpl;
import com.stackroute.oops.league.exception.PlayerAlreadyAllottedException;
import com.stackroute.oops.league.exception.PlayerAlreadyExistsException;
import com.stackroute.oops.league.exception.PlayerNotFoundException;
import com.stackroute.oops.league.exception.TeamAlreadyFormedException;
import com.stackroute.oops.league.model.Player;
import com.stackroute.oops.league.model.PlayerTeam;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class implements leagueTeamService
 * This has four fields: playerDao, playerTeamDao and registeredPlayerList and playerTeamSet
 */
public class LeagueTeamServiceImpl implements LeagueTeamService {
    private String fileName= "F:\\niit_exercise\\JavaProject\\foundation_project_football_league_boilerplate\\src\\main\\resources\\player.csv";
    private PlayerDao playerDao;
    private PlayerTeamDao playerTeamDao;
    private List<Player> registeredPlayerList;

    /**
     * static nested class to initialize admin credentials
     * admin name='admin' and password='pass'
     */
    static class AdminCredentials {
        private static String admin = "admin";
        private static String password = "pass";
    }
    /**
     * Constructor to initialize playerDao, playerTeamDao
     * empty ArrayList for registeredPlayerList and empty TreeSet for playerTeamSet
     */
    public LeagueTeamServiceImpl() {
        playerDao=new PlayerDaoImpl();
        playerTeamDao=new PlayerTeamDaoImpl();
        registeredPlayerList=new ArrayList<>();

    }

    //Add player data to file using PlayerDao object
    @Override
    public boolean addPlayer(Player player) throws PlayerAlreadyExistsException, IOException {
        playerDao.addPlayer(player);
        return true;
    }

    /**
     * Register the player for the given teamTitle
     * Throws PlayerNotFoundException if the player does not exists
     * Throws PlayerAlreadyAllottedException if the player is already allotted to team
     * Throws TeamAlreadyFormedException if the maximum number of players has reached for the given teamTitle
     * Returns null if there no players available in the file "player.csv"
     * Returns "Registered" for successful registration
     * Returns "Invalid credentials" when player credentials are wrong
     */
    @Override
    public synchronized String registerPlayerToLeague(String playerId, String password, LeagueTeamTitles teamTitle)
            throws PlayerNotFoundException, TeamAlreadyFormedException, PlayerAlreadyAllottedException, IOException {
        if(playerDao.getAllPlayers().size()==0){
            return null;
        }
        Player player=playerDao.findPlayer(playerId);
        if(player==null){
            throw new PlayerNotFoundException();
        }
        if(!player.getPassword().equals(password)){
            return "Invalid credentials";
        }
        if(playerTeamDao.getAllPlayerTeams().stream().anyMatch(play->play.getPlayerId().equals(playerId)))
               throw new PlayerAlreadyAllottedException();
        if(getNumberOfPlayersInTeam(teamTitle)>=11){
            throw new TeamAlreadyFormedException();
        }

        player.setTeamTitle(teamTitle.getValue());
        registeredPlayerList.add(player);
        return "Registered";
    }

    /**
     * Return the list of all registered players
     */
    @Override
    public List<Player> getAllRegisteredPlayers() {
        return registeredPlayerList;
    }


    /**
     * Return the existing number of players for the given title
     */
    @Override
    public int getExistingNumberOfPlayersInTeam(LeagueTeamTitles teamTitle) throws IOException {
     long counts=playerDao.getAllPlayers().stream().filter(play->play.getTeamTitle()!=null).filter(play1->play1.getTeamTitle().equals(teamTitle.getValue())).count();
     return (int)(counts);
    }

    /**
     * Admin credentials are authenticated and registered players are allotted to requested teams if available
     * If the requested teams are already formed,admin randomly allocates to other available teams
     * PlayerTeam object is added to "finalteam.csv" file allotted by the admin using PlayerTeamDao
     * Return "No player is registered" when registeredPlayerList is empty
     * Throw TeamAlreadyFormedException when maximum number is reached for all teams
     * Return "Players allotted to teams" when registered players are successfully allotted
     * Return "Invalid credentials for admin" when admin credentials are wrong
     */
    @Override
    public String allotPlayersToTeam(String adminName, String password, LeagueTeamTitles teamTitle) throws TeamAlreadyFormedException, PlayerNotFoundException, IOException {
        if((adminName.equals(AdminCredentials.admin))&& password.equals(AdminCredentials.password)){
            if(registeredPlayerList.size()==0){
                return "No player is registered";
            }
            if(checkAllTeamFilled()){
                throw new TeamAlreadyFormedException();
            }
            int regPlayerIndex=addPlayerToTeamTitle(0,getAllRegisteredPlayers(),teamTitle);
            if(regPlayerIndex<registeredPlayerList.size()){
                regPlayerIndex=addPlayerToTeamTitle(regPlayerIndex,getAllRegisteredPlayers());
            }
            if(regPlayerIndex!=registeredPlayerList.size()){
                if(checkAllTeamFilled()) {
                    throw new TeamAlreadyFormedException();
                }
            }
            return "Players allotted to teams";
        }
        return "Invalid credentials for admin";
    }



    public boolean checkPlayerTeam(Player play2) throws IOException {
        List<Player> playerSet1=playerDao.getAllPlayers();
      boolean present=playerSet1.stream().anyMatch(play->play.getPlayerId().equals(play2.getPlayerId()));
        if(present){
           Player playy= playerSet1.stream().filter(play->play.getPlayerId().equals(play2.getPlayerId())).findAny().get();
           if(playy.getTeamTitle()==null)
            return true;
        }
        return false;
    }

    public boolean checkAllTeamFilled() throws IOException, PlayerNotFoundException {
        List<Player> players=playerDao.getAllPlayers();
        for(LeagueTeamTitles leagueTeamTitles:LeagueTeamTitles.values()){
            if(getExistingNumberOfPlayersInTeam(leagueTeamTitles)<11)
                return false;
        }
        return true;
    }

    public int addPlayerToTeamTitle( int regPlayerIndex,List<Player> regPlayer,LeagueTeamTitles teamTitle) throws IOException, PlayerNotFoundException {
          while (regPlayerIndex!=regPlayer.size() && getExistingNumberOfPlayersInTeam(teamTitle)<11){
                if(checkPlayerTeam(regPlayer.get(regPlayerIndex))){
                    addPlayerToTeamTitle(regPlayer.get(regPlayerIndex),teamTitle);
                }
                regPlayerIndex++;
        }
        return regPlayerIndex;
    }

    public void addPlayerToTeamTitle(Player regPlayer,LeagueTeamTitles teamTitle) throws IOException, PlayerNotFoundException {
        regPlayer.setTeamTitle(teamTitle.getValue());
        playerTeamDao.addPlayerToTeam(regPlayer);
    }
    public int addPlayerToTeamTitle(int index, List<Player> regPlayer) throws IOException, PlayerNotFoundException {
        for(LeagueTeamTitles leagueTeamTitles:LeagueTeamTitles.values()){
            if(getExistingNumberOfPlayersInTeam(leagueTeamTitles)<11 && index<regPlayer.size()){
                addPlayerToTeamTitle(regPlayer.get(index++),leagueTeamTitles);
            }
        }
        return index;
    }
    public int getNumberOfPlayersInTeam(LeagueTeamTitles teamTitles) throws IOException {
        return playerTeamDao.getAllPlayerTeams().stream().filter(play->play.getTeamTitle().equals(teamTitles.getValue())).collect(Collectors.toList()).size();
    }

}

