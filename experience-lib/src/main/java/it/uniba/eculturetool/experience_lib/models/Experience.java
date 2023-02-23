package it.uniba.eculturetool.experience_lib.models;

import java.util.Objects;
import java.util.UUID;

public abstract class Experience {
    protected String id;
    protected Difficulty difficulty;
    protected int points = 0;

    public Experience(String id, Difficulty difficulty, int points) {
        this.id = id;
        this.difficulty = difficulty;
        this.points = points;
    }

    public Experience(Difficulty difficulty, int points) {
        this.id = UUID.randomUUID().toString();
        this.difficulty = difficulty;
        this.points = points;
    }

    public Experience() {
        this.id = UUID.randomUUID().toString();
        difficulty = Difficulty.LOW;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Experience)) return false;
        Experience that = (Experience) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
