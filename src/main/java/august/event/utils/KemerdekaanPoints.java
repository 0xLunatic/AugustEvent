package august.event.utils;

public class KemerdekaanPoints {
    private String playerName;
    private double kemerdekaanPoints;

    public KemerdekaanPoints(String playerName, double kemerdekaanPoints) {
        this.playerName = playerName;
        this.kemerdekaanPoints = kemerdekaanPoints;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public double getKemerdekaanPoints() {
        return kemerdekaanPoints;
    }

    public void setKemerdekaanPoints(double kemerdekaanPoints) {
        this.kemerdekaanPoints = kemerdekaanPoints;
    }
}
