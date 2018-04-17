/**
 * Created by Alex on 3/4/2017.
 * Represents garfield in battle.
 */
public class Player extends Actor{
    int weaponDamage; //weapon damage
    String weaponName;
    int exp = 0;
    int gold = 0;
    int potionNum = 3;
    int nextLvl;

    String potion(){ //potion usage is somewhat scaled so that in the later game they aren't useless
        if(this.potionNum > 0) {
            if (this.level < 6) {
                if (25 + this.currHP < this.maxHP) {
                    currHP += 25;
                    this.potionNum--;
                    return "Potion restored 25 points of health! " + (potionNum) + " remaining";
                } else {
                    currHP = maxHP;
                    this.potionNum--;
                    return "Potion restored you back to full health! " + (potionNum) + " remaining";
                }
            }
            else{
                if (40 + this.currHP < this.maxHP) {
                    currHP += 40;
                    this.potionNum--;
                    return "Potion restored 40 points of health! " + (potionNum) + " remaining";
                } else {
                    currHP = maxHP;
                    this.potionNum--;
                    return "Potion restored you back to full health! " + (potionNum) + " remaining";
                }
            }
        }
        else{
            return "You're out of potions!";
        }
    }

    void Player(int level){
        this.level = level;
        setStrength();
        setMaxHP();
        setCurrHP(maxHP);
        setName();
        setSpeed();
        setAttack();
        this.nextLvl = this.level*(4*this.level);
        this.nextLvl = this.nextLvl*(this.nextLvl/3) + 40;
        this.nextLvl = this.nextLvl - this.exp;
    }

    boolean canLevelUp(int x){
        this.nextLvl = this.level*(4*this.level);
        this.nextLvl = this.nextLvl*(this.nextLvl/3) + 40;
        this.nextLvl = this.nextLvl - x;
        if(nextLvl < 1){
            return true;
        }
        else{
            return false;
        }
    }

    void setWeapon(String wn, int wd){
        weaponDamage = wd;
        weaponName = wn;
    }

    void setStrength(){
        strength = 2 + level;
    }

    void setAttack(){
        attack = weaponDamage + strength*weaponDamage;
    }

    void setMaxHP(){
        maxHP = 30 + 5*level;
    }

    void setCurrHP(int hp){
        currHP = hp;
    }

    void setSpeed(){
        speed = 7 + 2*level;
    }


    void setName(){
        this.name = "Garfield";
    }

    void setGold(int g){
        this.gold = this.gold + g;
    }

    void addPotion(){ //will be used in town
        this.potionNum++;
    }
}
