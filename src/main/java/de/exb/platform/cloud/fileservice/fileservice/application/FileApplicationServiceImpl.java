package de.exb.platform.cloud.fileservice.fileservice.application;

import de.exb.platform.cloud.fileservice.fileservice.domain.FileService;
import de.exb.platform.cloud.fileservice.fileservice.domain.FileServiceException;
import de.exb.platform.cloud.fileservice.fileservice.infrastructure.repository.FileRepository;
import de.exb.platform.cloud.fileservice.fileservice.infrastructure.repository.RepositoryNotFoundException;
import de.exb.platform.cloud.fileservice.fileservice.view.resource.DirectoryResource;
import de.exb.platform.cloud.fileservice.fileservice.view.resource.ItemResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.inject.Inject;
import javax.inject.Named;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Named
public class FileApplicationServiceImpl implements FileApplicationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileApplicationServiceImpl.class);

    private final FileService fileService;

    private final FileRepository fileRepository;

    private final String rootDir;

    @Inject
    public FileApplicationServiceImpl(FileService fileService, FileRepository fileRepository,
            @Value("${directory.server.rootDir}") String rootDir)
    {
        this.fileService = fileService;
        this.fileRepository = fileRepository;
        this.rootDir = rootDir;
    }

    @Override
    public Optional<ItemResource<DirectoryResource>> getDirectories()
    {
        final String aSessionId = UUID.randomUUID().toString();

        try {

            final URL url = fileService.construct(aSessionId, rootDir);

            final List<URL> list = fileService.list(aSessionId, url);

            final Map<Long, String> all = fileRepository.findAll();

            final List<DirectoryResource> directories = list.stream()
                    .map(e -> {
                        return DirectoryResource.create(1L, e.getPath());
                    })
                    .collect(Collectors.toList());

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
        final String aSessionId = UUID.randomUUID().toString();

        try
        {

            final String path = fileRepository.find(id);

            final URL url = fileService.construct(aSessionId, rootDir);

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
        catch (FileServiceException | RepositoryNotFoundException e)
        {
            LOGGER.warn(e.getLocalizedMessage());
            throw new ApplicationException(e);
        }
    }

    @Override
    public Optional<ItemResource<DirectoryResource>> createDirectory(DirectoryResource directoryResource)
    {

        final String aSessionId = UUID.randomUUID().toString();

        final String newPath = directoryResource.getPath();

        try
        {
            final URL url = fileService.construct(aSessionId, newPath);

            fileService.createNewFile(aSessionId, url);

            long newId = fileRepository.create(newPath);

            final ItemResource<DirectoryResource> itemResource = ItemResource.<DirectoryResource>builder()
                    .item(DirectoryResource.create(newId, newPath)).build();

            return Optional.of(itemResource);

        }
        catch (FileServiceException e)
        {
            LOGGER.warn(e.getLocalizedMessage());
            throw new ApplicationException(e);
        }

    }

    @Override
    public Optional<ItemResource<DirectoryResource>> updateDirectory(Long id, DirectoryResource directoryResource)
    {

        deleteDirectory(id);

        return createDirectory(directoryResource);
    }

    @Override
    public Optional<ItemResource<DirectoryResource>> deleteDirectory(Long id)
    {

        final String aSessionId = UUID.randomUUID().toString();

        try
        {
            final String path = fileRepository.find(id);

            final URL url = fileService.construct(aSessionId, path);

            fileService.delete(aSessionId, url, false);

            fileRepository.delete(id);

            return Optional.empty();
        }
        catch (FileServiceException | RepositoryNotFoundException e)
        {
            LOGGER.warn(e.getLocalizedMessage());
            throw new ApplicationException(e);
        }

    }

}
