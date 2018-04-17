import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * Created by Alex on 3/5/2017.
 * controls the entire battle, returns strings to print to the screen and alters the stats of actors
 * at the end of the battle, exports the garfield(player) stats to the overworld
 * Also manages the music
 */
public class BattleManager {

    int state = 0;
    int turns = 2;
    int counter = 0;

    boolean fighting = false;
    boolean songPlaying = false;

    Monster m;
    Player p;

    MediaPlayer mP;

    void playFightSong() {
        Media song = new Media(ClassLoader.getSystemResource("battle.mp3").toString());
        mP = new MediaPlayer(song);
        mP.play();
        songPlaying = true;
    }

    void playWinSong() {
        Media song = new Media(ClassLoader.getSystemResource("dilbert3.mp3").toString());
        mP = new MediaPlayer(song);
        mP.play();
        songPlaying = true;
    }

    void playLoseSong() {
        Media song = new Media(ClassLoader.getSystemResource("dilbert2.mp3").toString());
        mP = new MediaPlayer(song);
        mP.play();
        songPlaying = true;
    }

    void playIntroSong() {
        Media song = new Media(ClassLoader.getSystemResource("opening.mp3").toString());
        mP = new MediaPlayer(song);
        mP.play();
        songPlaying = true;
    }

    void setPlayer(Player p) {
        this.p = p;
    }

    void enemy(Monster m) {
        this.m = m;
    }

    String[] battle(Actor attacker, Actor defender) {
        String[] battleText = new String[5];
        int damage = attacker.fight();
        battleText[0] = attacker.name + " attacked for " + damage + " damage";

        defender.setCurrHP(defender.currHP - damage);
        if (defender.currHP < 1) {
            defender.alive = false;
            battleText[1] = defender.name + " died!";
        } else {
            battleText[1] = "";
        }

        battleText[2] = "";
        battleText[3] = "";
        battleText[4] = "";

        return battleText;
    }

    String[] battleWin(Player p, Monster m) {
        m.currHP = m.maxHP;
        m.alive = true;
        String[] array = new String[5];
        array[0] = p.name + " defeated " + m.name + "!";
        p.setGold(m.gold);
        array[1] = "Got " + m.gold + " gold";
        p.exp = p.exp + m.exp;
        array[2] = "Got " + m.exp + " exp";
        if (p.canLevelUp(p.exp)) {
            p.Player(p.level + 1);
            array[3] = p.name + " leveled up to level " + (p.level);
            array[4] = "Your health was restored completely!";
        } else {
            array[3] = "";
            array[4] = "";
        }
        return array;
    }
}