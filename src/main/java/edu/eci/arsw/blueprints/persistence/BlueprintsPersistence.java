package edu.eci.arsw.blueprints.persistence;

import java.util.Set;

import edu.eci.arsw.blueprints.model.Blueprint;

/**
 *
 * @author hcadavid
 */
public interface BlueprintsPersistence {

    /**
     * 
     * @param bp the new blueprint
     * @throws BlueprintPersistenceException if a blueprint with the same name
     *                                       already exists, or any other low-level
     *                                       persistence error occurs.
     */
    public void saveBlueprint(Blueprint bp) throws BlueprintPersistenceException;

    /**
     * @return All blueprints
     */
    Set<Blueprint> getAllBlueprints();

    /**
     * 
     * @param author     blueprint's author
     * @param bprintname blueprint's author
     * @return the blueprint of the given name and author
     * @throws BlueprintNotFoundException if there is no such blueprint
     */
    public Blueprint getBlueprint(String author, String bprintname) throws BlueprintNotFoundException;

    /**
     * 
     * @param author blueprint's author
     * @return the blueprints of the given author
     * @throws BlueprintNotFoundException if there is no such blueprint
     */
    public Set<Blueprint> getBlueprintsByAuthor(String author) throws BlueprintNotFoundException;
    
    public void updateBlueprint(String author, String bprintname, Blueprint updatedBlueprint) throws BlueprintNotFoundException, BlueprintPersistenceException;

}
