package it.uniba.eculturetool.experience_lib.models.hittheenemy;

import java.util.ArrayList;
import java.util.List;

import it.uniba.eculturetool.experience_lib.models.Experience;

public class HitTheEnemy extends Experience {
    private List<HitTheEnemyItem> hitTheEnemies = new ArrayList<>();

    public HitTheEnemy() {}

    public List<HitTheEnemyItem> getHitTheEnemies() {
        return hitTheEnemies;
    }

    public void setHitTheEnemies(List<HitTheEnemyItem> hitTheEnemies) {
        this.hitTheEnemies = hitTheEnemies;
    }
}
