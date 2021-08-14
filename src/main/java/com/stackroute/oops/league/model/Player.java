package com.stackroute.oops.league.model;

/**
 * This class contains five fields playerId,playerName,password,yearExpr and teamTitle
 * It is a subclass of Comparable interface
 */
public class Player extends Thread implements Comparable {
    //Parameterized Constructor to initialize all properties
    private String playerId;
    private String playerName;
    private String password;
    private String teamTitle;
    private int yearExpr;
    public Player(String playerId, String playerName, String password, int yearExpr) {
        this.playerId=playerId;
        this.playerName=playerName;
        this.yearExpr=yearExpr;
        this.password=password;
    }

    public String getPlayerId() {
        return playerId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getPassword() {
        return password;
    }

    public int getYearExpr() {
        return yearExpr;
    }

    //Return teamTitle if it is present and not empty
    public String getTeamTitle() {
        if(teamTitle!=null&&(!(teamTitle.equals("")))){
            return teamTitle;
        }
       return null;
    }

    public void setTeamTitle(String teamTitle) {
        this.teamTitle=teamTitle;
    }


    //Overridden hashcode method
    @Override
    public int hashCode() {
        return this.playerId.hashCode()+this.yearExpr+this.playerName.hashCode()+this.teamTitle.hashCode()+this.password.hashCode();
    }

    //compares player object based on playerId
    @Override
    public int compareTo(Object o) {
        Player player=(Player)o;
       return this.playerId.compareTo(player.playerId);
    }
    /**
     * This overridden method should return player details in below format
     * Player{playerId=xxxxx, playerName=xxxxxx, yearExpr=xxxxxx,teamTitle=xxxxxxxx}-> if league team title is set
     * Player{playerId=xxxxx, playerName=xxxxxx, yearExpr=xxxxxx}-> if league team title not set
     */
    @Override
    public String toString() {
        if(getTeamTitle()==null){
            return "Player{" +
                    "playerId=" + playerId +
                    ", playerName=" + playerName +
                    ", yearExpr=" + yearExpr +
                    '}';
        }
        return "Player{" +
                "playerId=" + playerId +
                ", playerName=" + playerName +
                ", yearExpr=" + yearExpr +
                ", teamTitle=" + teamTitle +
                '}';
    }
}
