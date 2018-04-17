import javafx.geometry.BoundingBox;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.LinkedList;

/**
 * Created by Alex on 3/5/2017.
 */
public class WorldObject {
    Image i;
    BoundingBox b;
    double x, y;

    public WorldObject(double x1, double y1, Image i) {
        this.x = x1;
        this.y = y1;
        this.i = i;
    }

    public void render(GraphicsContext gc) {
        gc.drawImage(i, x, y);
    }

    public void render(GraphicsContext gc, boolean debug) //keeping this here in case you want to look at the bbs
    {
        render(gc);
        if (debug)
        {
            gc.setStroke(Color.BLACK);
            BoundingBox bb = getBoundingBox();
            gc.strokeRect(bb.getMinX(), bb.getMinY(), bb.getWidth()  , bb.getHeight());
        }
    }

    public BoundingBox getBoundingBox()
    {
        double width = this.i.getWidth();
        double height = this.i.getHeight();
        double xoff = (width*(1.0f - javaRPG.BBscale)/2.0f);
        double yoff = (height*(1.0f - javaRPG.BBscale)/2.0f);
        double bbw = (width*javaRPG.BBscale);
        double bbh = (height*javaRPG.BBscale);
        return new BoundingBox(x+xoff, y+yoff, bbw, bbh);
    }
}
