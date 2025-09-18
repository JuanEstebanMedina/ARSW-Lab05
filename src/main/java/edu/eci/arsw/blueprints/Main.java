package edu.eci.arsw.blueprints;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import edu.eci.arsw.blueprints.services.BlueprintsServices;

import java.util.Set;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

    public static void main(String[] args) {
        try (ClassPathXmlApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml")) {

            BlueprintsServices blueprintsServices = ac.getBean(BlueprintsServices.class);

            Point[] pts1 = { new Point(0, 0), new Point(10, 10), new Point(20, 20) };
            Blueprint bp1 = new Blueprint("john", "house_blueprint", pts1);
            Point[] pts2 = { new Point(50, 50), new Point(100, 100) };
            Blueprint bp2 = new Blueprint("john", "office_blueprint", pts2);
            Blueprint bp3 = new Blueprint("mary", "kitchen_blueprint",
                    new Point[] { new Point(0, 0), new Point(10, 0), new Point(10, 10), new Point(0, 10) });
            Point[] pts3 = { new Point(0, 0), new Point(0, 0), new Point(0, 0), new Point(10, 10), new Point(20, 20) };
            Blueprint bp4 = new Blueprint("maria", "home_blueprint", pts3);

            try {
                blueprintsServices.addNewBlueprint(bp1);
                blueprintsServices.addNewBlueprint(bp2);
                blueprintsServices.addNewBlueprint(bp3);
                blueprintsServices.addNewBlueprint(bp4);
                System.out.println(" Registrados: john/{house,office}, mary/kitchen");
            } catch (BlueprintPersistenceException e) {
                System.err.println("Error registrando: " + e.getMessage());
            }

            try {
                System.out.println("\n Plano específico john/house_blueprint:");
                System.out.println(blueprintsServices.getBlueprint("john", "house_blueprint"));
            } catch (BlueprintNotFoundException e) {
                System.err.println("No encontrado john/house_blueprint: " + e.getMessage());
            }

            try {
                System.out.println("\n Planos del autor 'john':");
                System.out.println(blueprintsServices.getBlueprintsByAuthor("john"));
            } catch (BlueprintNotFoundException e) {
                System.err.println("john sin planos: " + e.getMessage());
            }

            Set<Blueprint> all = blueprintsServices.getAllBlueprints();
            System.out.println("\n Todos los planos (" + all.size() + "):");
            System.out.println(all);

            try {
                blueprintsServices.addNewBlueprint(
                        new Blueprint("john", "house_blueprint", new Point[] { new Point(999, 999) }));
                System.err.println(" Debería lanzarse excepción por duplicado");
            } catch (BlueprintPersistenceException expected) {
                System.out.println("\n Excepción esperada por duplicado: " + expected.getMessage());
            }

            try {
                blueprintsServices.getBlueprint("ghost", "missing");
                System.err.println(" Debería lanzarse excepción por no encontrado");
            } catch (BlueprintNotFoundException expected) {
                System.out.println(" Excepción esperada por no encontrado: " + expected.getMessage());
            }

            try {
                Set<Blueprint> filtered = blueprintsServices.getBlueprintsByAuthor("maria");
                for (Blueprint bp : filtered) {
                    System.out.println(" Puntos de maria filtrados: " + bp.getPoints());
                }
                System.err.println(" Planos de maria: " + filtered);
            } catch (BlueprintNotFoundException e) {
            System.out.println(" No esperado, Blueprint no encontrado " + e.getMessage());
            }

        }
    }
}