package it.uniba.eculturetool.experience_lib.fragments.hittheenemy;

import androidx.lifecycle.ViewModel;

import it.uniba.eculturetool.experience_lib.models.hittheenemy.HitTheEnemy;
import it.uniba.eculturetool.experience_lib.models.hittheenemy.HitTheEnemyItem;

public class HitTheEnemyViewModel extends ViewModel {
    private HitTheEnemy hitTheEnemy = new HitTheEnemy();
    private HitTheEnemyItem activeHitTheEnemyItem;

    public HitTheEnemy getHitTheEnemy() {
        return hitTheEnemy;
    }

    public void setHitTheEnemy(HitTheEnemy hitTheEnemy) {
        this.hitTheEnemy = hitTheEnemy;
    }

    public HitTheEnemyItem getActiveHitTheEnemyItem() {
        return activeHitTheEnemyItem;
    }

    public void setActiveHitTheEnemyItem(HitTheEnemyItem activeHitTheEnemyItem) {
        this.activeHitTheEnemyItem = activeHitTheEnemyItem;
    }
}
