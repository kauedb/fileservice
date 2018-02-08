package de.exb.platform.cloud.fileservice.fileservice.application;

import de.exb.platform.cloud.fileservice.fileservice.view.resource.DirectoryResource;
import de.exb.platform.cloud.fileservice.fileservice.view.resource.ItemResource;

import java.util.Optional;

public interface FileApplicationService {

    Optional<ItemResource<DirectoryResource>> getDirectories();

    Optional<ItemResource<DirectoryResource>> getDirectories(Long id);

    Optional<ItemResource<DirectoryResource>> createDirectory(DirectoryResource directoryResource);

    Optional<ItemResource<DirectoryResource>> updateDirectory(Long id, DirectoryResource directoryResource);
}
