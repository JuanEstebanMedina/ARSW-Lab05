package edu.eci.arsw.blueprints.services;

import edu.eci.arsw.blueprints.filters.BlueprintFilter;
import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import edu.eci.arsw.blueprints.persistence.BlueprintsPersistence;

import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 *
 * @author hcadavid
 */
@Service
public class BlueprintsServices {

    private final BlueprintsPersistence bpp;
    private final BlueprintFilter bpf;

    @Autowired
    public BlueprintsServices(BlueprintsPersistence bpp, @Qualifier("subsamplingFilter") BlueprintFilter bpf) {
        this.bpp = bpp;
        this.bpf = bpf;
    }

    /**
     *
     * @param bp new blueprint
     * @throws BlueprintPersistenceException if a blueprint with the same name
     * already exists, or any other low-level persistence error occurs.
     */
    public void addNewBlueprint(Blueprint bp) throws BlueprintPersistenceException {
        bpp.saveBlueprint(bp);
    }

    /**
     *
     * @return all the blueprints
     */
    public Set<Blueprint> getAllBlueprints() {
        return bpp.getAllBlueprints();
    }

    /**
     *
     * @param author blueprint's author
     * @param name blueprint's name
     * @return the blueprint of the given name created by the given author
     * @throws BlueprintNotFoundException if there is no such blueprint
     */
    public Blueprint getBlueprint(String author, String name) throws BlueprintNotFoundException {
        Blueprint raw = bpp.getBlueprint(author, name);
        return bpf.apply(raw);
    }

    /**
     *
     * @param author blueprint's author
     * @return all the blueprints of the given author
     * @throws BlueprintNotFoundException if the given author doesn't exist
     */
    public Set<Blueprint> getBlueprintsByAuthor(String author) throws BlueprintNotFoundException {
        Set<Blueprint> raw = bpp.getBlueprintsByAuthor(author);
        Set<Blueprint> filtered = new HashSet<>(raw.size());
        for (Blueprint bp : raw) {
            filtered.add(bpf.apply(bp));
        }
        return filtered;
    }

    public void updateBlueprint(String author, String name, Blueprint updatedBlueprint) throws BlueprintNotFoundException, BlueprintPersistenceException {
        bpp.updateBlueprint(author, name, updatedBlueprint);
    }

}
