import javafx.scene.image.Image;
import java.util.Random;

/**
 * Created by Alex on 3/4/2017.
 * A generic class that serves as a template for the Monster and Player classes
 */
public class Actor {
    int strength;
    int attack;
    int currHP;
    int maxHP;
    int speed;
    int level;
    String name;
    boolean alive = true;
    Image i;

    void Actor(int level){
        this.level = level;
        setStrength();
        setAttack();
        setMaxHP();
        setCurrHP(maxHP);
        setName();
        setSpeed();
    }



    void setStrength(){
        strength = 3 + 2*level;
        //overwrite based on which monster
    }

    void setAttack(){
        attack = strength;
        //use strength*weapon for player
    }

    void setMaxHP(){
        maxHP = 25 + 5*level;
        //overwrite based on which monster
    }

    void setCurrHP(int x){this.currHP = x;
    }

    void setSpeed(){
        speed = 4 + 2*level;
        //to overwrite
    }

    void setName(){
        name = "Actor";
    }

    int fight(){
        //calculates the damage (the output)
        //speed and animation will be in the main class
        Random r = new Random();
        int ret = r.nextInt(3);
        boolean pos = r.nextBoolean();
        if(pos){
            return attack+ret;
        }
        else{
            return attack-ret;
        }
    }
}
