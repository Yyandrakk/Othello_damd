package es.uam.oscar_garcia.othello.model;

import java.util.Date;
import java.util.UUID;

/**
 * Created by oscar on 1/03/17.
 */

public class Round {

    private int size=8;
    private UUID player1;
    private String id;
    private String title;
    private String date;
    private OthelloBoard board;
    private String firstPlayerName;
    private String secondPlayerName;
    /**
     *
     * @param size
     */
    public Round(int size,UUID player) {
        this.size = size;
        id = UUID.randomUUID().toString();
        title = "ROUND " + id.toString().substring(19, 23).toUpperCase();
        //title = getString(R.string.round_name) + id.toString().substring(19, 23).toUpperCase();
        date = new Date().toString();
        board = new OthelloBoard(size);
        player1=player;
    }

    /**
     *
     * @return
     */
    public int getSize() { return size;}

    /**
     *
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public OthelloBoard getBoard() {
        return board;
    }
    public void setBoard(OthelloBoard board) {
        this.board = board;
    }

    public UUID getPlayerUUID() {
        return player1;
    }

    public void setFirstPlayerName(String firstPlayerName) {
        this.firstPlayerName = firstPlayerName;
    }

    public void setSecondPlayerName(String secondPlayerName) {
        this.secondPlayerName = secondPlayerName;
    }
}
