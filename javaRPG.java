import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.util.Random;
import java.util.Scanner;

/**
 * gRPG, a garfield RPG
 * Created by Alex Kosla in March 2017.
 *
 * A primitive RPG influenced by Final Fantasy I. I decided to use a garfield theme as a placeholder, but decided to keep it.
 * Stylistic influences from the video Garfielf by Pilot Red Sun (https://www.youtube.com/watch?v=OGbhJjXl9Rk),
 * which is a rough prequel to the game; gRPG takes place after the events of the video.
 *
 * Known bugs:
 * 1. In the Wilds, it is possible to get stuck between two objects. To fix that, just go back to town and then come back to the wilds
 *
 */

public class javaRPG extends Application {

    final String appName = "javaRPG";
    final int FPS = 15;
    final static int HEIGHT = 500;
    final static int WIDTH = 500;

    int state = 5;
    int townState = 0;

    BattleManager bm = new BattleManager();
    Overworld ow = new Overworld();

    Font console = new Font("Lucida Console", 11);
    Font fightFont = new Font("Lucida Console", 14);
    Font fightFontBig = new Font("Lucida Console", 16);
    Font big = new Font("Comic Sans MS", 30);
    Font bigIntro = new Font("Comic Sans MS", 48);

    Monster[] b = new Monster[4];

    public static float BBscale = .95f;

    void initialize(){
        Player garfield = new Player();
        garfield.Player(1);
        garfield.setWeapon("Claws", 3); //the default weapon when starting a new game
        garfield.setAttack();
        bm.setPlayer(garfield);
        bm.fighting = false;
        monsterInitialize(bm.p.level);
        state = 1;
    }

    void monsterInitialize(int level){
        if(level < 6) {
            b[0] = new Monster();
            b[0].initialize(100 + (25 * (level - 1)), level - 1, 12 + (2 * (level - 1)), 14 + (1 * (level - 1)), 30 + (1 * (level - 1)), "A Mysterious Stranger", new Image("garfieldcowboy.png"));

            b[1] = new Monster();
            b[1].initialize(30 + (10 * (level - 1)), level - 1, 40 + (4 * (level - 1)), 3 + (1 * (level - 1)), 30 + (1 * (level - 1)), "Garfieri", new Image("garfieldfieri.png"));

            b[2] = new Monster();
            b[2].initialize(65 + (15 * (level - 1)), level - 1, 20 + (3 * (level - 1)), 5 + (1 * (level - 1)), 40 + (1 * (level - 1)), "An Intervention", new Image("garfieldintervention.png"));

            b[3] = new Monster();
            b[3].initialize(150 + (25 * (level - 1)), level - 1, 27 + (3 * (level - 1)), 12 + (1 * (level - 1)), 9 + (1 * (level - 1)), "An Escaped Jon Clone", new Image("jon0.gif"));
        }
        else{
            b[0] = new Monster();
            b[0].initialize(100 + (25 * (level - 1)), level - 1, 12 + (2 * (level - 1)), 14 + (1 * (level - 1)), 30 + (1 * (level - 1)), "A Mysterious Stranger", new Image("garfieldcowboy.png"));

            b[1] = new Monster();
            b[1].initialize(200 + (30 * (level - 1)), level - 1, 25 + (5 * (level - 1)), 7 + (2 * (level - 1)), 15 + (3 * (level - 1)), "Gazorpazorpfield", new Image("gazorp.png"));

            b[2] = new Monster();
            b[2].initialize(250 + (20 * (level - 1)), level - 1, 40 + (4 * (level - 1)), 8 + (1 * (level - 1)), 40 + (1 * (level - 1)), "A Monday", new Image("garfieldintervention.png"));

            b[3] = new Monster();
            b[3].initialize(180 + (30 * (level - 1)), level - 1, 35 + (3 * (level - 1)), 16 + (1 * (level - 1)), 9 + (2 * (level - 1)), "3D Jon", new Image("jon3d.png"));
        }
    }

    void startBattle(){
        if(bm.songPlaying){
            bm.mP.stop();
            bm.playFightSong();
        }
        Random r = new Random();

        bm.enemy(b[r.nextInt(4)]);
        ow.steplimit = 50+r.nextInt(30);
        ow.steps = 0;
        bm.fighting = true;
        bm.state = 0;
        state = 0;
    }

