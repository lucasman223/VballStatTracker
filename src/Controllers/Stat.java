package Controllers;

public class Stat {
    private Player player;
    private Action action;
    private int statID;

    private String playerString;
    private String actionString;

    public Stat(Player p, Action a, int s) {
        this.player = p;
        this.action = a;
        this.statID = s;

        playerString = player.getName() + " " + player.getNumber();
        actionString = action.getName();
    }

    public Player getPlayer() {
        return player;
    }

    public Action getAction() {
        return action;
    }

    public int getStatID() {
        return statID;
    }

    public String getPlayerString() {
        return playerString;
    }

    public String getActionString() {
        return actionString;
    }
}
