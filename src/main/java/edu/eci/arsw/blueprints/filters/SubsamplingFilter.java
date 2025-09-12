package edu.eci.arsw.blueprints.filters;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("subsamplingFilter")
public class SubsamplingFilter implements BlueprintFilter {

    @Override
    public Blueprint apply(Blueprint original) {
        List<Point> src = original.getPoints();
        if (src == null || src.size() <= 1) {
            return new Blueprint(original.getAuthor(), original.getName(),
                    src == null ? new Point[0] : src.toArray(new Point[0]));
        }

        List<Point> result = new ArrayList<>((src.size() + 1) / 2);
        for (int i = 0; i < src.size(); i += 2) {
            result.add(src.get(i));
        }
        return new Blueprint(original.getAuthor(), original.getName(), result.toArray(new Point[0]));
    }
}
