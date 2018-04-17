import javafx.geometry.BoundingBox;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.LinkedList;
import java.util.Random;

/**
 * Created by Alex on 3/5/2017.
 * Manages the Wilds.
 */
public class Overworld {
    int steps;
    int steplimit;
    PlayerWorldObject owp;
    double x1, y1;
    Random r;
    LinkedList<WorldObject> objects;

    void Overworld(double x1, double y1) {
        Image x = new Image("garfieldkiss1.png");
        this.owp = new PlayerWorldObject(x1, y1, x);
        objects = new LinkedList<>(); //stores the objects I'll add to the overworld
        objects.add(owp);

        this.r = new Random();
        steplimit = 40+r.nextInt(30);
        this.x1 = x1;
        this.y1 = y1;

        int numberofObjects = r.nextInt(5)+1; //1 or two more than this number of objects and the program may not be able to fit them all

        for(int i = 0; i < numberofObjects; i++) { //i is the object you are comparing to the others
            x1 = r.nextInt(350)+50; //generates coordinates for all the WorldObjects and determines if they overlap
            y1 = r.nextInt(350)+50;
            boolean goodcoords = false;
            while(!goodcoords){
                if(i==0){
                    if (x1 < 225 && (((y1 < 400) && (y1 > 300)) || (y1 < 225))) {
                        goodcoords = true;
                    } else if ((x1 > 300) && (y1 < 400) && (x1 < 450)) {
                        goodcoords = true;
                    } else if ((y1 > 300) && (y1 < 400) && ((x1 < 175) || ((x1 > 225) && (x1 < 400)))) {
                        goodcoords = true;
                    } else {
                        x1 = r.nextInt(350)+50;
                        y1 = r.nextInt(350)+50;
                    }
                }
                else {
                    for (int z = 0; z <= i; z++) { //z are the objects you're already gotten "good" coordinates for
                        BoundingBox bb = objects.get(z).getBoundingBox();
                        if ( (Math.abs(bb.getMinY() - y1) > 50) && (Math.abs(bb.getMinX() - x1) > 50) ){
                            if ((bb.getMinX() < x1 && bb.getMaxX() < x1) || (bb.getMaxX() > x1 + 50 && bb.getMinX() > x1)) {
                                if ((bb.getMinY() < y1 && bb.getMaxY() < y1) || (bb.getMaxY() > y1 + 50 && bb.getMinY() > y1)) {
                                    if (x1 < 225 && (((y1 < 400) && (y1 > 300)) || (y1 < 225))) {
                                        goodcoords = true;
                                    } else if ((x1 > 300) && (y1 < 400) && (x1 < 450)) {
                                        goodcoords = true;
                                    } else if ((y1 > 300) && (y1 < 400) && ((x1 < 175) || ((x1 > 225) && (x1 < 400)))) {
                                        goodcoords = true;
                                    } else {
                                        x1 = r.nextInt(350) + 50;
                                        y1 = r.nextInt(350) + 50;
                                        z = 0;
                                    }
                                }
                            }
                        }
                        else{
                            x1 = r.nextInt(350) + 50;
                            y1 = r.nextInt(350) + 50;
                            z = 0;
                        }
                    }
                }
            }
            this.x1 = x1;
            this.y1 = y1;
            int objectNumber = r.nextInt(4);
            int objectSize = r.nextInt(15);
            switch (objectNumber) {
                case 0:
                    objects.add(new WorldObject(x1, y1, new Image("rocksmall.png",objectSize+20,objectSize+20,true,true)));
                    break;
                case 1:
                    objects.add(new WorldObject(x1, y1, new Image("tree.png",objectSize+35,objectSize+35,true,true)));
                    break;
                case 2:
                    objects.add(new WorldObject(x1, y1, new Image("rock1.png",objectSize+30,objectSize+30,true,true)));
                    break;
                case 3:
                    objects.add(new WorldObject(x1, y1, new Image("tree1.gif",objectSize+35,objectSize+35,true,true)));
                    break;
                case 4:
                    objects.add(new WorldObject(x1, y1, new Image("rock3.png",objectSize+25,objectSize+25,true,true)));
                    break;
            }
        }
    }
    void update(){
        this.owp.move(objects);
    }

    void render(GraphicsContext gc) {
        gc.setFill(Color.SPRINGGREEN);
        gc.fillRect(0.0, 0.0, javaRPG.WIDTH, javaRPG.HEIGHT);
        for(int i = 0; i < objects.size(); i++){
            objects.get(i).render(gc);
        }
    }
}
