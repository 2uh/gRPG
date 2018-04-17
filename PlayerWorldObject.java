import javafx.geometry.BoundingBox;
import javafx.scene.image.Image;

import java.util.LinkedList;

/**
 * Created by Alex on 3/6/2017.
 * Represents garfield in the overworld. In charge of his movement.
 */
public class PlayerWorldObject extends WorldObject {
    double speed;
    boolean goUp = false;
    boolean goDown = false;
    boolean goLeft = false;
    boolean goRight = false;
    boolean canGoLeft = true;
    boolean canGoRight = true;
    boolean canGoUp = true;
    boolean canGoDown = true;
    boolean newArea = false;

    public PlayerWorldObject(double x1, double y1, Image i) {
        super(x1, y1, i);
        this.speed = 5.0;
    }

    public void move(LinkedList<WorldObject> wo){
        //checks if there will be a collision with one of the WorldObjects, then determines if you can move
        int index;
        index = 1;
        WorldObject bb;
        while(index < wo.size()) {
            bb = wo.get(index);

            if ( !(this.y > 0 && (!stuckTop(wo)) && !(this.b.contains(bb.getBoundingBox()))) )
                canGoUp = false;

            if ( !(this.i.getHeight() + this.y < javaRPG.HEIGHT && (!stuckBottom(wo)) && !(this.b.contains(bb.getBoundingBox()))))
                canGoDown = false;

            if ( !(this.x > 0 && (!stuckLeft(wo)) && !(this.b.contains(bb.getBoundingBox()))))
                canGoLeft = false;

            if ( !(this.i.getWidth() + this.x < javaRPG.WIDTH && (!stuckRight(wo)) && !(this.b.contains(bb.getBoundingBox()))))
                canGoRight = false;
            index++;
        }

        //checking if I'm touching the edge of the screen and if I need to generate a new area

        if (goUp && this.y < 1) {
            this.y = 449;
            newArea = true;
        }

        if (goDown && this.y > 449) {
            this.y = 1;
            newArea = true;
        }

        if (goLeft && this.x < 1) {
            this.x = 449;
            newArea = true;
        }

        if (goRight && this.x > 449) {
            this.x = 1;
            newArea = true;
        }

        //checks if you want to go a direction and moves you in that direction if nothing is in your way

        if (goUp && canGoUp){
            y -= speed;
        }

        if (goDown && canGoDown) {
            y += speed;
        }

        if (goLeft && canGoLeft) {
            x -= speed;
        }

        if (goRight && canGoRight) {
            x += speed;
        }

        //resets the booleans to true so they can be re-checked next time the method is called

        canGoDown = true;
        canGoLeft = true;
        canGoRight = true;
        canGoUp = true;
    }

    public boolean stuckLeft(LinkedList<WorldObject> wo) {
        for (int i = 1; i < wo.size(); i++) {
            BoundingBox tree = wo.get(i).getBoundingBox();
            this.b = getBoundingBox();
            if (!((this.b.getMinY() > tree.getMaxY()) || (this.b.getMaxY() < tree.getMinY()))) {
                if ((this.b.getMaxX() >= tree.getMaxX()) && (this.b.getMinX() <= tree.getMaxX())) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean stuckRight(LinkedList<WorldObject> wo) {
        for (int i = 1; i < wo.size(); i++) {
            BoundingBox tree = wo.get(i).getBoundingBox();
            this.b = getBoundingBox();
            if (!((this.b.getMinY() > tree.getMaxY()) || (this.b.getMaxY() < tree.getMinY()))) {
                if ((this.b.getMinX() <= tree.getMinX()) && (this.b.getMaxX() >= tree.getMinX())) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean stuckBottom(LinkedList<WorldObject> wo) {
        for (int i = 1; i < wo.size(); i++) {
            BoundingBox tree = wo.get(i).getBoundingBox();
            this.b = getBoundingBox();
            if (!((this.b.getMinX() > tree.getMaxX()) || (this.b.getMaxX() < tree.getMinX()))) {
                if ((this.b.getMinY() <= tree.getMinY()) && (this.b.getMaxY() >= tree.getMinY())) {
                    return true;
                }
            }
        } return false;
    }

    public boolean stuckTop(LinkedList<WorldObject> wo) {
        for (int i = 1; i < wo.size(); i++) {
            BoundingBox tree = wo.get(i).getBoundingBox();
            this.b = getBoundingBox();
            if (!((this.b.getMinX() > tree.getMaxX()) || (this.b.getMaxX() < tree.getMinX()))) { //are you lined up with the object
                if ((this.b.getMaxY() >= tree.getMaxY()) && (this.b.getMinY() <= tree.getMaxY())) {
                    return true;
                }
            }
        } return false;
    }
}
