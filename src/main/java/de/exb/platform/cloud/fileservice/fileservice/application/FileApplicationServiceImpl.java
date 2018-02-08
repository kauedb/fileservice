package de.exb.platform.cloud.fileservice.fileservice.application;

import de.exb.platform.cloud.fileservice.fileservice.domain.FileService;
import de.exb.platform.cloud.fileservice.fileservice.domain.FileServiceException;
import de.exb.platform.cloud.fileservice.fileservice.view.resource.DirectoryResource;
import de.exb.platform.cloud.fileservice.fileservice.view.resource.ItemResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

@Named
public class FileApplicationServiceImpl implements FileApplicationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileApplicationServiceImpl.class);

    private final FileService fileService;

    private final Map<Long, String> repository = new HashMap<Long, String>()
    {
        {
            put(1L, "/");
            put(2L, "/tmp/");
        }
    };


    @Inject
    public FileApplicationServiceImpl(FileService fileService) {
        this.fileService = fileService;
    }

    @Override
    public Optional<ItemResource<DirectoryResource>> getDirectories()
    {

        try {

            final String aSessionId = UUID.randomUUID().toString();

            final URL url = fileService.construct(aSessionId, "/");

            final List<URL> list = fileService.list(aSessionId, url);

            final List<DirectoryResource> directories = list.stream()
                    .map(e -> DirectoryResource.create(1L, e.getFile())).collect(Collectors.toList());

            if (directories.isEmpty())
            {
                return Optional.empty();
            }
            else
            {

                return Optional.of(ItemResource.<DirectoryResource>builder().items(directories).build());
            }

        }
        catch (FileServiceException e)
        {
            LOGGER.error(e.getLocalizedMessage());
            throw new ApplicationException(e);
        }

    }

    @Override
    public Optional<ItemResource<DirectoryResource>> getDirectories(Long id)
    {
        try
        {

            final String path = repository.get(id);

            final String aSessionId = UUID.randomUUID().toString();

            final URL url = fileService.construct(aSessionId, "/");

            final List<URL> list = fileService.list(aSessionId, url);

            final List<DirectoryResource> directories = list.stream().filter(e -> path.equalsIgnoreCase(e.getPath()))
                    .map(e -> DirectoryResource.create(id, e.getPath())).collect(Collectors.toList());

            if (directories.isEmpty())
            {
                return Optional.empty();
            }
            else
            {

                final DirectoryResource first = directories.iterator().next();

                final ItemResource<DirectoryResource> itemResource = ItemResource.<DirectoryResource>builder()
                        .item(first).build();

                return Optional.of(itemResource);
            }

        }
        catch (FileServiceException e)
        {
            LOGGER.error(e.getLocalizedMessage());
            throw new ApplicationException(e);
        }
    }

    @Override
    public Optional<ItemResource<DirectoryResource>> createDirectory(DirectoryResource directoryResource)
    {


        long newId = Collections.max(repository.keySet()) + 1;

        final String path = repository.put(newId, directoryResource.getPath());

        if (path == null)
        {
            return Optional.empty();
        }

        final String aSessionId = UUID.randomUUID().toString();

        final String newPath = directoryResource.getPath();

        try
        {

            final URL urlToCreate = fileService.construct(aSessionId, newPath);

            fileService.createNewFile(aSessionId, urlToCreate);

            final ItemResource<DirectoryResource> itemResource = ItemResource.<DirectoryResource>builder()
                    .item(DirectoryResource.create(newId, newPath)).build();

            return Optional.of(itemResource);

        }
        catch (FileServiceException e)
        {
            LOGGER.error(e.getLocalizedMessage());
            throw new ApplicationException(e);
        }

    }

    @Override
    public Optional<ItemResource<DirectoryResource>> updateDirectory(Long id, DirectoryResource directoryResource)
    {
        final String path = repository.get(id);

        if (path == null)
        {
            return Optional.empty();
        }

        final String aSessionId = UUID.randomUUID().toString();

        final String newPath = directoryResource.getPath();

        try
        {
            final URL urlToDelete = fileService.construct(aSessionId, path);

            fileService.delete(aSessionId, urlToDelete, false);

            final URL urlToCreate = fileService.construct(aSessionId, newPath);

            fileService.createNewFile(aSessionId, urlToCreate);

            final ItemResource<DirectoryResource> itemResource = ItemResource.<DirectoryResource>builder()
                    .item(DirectoryResource.create(id, newPath)).build();

            return Optional.of(itemResource);

        }
        catch (FileServiceException e)
        {
            LOGGER.error(e.getLocalizedMessage());
            throw new ApplicationException(e);
        }

    }

}
