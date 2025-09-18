package edu.eci.arsw.blueprints.test.filters;

import edu.eci.arsw.blueprints.filters.SubsamplingFilter;
import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SubsamplingFilterTest {

    @Test
    public void shouldKeepEveryOtherPointStartingAtIndexZero() {
        Point[] pts = new Point[] {
                new Point(0, 0), // keep
                new Point(1, 1),
                new Point(2, 2), // keep
                new Point(3, 3),
                new Point(4, 4), // keep
                new Point(5, 5),
                new Point(6, 6) // keep
        };
        Blueprint original = new Blueprint("bob", "seq7", pts);

        SubsamplingFilter filter = new SubsamplingFilter();
        Blueprint filtered = filter.apply(original);

        assertEquals(4, filtered.getPoints().size());
        assertEquals(0, filtered.getPoints().get(0).getX());
        assertEquals(2, filtered.getPoints().get(1).getX());
        assertEquals(4, filtered.getPoints().get(2).getX());
        assertEquals(6, filtered.getPoints().get(3).getX());
    }

    @Test
    public void shouldReturnCopyWhenOneOrLessPoints() {
        SubsamplingFilter filter = new SubsamplingFilter();

        Blueprint empty = new Blueprint("a", "empty", new Point[] {});
        assertEquals(0, filter.apply(empty).getPoints().size());

        Blueprint one = new Blueprint("a", "one", new Point[] { new Point(1, 1) });
        assertEquals(1, filter.apply(one).getPoints().size());

        Blueprint two = new Blueprint("a", "two", new Point[] { new Point(1, 1), new Point(2, 2) });
        System.out.println("HEREEEEEEEEEEE");
        System.out.println(two.getPoints());
        System.out.println(filter.apply(two).getPoints());
        assertEquals(1, filter.apply(two).getPoints().size());
    }
}
