package it.uniba.eculturetool.experience_lib.models.hittheenemy;

import android.graphics.Bitmap;

import it.uniba.eculturetool.experience_lib.models.Difficulty;
import it.uniba.eculturetool.experience_lib.models.Experience;

public class HitTheEnemyItem extends Experience {
    private String characterName;
    private String uriCharacter;
    private int characterSpeed;
    private int characterResistance;
    private String uriBackground;
    private String uriEnemy;
    private String uriEnemyHit;
    private int hitSpeed;
    private transient Bitmap character;
    private transient Bitmap background;
    private transient Bitmap enemy;
    private transient Bitmap enemyHit;

    public HitTheEnemyItem(String id, Difficulty difficulty, int points) {
        super(id, difficulty, points);
    }

    public HitTheEnemyItem(Difficulty difficulty, int points) {
        super(difficulty, points);
    }

    public HitTheEnemyItem() {}

    public String getCharacterName() {
        return characterName;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }

    public String getUriCharacter() {
        return uriCharacter;
    }

    public void setUriCharacter(String uriCharacter) {
        this.uriCharacter = uriCharacter;
    }

    public String getUriBackground() {
        return uriBackground;
    }

    public void setUriBackground(String uriBackground) {
        this.uriBackground = uriBackground;
    }

    public String getUriEnemy() {
        return uriEnemy;
    }

    public void setUriEnemy(String uriEnemy) {
        this.uriEnemy = uriEnemy;
    }

    public String getUriEnemyHit() {
        return uriEnemyHit;
    }

    public void setUriEnemyHit(String uriEnemyHit) {
        this.uriEnemyHit = uriEnemyHit;
    }

    public Bitmap getCharacter() {
        return character;
    }

    public void setCharacter(Bitmap character) {
        this.character = character;
    }

    public Bitmap getBackground() {
        return background;
    }

    public void setBackground(Bitmap background) {
        this.background = background;
    }

    public Bitmap getEnemy() {
        return enemy;
    }

    public void setEnemy(Bitmap enemy) {
        this.enemy = enemy;
    }

    public Bitmap getEnemyHit() {
        return enemyHit;
    }

    public void setEnemyHit(Bitmap enemyHit) {
        this.enemyHit = enemyHit;
    }

    public int getCharacterSpeed() {
        return characterSpeed;
    }

    public void setCharacterSpeed(int characterSpeed) {
        this.characterSpeed = characterSpeed;
    }

    public int getCharacterResistance() {
        return characterResistance;
    }

    public void setCharacterResistance(int characterResistance) {
        this.characterResistance = characterResistance;
    }

    public int getHitSpeed() {
        return hitSpeed;
    }

    public void setHitSpeed(int hitSpeed) {
        this.hitSpeed = hitSpeed;
    }
}
