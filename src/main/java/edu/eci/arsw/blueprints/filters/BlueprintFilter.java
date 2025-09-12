package edu.eci.arsw.blueprints.filters;

import edu.eci.arsw.blueprints.model.Blueprint;

public interface BlueprintFilter {

    /**
     * Applies the filter to the original blueprint and returns a new, filtered
     * blueprint.
     * 
     * @param original the original blueprint
     * @return a new blueprint, resulting from applying the filter to the
     *         original one.
     */
    public Blueprint apply(Blueprint original);
}
