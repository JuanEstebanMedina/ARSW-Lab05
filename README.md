# ARSW-Lab05
## REST API for Blueprint Management

**Colombian School of Engineering Julio Garavito**  
**Software Architectures - ARSW**  
**Laboratory Number 5**

**Members:**
- Juan Esteban Medina Rivas
- María Paula Sánchez Macías

---

## Problem Context

In this exercise we built the BlueprintsRESTAPI component, which allows managing architectural blueprints for a prestigious design company. The idea of this API is to offer a standardized and 'platform-independent' medium so that tools developed in the future for the company can manage blueprints in a centralized way.

## Part I

1. **Base component integration**

> We integrated the Beans developed in the previous exercise into the provided base project. We copied only the classes, NOT the configuration files. We verified that the dependency injection scheme is correctly configured with @Service and @Autowired annotations.

The final project structure was as follows with all necessary classes:

<img src="img/partI-1.png" width="500">

2. **Persistence bean modification**

> We modified the 'InMemoryBlueprintPersistence' persistence bean so that by default it initializes with at least three other blueprints, and with two associated with the same author.

The InMemoryBlueprintPersistence constructor was configured this way, where it can be seen that the author with two blueprints is "mapu":

<img src="img/partI - 2.png" width="600">

The test data includes:
- **mapu**: "sala", "comedor" (2 blueprints from the same author)
- **juan**: "baño" (1 additional blueprint)
- **_authorname_**: "_bpname_" (original blueprint)

3. **Configuration of /blueprints GET resource**

> We configured our application to offer the "/blueprints" resource, so that when a GET request is made, it returns -in JSON format- the set of all blueprints.

We modified the BlueprintAPIController class implementing the shown pattern:

<img src="img/partI-3.1.png" width="600">

We injected the BlueprintsServices bean using @Autowired:

<img src="img/partI-3.2.png" width="600">

4. **Functionality verification**

> We verified the application's functionality by launching the application with maven and sending a GET request to http://localhost:8080/blueprints.

Successful compilation:

<img src="img/partI-4.0.png" width="600">

Application execution:

<img src="img/partI-4.1.png" width="600">

Endpoint response showing blueprints with applied filtering:

<img src="img/partI-4.2.png" width="300"> 
<img src="img/partI-4.3.png" width="300">

5. **Endpoint for blueprints by author**

> We modified the controller to accept GET requests to the /blueprints/{author} resource, which returns using a JSON representation all blueprints made by the author whose name is {author}.

Implementation of the endpoint with @PathVariable:

<img src="img/partI-5.1.png" width="600">

Verification with request to /blueprints/mapu (author with 2 blueprints):

<img src="img/partI-5.2.png" width="400">

6. **Endpoint for specific blueprint**

> We modified the controller to accept GET requests to the /blueprints/{author}/{bpname} resource, which returns using a JSON representation only ONE specific blueprint.

<img src="img/partI-6.1.png" width="600">

Verification of specific blueprint /blueprints/mapu/sala:

<img src="img/partI-6.2.png" width="400">

## Part II

7. **POST request handling**

> We added POST request handling (creation of new blueprints), so that an http client can register a new blueprint by making a POST request to the 'blueprints' resource, sending as request content all the resource details through a JSON document.

Implementation of the POST endpoint following the indicated pattern:

<img src="img/partII-1.1.png" width="600">

Results:

<img src="img/partII-1.2.png" width="300">

When the blueprint creation fails, it looks as follows:

<img src="img/post403.png" width="600">

8. **Tests with curl**

> We tested that the 'blueprints' resource accepts and correctly interprets POST requests using the curl command. We registered a new blueprint and verified that it can be obtained through a GET request.

Creation test with curl:

<img src="img/partII-2.1.png" width="600">

Verification of created blueprint:

<img src="img/partII-3.png" width="600">

9. **PUT verb support**

> We added support for the PUT verb for resources of the form '/blueprints/{author}/{bpname}', so that it is possible to update a specific blueprint.

For this we modified the following classes:

- InMemoryBlueprintPersistence

<img src="img/partII-4.1IMBP.png" width="600">

- BlueprintsPersistence

<img src="img/partII-4.2BP.png" width="800">

- BlueprintsServices

<img src="img/partII-4.3BS.png" width="800">

- BlueprintAPIController

<img src="img/partII-4.4BC.png" width="800">

The results show success:

<img src="img/partII-4.5.png" width="300">
<img src="img/partII-4.6.png" width="300">

<img src="img/partII-4.7Bash.png" width="500">

## Part III - Concurrency Analysis

### 10. Identification of concurrency problems

> We identified race conditions and critical regions in the API's concurrent environment.

**Identified problems:**
- HashMap not thread-safe
- Check-then-act operation not atomic in `saveBlueprint()`
- ConcurrentModificationException in iterations

### Solution implementation

> We implemented a hybrid strategy without significantly degrading performance.

**1. ConcurrentHashMap:**
```java
private final Map<Tuple<String, String>, Blueprint> blueprints = new ConcurrentHashMap<>();
```

**2. Atomic operation:**
```java
@Override
public void saveBlueprint(Blueprint bp) throws BlueprintPersistenceException {
    Tuple<String, String> key = new Tuple<>(bp.getAuthor(), bp.getName());
    Blueprint existing = blueprints.putIfAbsent(key, bp);
    if (existing != null) {
        throw new BlueprintPersistenceException("The given blueprint already exists: " + bp);
    }
}
```

**3. Selective synchronization:**
```java
@Override
public synchronized Set<Blueprint> getBlueprintsByAuthor(String author) 
        throws BlueprintNotFoundException {
}

@Override
public synchronized void updateBlueprint(String author, String bprintname, 
        Blueprint updatedBlueprint) throws BlueprintNotFoundException {
}
```

**4. Methods without synchronization:**
```java
@Override
public Set<Blueprint> getAllBlueprints() {
    return new HashSet<>(blueprints.values());
}

@Override
public Blueprint getBlueprint(String author, String bprintname) {
}
```

## Results

> The solution eliminates race conditions while maintaining high performance.

**Performance classification:**
- **Non-blocking**: `saveBlueprint()`, `getAllBlueprints()`, `getBlueprint()`
- **Minimal synchronization**: `getBlueprintsByAuthor()`, `updateBlueprint()`

**Non-blocking methods (maximum performance):**
- `getAllBlueprints()`
- `getBlueprint()`  
- `saveBlueprint()`

**Methods with minimal synchronization:**
- `getBlueprintsByAuthor()`
- `updateBlueprint()`

## Implemented Endpoints

| Method | Endpoint | Description | HTTP Codes |
|--------|----------|-------------|------------|
| GET | `/blueprints` | Get all blueprints | 200, 500 |
| GET | `/blueprints/{author}` | Get blueprints by author | 200, 404 |
| GET | `/blueprints/{author}/{bpname}` | Get specific blueprint | 200, 404 |
| POST | `/blueprints` | Create new blueprint | 201, 403 |
| PUT | `/blueprints/{author}/{bpname}` | Update blueprint | 200, 404 |

## Conclusions

1. **Successful implementation**: We managed to build a complete REST API that manages architectural blueprints with all required CRUD operations.

2. **Dependency injection**: We correctly applied dependency inversion principles using Spring annotations (@Service, @Autowired).

3. **Efficient concurrency handling**: We developed a solution that eliminates race conditions without significantly degrading performance, combining ConcurrentHashMap with selective synchronization.

4. **Best practices**: We followed SOLID principles, proper exception handling and appropriate HTTP status codes.