package com.stackroute.oops.league.dao;

import com.stackroute.oops.league.exception.PlayerNotFoundException;
import com.stackroute.oops.league.model.Player;
import com.stackroute.oops.league.model.PlayerTeam;

import java.io.*;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * This class implements the PlayerTeamDao interface
 * This class has two fields playerTeamSet,playerDao and a String constant for storing file name.
 */
public class PlayerTeamDaoImpl implements PlayerTeamDao {
    private static final String TEAM_FILE_NAME = "F:\\niit_exercise\\JavaProject\\foundation_project_football_league_boilerplate\\src\\main\\resources\\finalteam.csv";
    private PlayerDao playerDao;
    private Set<PlayerTeam> players;
    /**
     * Constructor to initialize an empty TreeSet and PlayerDao object
     */
    public PlayerTeamDaoImpl() {
     playerDao=new PlayerDaoImpl();
     players=new TreeSet<>();
    }
    /*
    Returns the list of players belonging to a particular teamTitle by reading
    from the file finalteam.csv
     */
    @Override
    public Set<PlayerTeam> getPlayerSetByTeamTitle(String teamTitle) throws IOException {
        Set<PlayerTeam> playerTeamSet=getAllPlayerTeams();
        if(playerTeamSet.size()==0){
            return playerTeamSet;
        }
       return playerTeamSet.stream().filter(play->play.getTeamTitle().equalsIgnoreCase(teamTitle)).collect(Collectors.toSet());
    }

    //Add he given PlayerTeam Object to finalteam.csv file
    @Override
    public boolean addPlayerToTeam(Player player) throws PlayerNotFoundException, IOException {
        List<Player> players=playerDao.getAllPlayers();
        if(players.stream().anyMatch(play->play.getPlayerId().equals(player.getPlayerId()))) {
           writeIntoFile(player);
            return true;
        }
        throw new PlayerNotFoundException();
    }


    //Return the set of all PlayerTeam by reading the file content from finalteam.csv file
    @Override
    public Set<PlayerTeam> getAllPlayerTeams() throws IOException {
        players.clear();
        BufferedReader bufferedReader=getReaderForTeam();
        String line;
        while((line=bufferedReader.readLine())!=null ){
            addPlayerToList(line);
        }
        return players;
    }




    public void writeIntoFile(Player player) throws IOException {
        BufferedWriter bufferedWriter=getWriterForTeam();
        bufferedWriter.write(playerDetail(player));
        bufferedWriter.newLine();
        bufferedWriter.flush();
    }


    public String playerDetail(Player player){
        return player.getPlayerId()+","+player.getPlayerName()+","+player.getTeamTitle()+","+player.getYearExpr()+","+player.getPassword();
    }

    public BufferedWriter getWriterForTeam() throws IOException {
      return new BufferedWriter(new FileWriter(TEAM_FILE_NAME,true));
    }


    public void addPlayerToList(String line){
        if(!(line.equals(""))){
            String feilds[]=line.split(",");
            players.add(new PlayerTeam(feilds[0],feilds[2]));
        }
    }


    public BufferedReader getReaderForTeam() throws FileNotFoundException {
        return new BufferedReader(new FileReader(TEAM_FILE_NAME));
    }
}