    void render(GraphicsContext gc) {
        // paint the game depending on state of game

        if (--bm.counter > 0)
            return;

        switch(state) {
            case 0: //battle

                gc.setFont(console); // set font in game
                gc.setFill(Color.BLACK);

                gc.fillRect(0, 0, WIDTH, HEIGHT); // clear buffer
                gc.rect(WIDTH - 20, 140, WIDTH - 20, HEIGHT - 20);

                gc.drawImage(bm.m.i, 250, 75, 200, 200);

                gc.setFill(Color.ANTIQUEWHITE);
                gc.setStroke(Color.ANTIQUEWHITE);
                gc.strokeRoundRect(10, 310, 350, 180, 20, 20);

                gc.setFont(fightFontBig);

                gc.strokeRoundRect(10, 10, 190, 40, 20, 20);
                gc.fillText("HP : " + bm.p.currHP+"/"+bm.p.maxHP, 35, 35);

                gc.setFont(console);
                gc.strokeRoundRect(10, 60, 190, 240, 20, 20);
                gc.fillText("Garfield's Other Stats ", 20, 75);
                gc.fillText("Level: "+bm.p.level, 20, 100);
                gc.fillText("Exp to next level : " + bm.p.nextLvl, 20, 125);
                gc.fillText("Strength : " + bm.p.strength, 20, 150);
                gc.fillText("Weapon damage : " + bm.p.weaponDamage, 20, 175);
                gc.fillText("Attack : " + bm.p.attack, 20, 200);
                gc.fillText("Speed : " + bm.p.speed, 20, 225);
                gc.fillText("Gold : " + bm.p.gold, 20, 250);
                gc.fillText("Potions : " + bm.p.potionNum, 20, 275);

                gc.strokeRoundRect(250, 10, 200, 30, 20, 20);
                gc.setFont(fightFont);
                gc.fillText(bm.m.name, 265, 30, 215);

                gc.strokeRoundRect(370, 310, 120, 180, 20, 20);

                gc.setFont(fightFontBig);
                gc.fillText("Z: Fight", 380, 350);
                gc.fillText("X: Potion", 380, 400); //check if there are any potions in inventory, if not, then do nothing
                gc.fillText("C: Run", 380, 450);
                gc.setFont(console);

                switch (bm.state) {
                    case 0: //encounter

                        if (!bm.songPlaying) {
                            bm.playFightSong();
                        } else {}

                        consoleText(gc, new String[]{"Encountered " + bm.m.name + "!", "", "", "", ""});
                        bm.state = 1;
                        bm.counter = 40;
                        break;

                    case 1: //choice
                        consoleText(gc, new String[]{"What will you do?", "", "", "", ""});
                        bm.counter = 30;
                        break;

                    case 2: //attack
                        consoleText(gc, bm.battle(bm.p, bm.m));
                        gc.setLineWidth(15);
                        gc.setStroke(Color.RED);
                        gc.strokeLine(275, 75, 425, 275);
                        gc.setLineWidth(1);
                        bm.turns--;

                        if (!bm.m.alive) {
                            bm.counter = 25;
                            bm.state = 6;
                        } else {
                            if (bm.turns < 1) {
                                bm.turns = 2;
                                bm.counter = 25;
                                bm.state = 1;
                            } else {
                                bm.counter = 25;
                                bm.state = 3;
                            }
                        }
                        break;

                    case 3: //defend
                        consoleText(gc, bm.battle(bm.m, bm.p));
                        bm.turns--;

                        if (!bm.p.alive) {
                            bm.mP.stop();
                            bm.playLoseSong();
                            bm.counter = 35;
                            bm.state = 7;
                        } else {
                            if (bm.turns < 1) {
                                bm.turns = 2;
                                bm.counter = 25;
                                bm.state = 1;
                            } else {
                                bm.counter = 25;
                                bm.state = 2;
                            }
                        }
                        break;

                    case 4: //potion
                        consoleText(gc, new String[]{bm.p.potion(), "", "", "", ""});
                        bm.turns--;
                        bm.counter = 40;
                        bm.state = 3;
                        break;

                    case 5: //run
                        if (bm.p.speed > bm.m.speed + 5) {
                            consoleText(gc, new String[]{"You tried to run away.", "You're a lot faster than " + bm.m.name + "!", "You got away!", "", ""});
                            bm.counter = 60;
                            bm.mP.stop();
                            bm.playWinSong();
                            bm.fighting = false;
                            bm.turns = 2;
                            state = 1;
                        } else {
                            consoleText(gc, new String[]{"You tried to run away.", "But "+bm.m.name + " is able to catch you!", "You didn't get away!", "", ""});
                            bm.turns--;
                            bm.counter = 50;
                            bm.state = 3;
                        }
                        break;

                    case 6: //win
                        consoleText(gc, bm.battleWin(bm.p, bm.m));
                        monsterInitialize(bm.p.level);
                        bm.counter = 70;
                        bm.mP.stop();
                        bm.playWinSong();
                        bm.fighting = false;
                        bm.turns = 2;
                        state = 1;
                        break;

                    case 7: //lose
                        bm.counter = 25;
                        bm.fighting = false;
                        state = 2;
                        break;

                }
                break;

            case 1: //overworld
                if(!bm.songPlaying){
                    bm.playWinSong();
                }
                ow.render(gc);
                if(ow.owp.newArea){
                    Overworld ow1 = new Overworld();
                    ow1.Overworld(ow.owp.x, ow.owp.y);
                    ow = ow1;
                    ow.owp.newArea = false;
                    ow.render(gc);
                }
                if(ow.steps > ow.steplimit) {
                    ow.owp.goUp = false;
                    ow.owp.goDown = false;
                    ow.owp.goLeft = false;
                    ow.owp.goRight = false;
                    startBattle();
                }
                break;

            case 2: //game over
                gc.setFill(Color.GHOSTWHITE);
                gc.fillRect(0, 0, WIDTH, HEIGHT);
                gc.setFill(Color.GOLDENROD);
                gc.setFont(big);
                gc.drawImage(new Image("garfielddead.png"), 0, 300, WIDTH, 200);
                gc.fillText("R.I.P. GARFIELD", 150, 50);
                gc.fillText("You will be missed...", 100, 150);
                gc.fillText("Press Z to return to the menu.", 20, 200);
                break;

            case 3: //menu
                gc.setFont(console); // set font in game
                gc.setFill(Color.BLACK);

                gc.fillRect(0, 0, WIDTH, HEIGHT); // clear buffer
                gc.rect(WIDTH - 20, 140, WIDTH - 20, HEIGHT - 20);

                gc.drawImage(new Image("garfield1.gif"), 300, 90, 150, 200);

                gc.setFill(Color.ANTIQUEWHITE);
                gc.setStroke(Color.ANTIQUEWHITE);

                gc.setFont(fightFontBig);
                gc.strokeRoundRect(10, 10, 240, 480, 20, 20);
                gc.fillText("Stats ", 100, 40);

                gc.setFont(fightFont);
                gc.fillText("Level : "+bm.p.level, 20, 90);
                gc.fillText("Current HP : "+bm.p.currHP, 20, 130);
                gc.fillText("Maximum HP : " + bm.p.maxHP, 20, 170);
                gc.fillText("Exp to next level : " + bm.p.nextLvl, 20, 210);
                gc.fillText("Strength : " + bm.p.strength, 20, 250);
                gc.fillText("Weapon damage : " + bm.p.weaponDamage, 20, 290);
                gc.fillText("Attack : " + bm.p.attack, 20, 330);
                gc.fillText("Speed : " + bm.p.speed, 20, 370);
                gc.fillText("Gold : " + bm.p.gold, 20, 410);
                gc.fillText("Potions : " + bm.p.potionNum, 20, 450);

                gc.strokeRoundRect(260, 10, 230, 50, 20, 20);
                gc.setFont(fightFontBig);
                gc.fillText("Garfield", 335, 40, 115);

                gc.strokeRoundRect(260, 300, 230, 190, 20, 20);

                gc.setFont(fightFont);
                gc.fillText("Press ESC:", 270, 325);
                gc.fillText("     to exit the menu", 270, 345);
                gc.fillText("Press Z:", 270, 375);
                gc.fillText("     to return to town", 270, 395);
                gc.fillText("Press X:", 270, 425);
                gc.fillText("     to return to the", 270, 445);
                gc.fillText("     title screen", 270, 465);
                break;
            case 4: //town
                switch(townState) {
                    case 0: //townMenu
                        gc.setFont(console); // set font in game
                        gc.setFill(Color.BLACK);

                        gc.fillRect(0, 0, WIDTH, HEIGHT); // clear buffer
                        gc.rect(WIDTH - 20, 140, WIDTH - 20, HEIGHT - 20);

                        gc.drawImage(new Image("muncie.jpg"), 240, 20, 230, 130);

                        gc.setFill(Color.ANTIQUEWHITE);
                        gc.setStroke(Color.ANTIQUEWHITE);

                        gc.setFont(fightFontBig);
                        gc.strokeRoundRect(10, 10, 480, 290, 20, 20);
                        gc.fillText("Muncie, Indiana", 50, 40);

                        gc.setFont(fightFont);
                        gc.fillText("Welcome back to town!", 20, 90);
                        gc.fillText("What do you want to do?", 20, 130);
                        gc.fillText("You can go back to Jon's house and", 20, 170);
                        gc.fillText("   try to pay off your debt.", 20, 185);
                        gc.fillText("You can sleep at the Inn and save your game", 20, 215);
                        gc.fillText("   until Jon lets you back in the house.", 20, 230);
                        gc.fillText("You can also go to the shop to buy weapons and potions.", 20, 270);

                        gc.strokeRoundRect(10, 310, 480, 180, 20, 20);

                        gc.setFont(fightFont);
                        gc.fillText("Press Z:", 30, 330);
                        gc.fillText("     to go to Jon's house", 30, 350);
                        gc.fillText("Press X:", 30, 380);
                        gc.fillText("     to go to the Inn", 30, 400);
                        gc.fillText("Press C:", 30, 430);
                        gc.fillText("     to go to the Shop", 30, 450);

                        gc.fillText("Press ESC:", 270, 330);
                        gc.fillText("     to go back to", 270, 350);
                        gc.fillText("     the Title Screen", 270, 370);
                        gc.fillText("Press V:", 270, 400);
                        gc.fillText("     to go back to", 270, 420);
                        gc.fillText("     the Wilds", 270, 440);
                        break;

                    case 1: //jon's house
                        gc.setFont(console); // set font in game
                        gc.setFill(Color.BLACK);

                        gc.fillRect(0, 0, WIDTH, HEIGHT); // clear buffer
                        gc.rect(WIDTH - 20, 140, WIDTH - 20, HEIGHT - 20);

                        gc.drawImage(new Image("jonpixel.png"), 250, 20, 200, 200);

                        gc.setFill(Color.ANTIQUEWHITE);
                        gc.setStroke(Color.ANTIQUEWHITE);

                        gc.setFont(fightFontBig);
                        gc.strokeRoundRect(10, 10, 480, 290, 20, 20);
                        gc.fillText("Jon's House", 50, 40);

                        gc.setFont(fightFont);
                        gc.fillText("\"Ah, Garfield...\"", 20, 90);
                        gc.fillText("\"You've come crawling back,", 20, 130);
                        gc.fillText("   I see.\"", 20, 150);
                        gc.fillText("\"I still haven't gotten", 20, 180);
                        gc.fillText("   that 10,000 gold...\"", 20, 200);
                        gc.fillText("\"Do you have the money?\"", 20, 230);
                        gc.fillText("\"If you don't, I think it's best you leave.\"", 20, 250);

                        gc.strokeRoundRect(10, 310, 480, 180, 20, 20);

                        gc.setFont(fightFont);
                        gc.fillText("You have:", 30, 330);
                        gc.fillText("   "+bm.p.gold+" gold", 30, 350);

                        if(bm.p.gold < 10000) {
                            gc.fillText("You don't have enough", 30, 380);
                            gc.fillText("   money to pay Jon.", 30, 400);
                            gc.fillText("   You still need", 30, 420);
                            gc.fillText("   " + (10000 - bm.p.gold) + " more gold.", 30, 450);
                            gc.fillText("Press X:", 270, 330);
                            gc.fillText("   to return to town", 270, 350);
                        }
                        else {
                            gc.fillText("Press Z:", 30, 380);
                            gc.fillText("   to pay off debt", 30, 400);
                            gc.fillText("Press X:", 30, 430);
                            gc.fillText("   to return to town", 30, 450);
                        }
                        break;

                    case 2: //the inn
                        gc.setFont(console); // set font in game
                        gc.setFill(Color.BLACK);

                        gc.fillRect(0, 0, WIDTH, HEIGHT); // clear buffer
                        gc.rect(WIDTH - 20, 140, WIDTH - 20, HEIGHT - 20);

                        gc.drawImage(new Image("billmurray.jpg"), 275, 20, 200, 250);

                        gc.setFill(Color.ANTIQUEWHITE);
                        gc.setStroke(Color.ANTIQUEWHITE);

                        gc.setFont(fightFontBig);
                        gc.strokeRoundRect(10, 10, 480, 290, 20, 20);
                        gc.fillText("Muncie Inn", 50, 40);

                        gc.setFont(fightFont);
                        gc.fillText("\"Hey, Garfield!\"", 20, 90);
                        gc.fillText("\"I have some available beds", 20, 130);
                        gc.fillText("   here at the Inn if you want", 20, 150);
                        gc.fillText("   to rest and save your game.\"", 20, 170);
                        gc.fillText("\"It's 150 gold per night\"", 20, 200);
                        gc.setFont(new Font("Comic Sans MS", 6));
                        gc.fillText("Hey, quit lookin' at me like that, I ain't some kind of freak!", 20, 230); //a bad joke
                        gc.setFont(fightFont);

                        gc.strokeRoundRect(10, 310, 480, 180, 20, 20);

                        if(bm.p.gold < 150) {
                            gc.fillText("You don't have enough", 30, 330);
                            gc.fillText("money to pay Bill Murray.", 30, 350);
                            gc.fillText("You still need", 30, 370);
                            gc.fillText("" + (150 - bm.p.gold) + " more gold.", 30, 390);
                            gc.fillText("Press X:", 270, 330);
                            gc.fillText("   to return to town", 270, 350);
                        }
                        else {
                            gc.fillText("Press Z:", 30, 380);
                            gc.fillText("   to rent a room", 30, 400);
                            gc.fillText("Press X:", 30, 430);
                            gc.fillText("   to return to town", 30, 450);
                        }
                        break;

                    case 3: //sleeping
                        gc.setFont(fightFontBig); // set font in game
                        gc.setFill(Color.BLACK);

                        gc.fillRect(0, 0, WIDTH, HEIGHT); // clear buffer
                        gc.rect(WIDTH - 20, 140, WIDTH - 20, HEIGHT - 20);

                        gc.drawImage(new Image("garfieldsleep.gif"),150,150,200,200);

                        gc.setFill(Color.ANTIQUEWHITE);
                        gc.setStroke(Color.ANTIQUEWHITE);

                        gc.fillText("Goodnight, Garfield!", 150, 50);

                        gc.fillText("Garfield's health was completely restored.", 10, 400);
                        gc.fillText("Game Saved. Press X to wake up", 10, 440);
                        break;

                    case 4: //shop
                        gc.setFont(console); // set font in game
                        gc.setFill(Color.BLACK);

                        gc.fillRect(0, 0, WIDTH, HEIGHT); // clear buffer
                        gc.rect(WIDTH - 20, 140, WIDTH - 20, HEIGHT - 20);

                        gc.drawImage(new Image("nermal.jpg"), 250, 20, 180, 180);

                        gc.setFill(Color.ANTIQUEWHITE);
                        gc.setStroke(Color.ANTIQUEWHITE);

                        gc.setFont(fightFontBig);
                        gc.strokeRoundRect(10, 10, 480, 290, 20, 20);
                        gc.fillText("Nermal's Bazaar", 50, 40);

                        gc.setFont(fightFont);
                        gc.fillText("\"Heya Garfield!\"", 20, 100);
                        gc.fillText("\"Boy, have I got some", 20, 140);
                        gc.fillText("   cool stuff for you!\"", 20, 160);
                        gc.fillText("\"Potions are 100 gold each.\"", 20, 190);
                        gc.fillText("\"Tiger claws are 4000 gold.", 20, 220);
                        gc.fillText("   They do way more damage than normal claws.\"", 20, 240);

                        gc.strokeRoundRect(10, 310, 480, 180, 20, 20);

                        if(bm.p.gold > 3999) {
                            gc.fillText("Press Z:", 30, 330);
                            gc.fillText("   to buy Tiger Claws", 30, 350);
                            gc.fillText("Press X:", 30, 380);
                            gc.fillText("   to buy a potion", 30, 400);
                            gc.fillText("Press C:", 30, 420);
                            gc.fillText("   to return to town", 30, 450);
                        }
                        else if(bm.p.gold > 99) {
                            gc.fillText("You don't have enough", 30, 330);
                            gc.fillText("   money to buy the claws.", 30, 350);
                            gc.fillText("   You still need", 30, 370);
                            gc.fillText("   " + (4000 - bm.p.gold) + " more gold.", 30, 390);
                            gc.fillText("Press Z:", 270, 330);
                            gc.fillText("   to buy 1 potion", 270, 350);
                            if(bm.p.gold > 499) {
                                gc.fillText("Press X:", 270, 380);
                                gc.fillText("   to buy 5 potions", 270, 400);
                                gc.fillText("Press C:", 270, 430);
                                gc.fillText("   to return to town", 270, 450);
                            }
                            else{
                                gc.fillText("Press X:", 30, 430);
                                gc.fillText("   to return to town", 30, 450);
                            }
                        }
                        else {
                            gc.fillText("You don't have enough", 30, 330);
                            gc.fillText("   money to buy anything.", 30, 350);
                            gc.fillText("   You still need", 30, 370);
                            gc.fillText("   " + (100 - bm.p.gold) + " more gold", 30, 390);
                            gc.fillText("   for just 1 potion", 30, 410);
                            gc.fillText("Press X:", 270, 330);
                            gc.fillText("   to return to town", 270, 350);
                        }
                }
                break;
            case 5: //title screen
                if(!bm.songPlaying) {
                    bm.playIntroSong();
                }
                gc.setFont(bigIntro);
                gc.setFill(Color.BLACK);

                gc.fillRect(0, 0, WIDTH, HEIGHT); // clear buffer
                gc.rect(WIDTH - 20, 140, WIDTH - 20, HEIGHT - 20);

                gc.drawImage(new Image("gargamel.png"), 300, 100, 150, 150);

                gc.setFill(Color.GOLDENROD);
                gc.setStroke(Color.DARKGOLDENROD);

                gc.fillText("gRPG", 45, 60);
                gc.setFont(fightFont);
                gc.fillText("a garfield RPG by alex kosla", 75, 90);

                gc.setFont(fightFont);
                gc.fillText("Press ESC:", 45, 150);
                gc.fillText("     to exit the game", 45, 170);
                gc.fillText("Press Z:", 45, 200);
                gc.fillText("     to start a new game", 45, 220);
                gc.fillText("Press X:", 45, 250);
                gc.fillText("     to load an old game", 45, 270);
                break;
            case 6: //new game
                gc.setFont(console); // set font in game
                gc.setFill(Color.BLACK);

                gc.fillRect(0, 0, WIDTH, HEIGHT); // clear buffer
                gc.rect(WIDTH - 20, 140, WIDTH - 20, HEIGHT - 20);

                gc.drawImage(new Image("grarfileld.png"),0,0,500,280);

                gc.setFill(Color.ANTIQUEWHITE);
                gc.setStroke(Color.ANTIQUEWHITE);
                gc.strokeRoundRect(10, 290, 480, 200, 20, 20);
                gc.fillText("Garfield, Jon wasn't very happy you ate all of his food.", 20, 315);
                gc.fillText("He's kicked you out of the house until you can pay for all", 20, 340);
                gc.fillText("of the food you ate, paid in full. You'll have to go out into", 20, 365);
                gc.fillText("the wilds outside Muncie and get money by killing monsters.", 20, 390);
                gc.fillText("You can press ESC at any time in the wilds to open your menu", 20, 415);
                gc.fillText("to see your stats and to return back to town.", 20, 440);
                gc.fillText("Press Z to advance. Good luck!", 20, 465);
                break;

            case 7: //win game
                gc.setFont(big); // set font in game
                gc.setFill(Color.BLACK);

                gc.fillRect(0, 0, WIDTH, HEIGHT); // clear buffer
                gc.rect(WIDTH - 20, 140, WIDTH - 20, HEIGHT - 20);

                gc.drawImage(new Image("flyjon.jpg"),225,25,230,250);
                gc.drawImage(new Image("garfieldhappy.png"),50,125,150,150);

                gc.setFill(Color.GOLDENROD);
                gc.setStroke(Color.DARKGOLDENROD);
                gc.fillText("Congratulations Garfield!", 20, 340);
                gc.fillText("You paid off your debt!", 20, 380);
                gc.fillText("You can go back to", 20, 420);
                gc.fillText("lying around in bed all day!", 20, 460);
                gc.fillText("Press Z to return to the menu.", 20, 275);
                break;
        }

    }
    @Override
    public void start (Stage theStage){
        theStage.setTitle(appName);

        Group root = new Group();
        Scene theScene = new Scene(root);
        theStage.setScene(theScene);

        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        root.getChildren().add(canvas);

        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Initial setup
        ow.Overworld(225,225); //set up overworld so it can be in the mainLoop
        setHandlers(theScene);

        // Setup and start animation loop (Timeline)
        KeyFrame kf = new KeyFrame(Duration.millis(1000 / FPS), e -> {
            ow.update();
            render(gc);
        });
        Timeline mainLoop = new Timeline(kf);
        mainLoop.setCycleCount(Animation.INDEFINITE);
        mainLoop.play();

        theStage.show();
    }

