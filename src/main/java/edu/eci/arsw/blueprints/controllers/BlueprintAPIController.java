/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blueprints.controllers;

import java.util.LinkedHashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import edu.eci.arsw.blueprints.services.BlueprintsServices;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

/**
 *
 * @author hcadavid
 */
@RestController
@RequestMapping(value = "/blueprints")
public class BlueprintAPIController {

    @Autowired
    private BlueprintsServices blueprintServices;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getAllBlueprints() {
        try {
            //obtener datos que se enviarán a través del API
            Set<Blueprint> data = blueprintServices.getAllBlueprints();
            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (Exception ex) {
            Logger.getLogger(BlueprintAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>("Error, no se pudieron obtener los planos", HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/{author}", method = RequestMethod.GET)
    public ResponseEntity<?> getBlueprintsByAuthor(@PathVariable String author) {
        try {
            //obtener datos que se enviarán a través del API
            Set<Blueprint> data = blueprintServices.getBlueprintsByAuthor(author);
            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (BlueprintNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/{author}/{bpname}", method = RequestMethod.GET)
    public ResponseEntity<?> getBlueprint(@PathVariable String author, @PathVariable String bpname) {
        try {
            //obtener datos que se enviarán a través del API
            Blueprint data = blueprintServices.getBlueprint(author, bpname);

            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (BlueprintNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> addNewBlueprint(@RequestBody Blueprint bp) {
        try {
            //obtener datos que se enviarán a través del API
            blueprintServices.addNewBlueprint(bp);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (BlueprintPersistenceException ex) {
            Logger.getLogger(BlueprintAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>("No se pudo crear el plano: " + ex.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(value = "/{author}/{bpname}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateBlueprint(@PathVariable String author, @PathVariable String bpname, @RequestBody Blueprint blueprint) {
        try {
            //obtener datos que se enviarán a través del API
            blueprintServices.updateBlueprint(author, bpname, blueprint);
            return new ResponseEntity<>("El plano: "+bpname+"se actualizó correctamnete", HttpStatus.OK);
        } catch (BlueprintNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (BlueprintPersistenceException  ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

}
