package com.stackroute.oops.league.dao;

import com.stackroute.oops.league.exception.PlayerAlreadyExistsException;
import com.stackroute.oops.league.exception.PlayerNotFoundException;
import com.stackroute.oops.league.model.Player;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is implementing the PlayerDao interface
 * This class has one field playerList and a String constant for storing file name
 */
public class PlayerDaoImpl implements PlayerDao {
    private static final String PLAYER_FILE_NAME = "F:\\niit_exercise\\JavaProject\\foundation_project_football_league_boilerplate\\src\\main\\resources\\player.csv";
    private List<Player> playerList;
    /**
     * Constructor to initialize an empty ArrayList for playerList
     */
    public PlayerDaoImpl() {
        playerList=new ArrayList<>();
    }

    /**
     * Return true if  player object is stored in "player.csv" as comma separated fields successfully
     * when password length is greater than six and yearExpr is greater than zero
     */
    @Override
    public boolean addPlayer(Player player) throws PlayerAlreadyExistsException, IOException {
        readFile(player);
        if(player.getPassword().length()>6&&player.getYearExpr()>0) {
           writeToFile(player);
            return true;
        }
        return false;
    }



    /**
     * Return Player object given playerId to search
     */
    @Override
    public Player findPlayer(String playerId) throws PlayerNotFoundException,FileNotFoundException,IOException {
        List<Player> players= getAllPlayers();
        if(players.stream().anyMatch(play -> play.getPlayerId().equals(playerId))) {
            return players.stream().filter(play -> play.getPlayerId().equals(playerId)).findAny().get();
        }
        else{
            throw new PlayerNotFoundException();
        }
    }


    //Return the list of player objects by reading data from the file "player.csv"
    @Override
    public List<Player> getAllPlayers() throws FileNotFoundException,IOException {
        playerList.clear();
        BufferedReader bufferedReader=getReader();
        String line;
        while ((line=bufferedReader.readLine())!=null){
            String[] fields=line.split(",");
            playerList.add(new Player(fields[0],fields[1],fields[3],Integer.parseInt(fields[2])));
        }
        return playerList;
    }



    //To write into file
    public void writeToFile(Player player) throws IOException {
        BufferedWriter bufferedWriter=getWriter();
        bufferedWriter.write(getPlayerDetail(player));
        bufferedWriter.newLine();
        bufferedWriter.flush();
    }

    //To Read data from File
    public void readFile(Player player) throws IOException, PlayerAlreadyExistsException {
       BufferedReader bufferedReader=null;
        bufferedReader=getReader();
            String line;
            while((line=bufferedReader.readLine())!=null){
                if(getPlayerDetail(player).equals(line)){
                    throw new PlayerAlreadyExistsException();
                }
            }
            bufferedReader.close();
    }
    //To get Writer
    public BufferedWriter getWriter() throws IOException {
        return new BufferedWriter(new FileWriter(PLAYER_FILE_NAME,true));
    }

    //To get player Detail
    public String getPlayerDetail(Player player){
       return player.getPlayerId() + "," + player.getPlayerName() + "," + player.getYearExpr()+","+player.getPassword();
    }

    //To get Reader Object
    public BufferedReader getReader() throws FileNotFoundException {
        return new BufferedReader(new FileReader(PLAYER_FILE_NAME));
    }
}