    void consoleText(GraphicsContext gc, String[] lines){ //used to display messages in battle
        gc.setFont(fightFont);
        gc.fillText(lines[0], 15, HEIGHT - 160, 340); //line 1
        gc.fillText(lines[1], 15, HEIGHT - 130, 340); //line 2
        gc.fillText(lines[2], 15, HEIGHT - 100, 340); //line 3
        gc.fillText(lines[3], 15, HEIGHT - 70, 340); //line 4
        gc.fillText(lines[4], 15, HEIGHT - 40, 340); //line 5
    }


    void save(Player garfield) throws IOException { //saves the player's stats to a txt file
        BufferedWriter writer = new BufferedWriter(new FileWriter("src//playerstats.txt"));
        writer.write(String.valueOf(garfield.exp)); //exp
        writer.newLine();
        writer.write(String.valueOf(garfield.level)); //level
        writer.newLine();
        writer.write(String.valueOf(garfield.gold)); //gold
        writer.newLine();
        writer.write(String.valueOf(garfield.weaponDamage));
        writer.newLine();
        writer.write(String.valueOf(garfield.potionNum));
        writer.flush();
        writer.close();
    }

    void load() throws IOException { //reads the player's stats from the txt file and assigns them accordingly
        Scanner s = new Scanner(new File("src//playerstats.txt"));
        Player garfield = new Player();

        garfield.exp = s.nextInt();
        garfield.Player(s.nextInt());
        garfield.setGold(s.nextInt());
        int wd = s.nextInt();
        if(wd != 7){
            garfield.setWeapon("Claws", 3);
            garfield.setAttack();
        }
        else{
            garfield.setWeapon("Tiger Claws", 7);
            garfield.setAttack();
        }
        garfield.potionNum = s.nextInt();
        s.close();
        bm.p = garfield;
        bm.setPlayer(bm.p);
        bm.fighting = false;
        monsterInitialize(bm.p.level);
        state = 5;
        ow.Overworld(225, 225);
    }

