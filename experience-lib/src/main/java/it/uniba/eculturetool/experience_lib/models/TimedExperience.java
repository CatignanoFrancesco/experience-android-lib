package it.uniba.eculturetool.experience_lib.models;

public abstract class TimedExperience extends Experience {
    public static final int MIN_SECONDS = 0;
    public static final int MIN_MINUTES = 0;
    public static final int MAX_SECONDS = 59;
    public static final int MAX_MINUTES = 119;

    private int minutes;
    private int seconds;

    public TimedExperience(String id, Difficulty difficulty, int points, int minutes, int seconds) {
        super(id, difficulty, points);
    }

    public TimedExperience(Difficulty difficulty, int points, int minutes, int seconds) {
        super(difficulty, points);
    }

    public TimedExperience() {
        super();
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }
}
