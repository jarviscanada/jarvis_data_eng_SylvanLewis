package ca.jrvs.insurance_api.service;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.data.mongodb.core.aggregation.ConditionalOperators;
import org.springframework.stereotype.Service;

import ca.jrvs.insurance_api.model.Person;
import ca.jrvs.insurance_api.repository.PersonRepository;

@Service
public class PersonService {

    private final PersonRepository repo;
    private final MongoTemplate mongo; // used for the aggregation pipelines

    // Single constructor = Spring injects automatically, no @Autowired needed
    public PersonService(PersonRepository repo, MongoTemplate mongo) {
        this.repo = repo;
        this.mongo = mongo;
    }

    // ---------- Create ----------

    public void save(Person person) {
        repo.save(person);
    }

    public void saveAll(List<Person> people) {
        repo.saveAll(people);
    }

    // ---------- Read ----------

    public Optional<Person> findOne(ObjectId id) {
        return repo.findById(id);
    }

    public List<Person> findAll(List<ObjectId> ids) {
        return repo.findAllById(ids);
    }

    public List<Person> findAll() {
        return repo.findAll();
    }

    // ---------- Delete ----------

    public void delete(ObjectId id) {
        repo.deleteById(id);
    }

    public void delete(List<ObjectId> ids) {
        repo.deleteAllById(ids);
    }

    public void deleteAll() {
        repo.deleteAll();
    }

    // ---------- Update ----------
    // Full replacement: the stored document becomes exactly what was sent.
    // To remove a car, send the person with that car left out of the "cars" list.
    // Returns false if the person does not exist (so we never create by accident).

    public boolean update(Person person) {
        if (person.getId() == null) {
            return false;
        }
        Optional<Person> existing = repo.findById(person.getId());
        if (existing.isEmpty()) {
            return false;
        }
        person.setCreatedAt(existing.get().getCreatedAt()); // keep the original creation date
        repo.save(person);
        return true;
    }

    /** Returns how many people were actually updated. */
    public int update(List<Person> people) {
        int updated = 0;
        for (Person p : people) {
            if (update(p)) {
                updated++;
            }
        }
        return updated;
    }

    // ---------- Aggregations ----------

    public long count() {
        return repo.count();
    }

    // db.people.aggregate([ { $group: { _id: null, averageAge: { $avg: "$age" } } } ])
    public double getAverageAge() {
        Aggregation agg = newAggregation(
                group().avg("age").as("averageAge"));

        Document result = mongo.aggregate(agg, Person.class, Document.class).getUniqueMappedResult();
        if (result == null || result.get("averageAge") == null) {
            return 0; // empty collection
        }
        return ((Number) result.get("averageAge")).doubleValue();
    }

    // db.people.aggregate([
    //   { $project: { carCount: { $size: { $ifNull: ["$cars", []] } } } },
    //   { $group:   { _id: null, maxCars: { $max: "$carCount" } } }
    // ])
    public int getMaxCars() {
        Aggregation agg = newAggregation(
                project().and(ArrayOperators.Size.lengthOfArray(
                        ConditionalOperators.ifNull("cars").then(Collections.emptyList()))).as("carCount"),
                group().max("carCount").as("maxCars"));

        Document result = mongo.aggregate(agg, Person.class, Document.class).getUniqueMappedResult();
        if (result == null || result.get("maxCars") == null) {
            return 0; // empty collection
        }
        return ((Number) result.get("maxCars")).intValue();
    }
}