    void setHandlers(Scene scene) {

        scene.setOnKeyPressed(e -> {
            KeyCode key = e.getCode();
            if(bm.fighting) {
                switch (key) {
                    case Z:
                        if (bm.m.speed < bm.p.speed) //if the monster is faster than you, it will attack first and you will defend
                            bm.state = 2; //attack
                        else
                            bm.state = 3; //defend
                        break;
                    case X:
                        bm.state = 4; //use potion
                        break;
                    case C:
                        bm.state = 5; //try running
                        break;
                    default:
                        break;
                }
            }
            else{
                if(state == 3){ //menu
                    switch(key){
                        case ESCAPE: //return to overworld
                            state = 1;
                            break;
                        case Z: //return to town
                            townState = 0;
                            state = 4;
                            break;
                        case X: //return to title screen
                            state = 5;
                            break;
                        default:
                            break;
                    }
                }
                else if(state == 4){ //in town
                    switch(townState) {
                        case 0: //Town Main Menu
                            switch (key) {
                                case Z:
                                    townState = 1; //Jon's House
                                    break;
                                case X:
                                    townState = 2; //Inn
                                    break;
                                case C:
                                    townState = 4; //shop
                                    break;
                                case V:
                                    townState = 0; //to the wilds
                                    ow.owp.newArea = true;
                                    state = 1;
                                    break;
                                case ESCAPE:
                                    state = 5; //title screen
                                    break;
                            }
                            break;
                        case 1: //Jon's house
                            if (bm.p.gold > 9999) {
                                switch (key) {
                                    case Z: //win
                                        state = 7;
                                        bm.mP.stop();
                                        bm.playIntroSong();
                                        break;
                                    case X:
                                        townState = 0;
                                        break;
                                }
                            } else {
                                switch (key) {
                                    case X:
                                        townState = 0;
                                        break;
                                }
                            }
                            break;
                        case 2: //the Inn
                            if (bm.p.gold > 149) {
                                switch (key) {
                                    case Z:
                                        townState = 3; //sleep
                                        bm.p.gold = (bm.p.gold - 150);
                                        break;
                                    case X:
                                        townState = 0;
                                        break;
                                }
                            } else {
                                switch (key) {
                                    case X:
                                        townState = 0;
                                        break;
                                }
                            }
                            break;
                        case 3: //sleep
                            bm.p.setCurrHP(bm.p.maxHP);
                            try {
                                save(bm.p);
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            switch(key){
                                case X:
                                    townState = 0;
                                    break;
                            }
                            break;
                        case 4: //nermal's shop
                            if (bm.p.gold > 3999) {
                                switch (key) {
                                    case Z: //tiger claws
                                        bm.p.setWeapon("Tiger Claws", 7);
                                        bm.p.setAttack();
                                        bm.p.gold = bm.p.gold - 4000;
                                        break;
                                    case X: //1 potion
                                        bm.p.addPotion();
                                        bm.p.gold = bm.p.gold - 100;
                                        break;
                                    case C:
                                        townState = 0;
                                        break;
                                }
                            } else if (bm.p.gold > 99) {
                                switch (key) {
                                    case Z: //1 potion
                                        bm.p.addPotion();
                                        bm.p.gold = bm.p.gold - 100;
                                        break;
                                    case X: //5 potion
                                        if (bm.p.gold > 499) {
                                            bm.p.addPotion();
                                            bm.p.addPotion();
                                            bm.p.addPotion();
                                            bm.p.addPotion();
                                            bm.p.addPotion();
                                            bm.p.gold = bm.p.gold - 500;
                                        } else {
                                            townState = 0;
                                        }
                                        break;
                                    case C: //exit
                                        if (bm.p.gold > 499) {
                                            townState = 0;
                                        }
                                        break;
                                }
                            }
                            else{
                                switch(key){
                                    case X:
                                        townState = 0;
                                        break;
                                }
                            }
                            break;
                    }
                }
                else if(state == 5){ //title screen
                    switch(key){
                        case ESCAPE:
                            System.exit(1);
                            break;
                        case Z: //new game
                            initialize();
                            state = 6;
                            break;
                        case X: //load game
                            try {
                                load();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            state = 1;
                            bm.mP.stop();
                            bm.songPlaying = false;
                            break;
                    }
                }
                else if(state == 6){ //introduction
                    switch(key){
                        case Z:
                            state = 1;
                            bm.mP.stop();
                            bm.songPlaying = false;
                            break;
                    }
                }
                else if(state == 7 || state == 2){
                    switch(key){
                        case Z:
                            state = 5;
                            bm.mP.stop();
                            bm.songPlaying = false;
                    }
                }
                else {
                    switch (key) { //used in the overworld
                        case W:
                            ow.owp.goUp = true;
                            ow.steps++;
                            break;
                        case A:
                            ow.owp.goLeft = true;
                            ow.steps++;
                            break;
                        case S:
                            ow.owp.goDown = true;
                            ow.steps++;
                            break;
                        case D:
                            ow.owp.goRight = true;
                            ow.steps++;
                            break;
                        case ESCAPE:
                            state = 3;
                            break;
                        default:
                            break;
                    }
                }
            }
        });
        scene.setOnKeyReleased(e -> {
            KeyCode key = e.getCode();
            if(bm.fighting) {}
            else{
                switch (key){ //you stop moving when you take your fingers off the keys
                    case W:
                        ow.owp.goUp = false;
                        break;
                    case A:
                        ow.owp.goLeft = false;
                        break;
                    case S:
                        ow.owp.goDown = false;
                        break;
                    case D:
                        ow.owp.goRight = false;
                        break;
                    default:
                        break;
                }
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}

