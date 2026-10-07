package ca.jrvs.insurance_api.controller;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ca.jrvs.insurance_api.model.Person;
import ca.jrvs.insurance_api.service.PersonService;

@RestController
@RequestMapping("/api") // the handout's sample says /insurance_api but its test steps use /api
public class PersonController {

    private final PersonService service;

    public PersonController(PersonService service) {
        this.service = service;
    }

    // ---------- Create ----------

    @PostMapping("person")
    @ResponseStatus(HttpStatus.CREATED)
    public void postPerson(@RequestBody Person person) {
        service.save(person);
    }

    @PostMapping("people")
    @ResponseStatus(HttpStatus.CREATED)
    public void postPeople(@RequestBody List<Person> people) {
        service.saveAll(people);
    }

    // ---------- Read ----------

    @GetMapping("people")
    public List<Person> getPeople() {
        return service.findAll();
    }

    @GetMapping("person/{id}")
    public ResponseEntity<Person> getPerson(@PathVariable ObjectId id) {
        Optional<Person> o = service.findOne(id);
        if (o.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(o.get());
    }

    // GET /api/people/id1,id2,id3
    @GetMapping("people/{ids}")
    public List<Person> getPeople(@PathVariable List<ObjectId> ids) {
        return service.findAll(ids);
    }

    // ---------- Update ----------

    @PutMapping("person")
    public ResponseEntity<Void> putPerson(@RequestBody Person person) {
        if (!service.update(person)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.noContent().build();
    }

    // Returns the number of people actually updated
    @PutMapping("people")
    public int putPeople(@RequestBody List<Person> people) {
        return service.update(people);
    }

    // ---------- Delete ----------

    @DeleteMapping("person/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePerson(@PathVariable ObjectId id) {
        service.delete(id);
    }

    // DELETE /api/people/id1,id2,id3
    @DeleteMapping("people/{ids}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePeople(@PathVariable List<ObjectId> ids) {
        service.delete(ids);
    }

    @DeleteMapping("people")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAllPeople() {
        service.deleteAll();
    }

    // ---------- Aggregations ----------

    @GetMapping("people/count")
    public long getCount() {
        return service.count();
    }

    @GetMapping("people/averageAge")
    public double getAverageAge() {
        return service.getAverageAge();
    }

    @GetMapping("people/maxCars")
    public int getMaxCars() {
        return service.getMaxCars();
    }
}