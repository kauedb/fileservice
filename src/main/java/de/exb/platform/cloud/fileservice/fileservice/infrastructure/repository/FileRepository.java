package de.exb.platform.cloud.fileservice.fileservice.infrastructure.repository;

import javax.inject.Named;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Named
public class FileRepository
{
    private final Map<Long, String> database = new HashMap<>();

    public FileRepository()
    {
        database.put(1L, "/");
        database.put(2L, "/tmp/");
    }

    public String find(Long id) throws RepositoryNotFoundException
    {
        final String path = database.get(id);

        if (path == null)
        {
            throw new RepositoryNotFoundException();
        }

        return path;
    }

    public long create(String newPath)
    {

        long newId = Collections.max(database.keySet()) + 1;

        database.put(newId, newPath);

        return newId;
    }

    public void delete(long id)
    {
        database.remove(id);
    }

    public Map<Long, String> findAll()
    {
        return database;
    }

    public Long findIdByPath(String path){
        return null;
    }
}
