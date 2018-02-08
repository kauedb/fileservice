package de.exb.platform.cloud.fileservice.fileservice.application;

import de.exb.platform.cloud.fileservice.fileservice.domain.FileService;
import de.exb.platform.cloud.fileservice.fileservice.domain.FileServiceException;
import de.exb.platform.cloud.fileservice.fileservice.view.resource.DirectoryResource;
import de.exb.platform.cloud.fileservice.fileservice.view.resource.ItemResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.UUID;

@Named
public class FileApplicationServiceImpl implements FileApplicationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileApplicationServiceImpl.class);

    private final FileService fileService;

    @Inject
    public FileApplicationServiceImpl(FileService fileService) {
        this.fileService = fileService;
    }

    public ItemResource<DirectoryResource> getDirectories(){

        try {
            final List<URL> list = fileService.list(UUID.randomUUID().toString(), URI.create("/tmp").toURL());

        } catch (FileServiceException | MalformedURLException e) {
            LOGGER.error(e.getLocalizedMessage());
        }

        return null;
    }
}
