package edu.eci.arsw.blueprints.filters;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("redundancyFilter")
public class RedundancyFilter implements BlueprintFilter {

    @Override
    public Blueprint apply(Blueprint original) {
        List<Point> src = original.getPoints();
        if (src == null || src.size() <= 1) {
            return new Blueprint(original.getAuthor(), original.getName(),
                    src == null ? new Point[0] : src.toArray(new Point[0]));
        }

        List<Point> result = new ArrayList<>(src.size());
        Point prev = null;
        for (Point p : src) {
            if (prev == null || !(prev.getX() == p.getX() && prev.getY() == p.getY())) {
                result.add(p);
            }
            prev = p;
        }
        return new Blueprint(original.getAuthor(), original.getName(), result.toArray(new Point[0]));
    }
}
