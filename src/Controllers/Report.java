package Controllers;

import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class Report {
    private List<Stat> statList = new ArrayList<Stat>();

    private Player player;

    private String playerString;

    private int serveAttempts = 0;
    private double serveRating = 0;
    private double serveError = 0;
    private double serveAce = 0;

    private int recAttempts = 0;
    private double recRating = 0;
    private double recError = 0;

    private int attackAttempts = 0;
    private double attackKill = 0;
    private double attackError = 0;
    private double attackEff = 0;

    private int setAttempts = 0;
    private double setAssists = 0;
    private double setError = 0;

    public Report(List<Stat> sl, Player p) {
        statList = sl;
        player = p;
        playerString = player.getName() + " " + player.getNumber();

        for (Stat s : statList) {
            if (s.getPlayer().getPlayer_id() == p.getPlayer_id()) {
                System.out.println(s.getAction().getActionId());
                switch(s.getAction().getActionId()) {

                    case 1:
                        serveAttempts++;
                        serveAce++;
                        break;
                    case 2:
                        serveAttempts++;
                        serveError++;
                        break;
                    case 3:
                        serveAttempts++;
                        break;
                    case 4:
                        recAttempts++;
                        recRating += 1;
                        break;
                    case 5:
                        recAttempts++;
                        recRating += 2;
                        break;
                    case 6:
                        recAttempts++;
                        recRating += 3;
                        break;
                    case 7:
                        recAttempts++;
                        recError++;
                        break;
                    case 8:
                        attackAttempts++;
                        attackKill++;
                        attackEff++;
                        break;
                    case 9:
                        attackAttempts++;
                        attackError++;
                        attackEff --;
                        break;
                    case 10:
                        attackAttempts++;
                        break;
                    case 11:
                        setAttempts++;
                        setAssists++;
                        break;
                    case 12:
                        setAttempts++;
                        setError++;
                        break;
                    case 13:
                        setAttempts++;
                        break;
                    default:
                        System.out.println("something went wrong");
                }
            }
        }

        /*
                Serving stats:
        Attempts, Rating, Error%, Ace%

        Receiving stats:
        Attempts, Rating, Error%

        Attack stats:
        Attempts, Kill%, Error%, Efficency%

        Setting stats:
        Attempts, Assist, Assist Error
         */
        if (serveAttempts > 0) {
            serveRating = (serveAce - serveError) / serveAttempts;
            serveError = serveError / serveAttempts;
            serveAce = serveAce / serveAttempts;
        }

        if (recAttempts > 0) {
            recRating = recRating / recAttempts;
            recError = recError / recAttempts;
        }

        if (attackAttempts > 0) {
            attackEff = attackEff / attackAttempts;
            attackKill = attackKill / attackAttempts;
            attackError = attackError / attackAttempts;
        }

        if (setAttempts > 0) {
            setAssists = setAssists / setAttempts;
            setError = setError / setAttempts;
        }

    }

    public Player getPlayer() {
        return player;
    }

    public String getPlayerString() {
        return playerString;
    }

    public int getServeAttempts() {
        return serveAttempts;
    }

    public double getServeRating() {
        return serveRating;
    }

    public double getServeError() {
        return serveError;
    }

    public double getServeAce() {
        return serveAce;
    }

    public int getRecAttempts() {
        return recAttempts;
    }

    public double getRecRating() {
        return recRating;
    }

    public double getRecError() {
        return recError;
    }

    public int getAttackAttempts() {
        return attackAttempts;
    }

    public double getAttackKill() {
        return attackKill;
    }

    public double getAttackError() {
        return attackError;
    }

    public double getAttackEff() {
        return attackEff;
    }

    public int getSetAttempts() {
        return setAttempts;
    }

    public double getSetAssists() {
        return setAssists;
    }

    public double getSetError() {
        return setError;
    }
}


