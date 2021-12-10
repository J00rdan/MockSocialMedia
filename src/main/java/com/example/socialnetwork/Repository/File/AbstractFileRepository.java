package com.example.socialnetwork.Repository.File;


import com.example.socialnetwork.Domain.Entity;
import com.example.socialnetwork.Domain.Validator.Validator;
import com.example.socialnetwork.Repository.Memory.InMemoryRepository;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractFileRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID,E> {
    private final String fileName;

    public AbstractFileRepository(String fileName, Validator<E> validator) {
        super(validator);
        this.fileName = fileName;
        loadData();
    }

    private void loadData() {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while((line = br.readLine()) != null){
                List<String> attributes = Arrays.asList(line.split(";"));
                E entity = extractEntity(attributes);
                super.save(entity);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void writeToFile(E entity){
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(fileName,true))){
            bw.write(createEntityAsString(entity));
            bw.newLine();
        }catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    protected void rewriteToFile()
    {
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(fileName,false))){
            Iterable<E> entities = super.findAll();
            for (E entity: entities) {
                bw.write(createEntityAsString(entity));
                bw.newLine();
            }
        }catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     *  extract entity  - template method design pattern
     *  creates an entity of type E having a specified list of @code attributes
     * @param attributes
     * @return an entity of type E
     */
    public abstract E extractEntity(List<String> attributes);


    protected abstract String createEntityAsString(E entity);

    @Override
    public E save(E entity){
        E ent = super.save(entity);
        if (ent == null)
            writeToFile(entity);
        return ent;
    }

    @Override
    public E delete(ID id) {
        E ent = super.delete(id);
        if (ent != null){
            rewriteToFile();
        }
        return ent;
    }

    @Override
    public E update(E entity) {
        E ent = super.update(entity);
        if (ent == null){
            rewriteToFile();
        }
        return ent;
    }
}
