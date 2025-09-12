package edu.eci.arsw.blueprints.test.persistence.impl;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import edu.eci.arsw.blueprints.persistence.impl.InMemoryBlueprintPersistence;

import java.util.Set;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author hcadavid
 */
public class InMemoryPersistenceTest {

    @Test
    public void saveNewAndLoadTest() throws BlueprintPersistenceException, BlueprintNotFoundException {
        InMemoryBlueprintPersistence ibpp = new InMemoryBlueprintPersistence();

        Point[] pts0 = new Point[] { new Point(40, 40), new Point(15, 15) };
        Blueprint bp0 = new Blueprint("mack", "mypaint", pts0);

        ibpp.saveBlueprint(bp0);

        Point[] pts = new Point[] { new Point(0, 0), new Point(10, 10) };
        Blueprint bp = new Blueprint("john", "thepaint", pts);

        ibpp.saveBlueprint(bp);

        assertNotNull("Loading a previously stored blueprint returned null.",
                ibpp.getBlueprint(bp.getAuthor(), bp.getName()));

        assertEquals("Loading a previously stored blueprint returned a different blueprint.",
                ibpp.getBlueprint(bp.getAuthor(), bp.getName()), bp);
    }

    @Test
    public void saveExistingBpTest() {
        InMemoryBlueprintPersistence ibpp = new InMemoryBlueprintPersistence();

        Point[] pts = new Point[] { new Point(0, 0), new Point(10, 10) };
        Blueprint bp = new Blueprint("john", "thepaint", pts);

        try {
            ibpp.saveBlueprint(bp);
        } catch (BlueprintPersistenceException ex) {
            fail("Blueprint persistence failed inserting the first blueprint.");
        }

        Point[] pts2 = new Point[] { new Point(10, 10), new Point(20, 20) };
        Blueprint bp2 = new Blueprint("john", "thepaint", pts2);

        try {
            ibpp.saveBlueprint(bp2);
            fail("An exception was expected after saving a second blueprint with the same name and autor");
        } catch (BlueprintPersistenceException ex) {
            // ok, the exception was thrown as expected
        }

    }

    @Test(expected = BlueprintNotFoundException.class)
    public void getBlueprintShouldThrowWhenNotFound() throws BlueprintNotFoundException {
        InMemoryBlueprintPersistence ibpp = new InMemoryBlueprintPersistence();
        ibpp.getBlueprint("no-one", "nothing");
    }

    @Test
    public void getBlueprintsByAuthorShouldReturnOnlyAuthorsBlueprints() throws Exception {
        InMemoryBlueprintPersistence ibpp = new InMemoryBlueprintPersistence();

        ibpp.saveBlueprint(new Blueprint("alice", "bp-1", new Point[] { new Point(1, 1) }));
        ibpp.saveBlueprint(new Blueprint("alice", "bp-2", new Point[] { new Point(2, 2) }));

        ibpp.saveBlueprint(new Blueprint("bob", "bp-x", new Point[] { new Point(9, 9) }));

        Set<Blueprint> aliceBps = ibpp.getBlueprintsByAuthor("alice");
        assertEquals("Expected exactly 2 blueprints for author 'alice'", 2, aliceBps.size());
        assertTrue(aliceBps.stream().allMatch(bp -> "alice".equals(bp.getAuthor())));
    }

    @Test(expected = BlueprintNotFoundException.class)
    public void getBlueprintsByAuthorShouldThrowWhenAuthorNotFound() throws Exception {
        InMemoryBlueprintPersistence ibpp = new InMemoryBlueprintPersistence();
        ibpp.getBlueprintsByAuthor("ghost-author");
    }

    @Test
    public void getAllBlueprintsShouldContainSavedOnes() throws Exception {
        InMemoryBlueprintPersistence ibpp = new InMemoryBlueprintPersistence();

        Blueprint a = new Blueprint("anna", "home", new Point[] { new Point(0, 0) });
        Blueprint b = new Blueprint("mike", "office", new Point[] { new Point(5, 5) });

        ibpp.saveBlueprint(a);
        ibpp.saveBlueprint(b);

        Set<Blueprint> all = ibpp.getAllBlueprints();
        assertTrue("All blueprints should contain 'anna/home'", all.contains(a));
        assertTrue("All blueprints should contain 'mike/office'", all.contains(b));
    }

    @Test
    public void getAllBlueprintsShouldReturnACopy() throws Exception {
        InMemoryBlueprintPersistence ibpp = new InMemoryBlueprintPersistence();

        Blueprint a = new Blueprint("charlie", "bp", new Point[] { new Point(3, 3) });
        ibpp.saveBlueprint(a);

        Set<Blueprint> snapshot = ibpp.getAllBlueprints();
        int originalSize = snapshot.size();

        snapshot.add(new Blueprint("hacker", "intruder", new Point[] { new Point(99, 99) }));

        Set<Blueprint> after = ibpp.getAllBlueprints();
        assertEquals("Underlying storage should not be affected by external Set modifications.",
                originalSize, after.size());

        try {
            ibpp.getBlueprint("hacker", "intruder");
            fail("No blueprint 'hacker/intruder' should exist in the persistence");
        } catch (BlueprintNotFoundException expected) {
            // ok, the exception was thrown as expected
        }
    }
}
