package com.example.socialnetwork.Repository.Memory;


import com.example.socialnetwork.Domain.Entity;
import com.example.socialnetwork.Domain.Validator.ValidationException;
import com.example.socialnetwork.Domain.Validator.Validator;
import com.example.socialnetwork.Repository.Repository;

import java.util.HashMap;
import java.util.Map;

public class InMemoryRepository<ID, E extends Entity<ID>> implements Repository<ID,E> {

    private Validator<E> validator;
    Map<ID,E> entities;

    public InMemoryRepository(Validator<E> validator) {
        this.validator = validator;
        entities=new HashMap<ID,E>();
    }

    @Override
    public E findOne(ID id){
        if (id==null)
            throw new ValidationException("id must be not null");
        return entities.get(id);
    }

    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

    @Override
    public E save(E entity) {
        if(entity == null)
            throw new ValidationException("entity must not be null");

        validator.validate(entity);

        if(entities.get(entity.getId()) != null)
            return entity;

        entities.put(entity.getId(), entity);

        return null;
    }

    @Override
    public E delete(ID id) {
        if(id == null)
            throw new ValidationException("id must not be null");
        if(findOne(id) == null)
            return null;

        E ret = findOne(id);
        entities.remove(id);

        return ret;
    }

    @Override
    public E update(E entity) {

        if(entity == null)
            throw new IllegalArgumentException("entity must be not null!");
        validator.validate(entity);

        entities.put(entity.getId(),entity);

        if(entities.get(entity.getId()) != null) {
            entities.put(entity.getId(),entity);
            return null;
        }
        return entity;

    }

}