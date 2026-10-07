package ca.jrvs.insurance_api.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
//import org.springframework.stereotype.Repository;

import ca.jrvs.insurance_api.model.Person;

//@Repository
public interface PersonRepository extends MongoRepository<Person, ObjectId> {}