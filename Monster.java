import javafx.scene.image.Image;

/**
 * Created by Alex on 3/5/2017.
 * Represents all enemies in the game.
 */
public class Monster extends Actor {
    int gold;
    Image i;
    int exp;

    void initialize(int gold, int level, int maxHP, int strength, int speed, String name, Image i){ //used in the monsterInitialize method in the javaRPG class
        this.gold = gold;
        this.name = name;
        this.i = i;
        this.level = level;
        this.maxHP = maxHP;
        this.currHP = maxHP;
        this.strength = strength;
        this.speed = speed;
        setExp();
        setAttack();
    }

    void setExp(){
        this.exp = this.strength + this.maxHP + this.speed;
    }
}
