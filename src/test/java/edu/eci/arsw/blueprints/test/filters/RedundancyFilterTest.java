package edu.eci.arsw.blueprints.test.filters;

import edu.eci.arsw.blueprints.filters.RedundancyFilter;
import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RedundancyFilterTest {

    @Test
    public void shouldRemoveOnlyConsecutiveDuplicates() {
        Point[] pts = new Point[] {
                new Point(0,0), new Point(0,0),
                new Point(10,10), new Point(10,10), new Point(10,10),
                new Point(20,20),
                new Point(30,30), new Point(30,30),
                new Point(0,0)
        };
        Blueprint original = new Blueprint("alice", "redundant", pts);

        RedundancyFilter filter = new RedundancyFilter();
        Blueprint filtered = filter.apply(original);

        assertEquals(5, filtered.getPoints().size());

        assertEquals(0, filtered.getPoints().get(0).getX());
        assertEquals(0, filtered.getPoints().get(0).getY());

        assertEquals(10, filtered.getPoints().get(1).getX());
        assertEquals(10, filtered.getPoints().get(1).getY());

        assertEquals(20, filtered.getPoints().get(2).getX());
        assertEquals(20, filtered.getPoints().get(2).getY());

        assertEquals(30, filtered.getPoints().get(3).getX());
        assertEquals(30, filtered.getPoints().get(3).getY());

        assertEquals(0, filtered.getPoints().get(4).getX());
        assertEquals(0, filtered.getPoints().get(4).getY());
    }

    @Test
    public void shouldReturnCopyWhenZeroOrOnePoint() {
        RedundancyFilter filter = new RedundancyFilter();

        Blueprint empty = new Blueprint("a","empty", new Point[]{});
        Blueprint filteredEmpty = filter.apply(empty);
        assertEquals(0, filteredEmpty.getPoints().size());

        Blueprint single = new Blueprint("a","single", new Point[]{ new Point(1,1) });
        Blueprint filteredSingle = filter.apply(single);
        assertEquals(1, filteredSingle.getPoints().size());
    }
}
