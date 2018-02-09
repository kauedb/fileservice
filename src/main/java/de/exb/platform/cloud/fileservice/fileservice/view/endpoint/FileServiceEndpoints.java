package de.exb.platform.cloud.fileservice.fileservice.view.endpoint;

import de.exb.platform.cloud.fileservice.fileservice.application.FileApplicationService;
import de.exb.platform.cloud.fileservice.fileservice.view.resource.DirectoryResource;
import de.exb.platform.cloud.fileservice.fileservice.view.resource.FileResource;
import de.exb.platform.cloud.fileservice.fileservice.view.resource.ItemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.util.Collections;
import java.util.Optional;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/rs/directories")
public class FileServiceEndpoints {

    private final FileApplicationService fileApplicationService;

    @Inject
    public FileServiceEndpoints(FileApplicationService fileApplicationService) {
        this.fileApplicationService = fileApplicationService;
    }

    @GetMapping
    public ResponseEntity<ItemResource<DirectoryResource>> get() {

        final Optional<ItemResource<DirectoryResource>> optional = fileApplicationService.getDirectories();

        if (optional.isPresent())
        {

            final ItemResource<DirectoryResource> itemResource = optional.get();

            itemResource.add(linkTo(methodOn(FileServiceEndpoints.class).get()).withSelfRel());

            return ResponseEntity.ok().body(itemResource);

        }
        else
        {

            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<ItemResource<DirectoryResource>> get(@PathVariable("id") final Long id) {

        final Optional<ItemResource<DirectoryResource>> optional = fileApplicationService.getDirectories(id);

        if (optional.isPresent())
        {

            final ItemResource<DirectoryResource> itemResource = optional.get();

            itemResource.add(linkTo(methodOn(FileServiceEndpoints.class).get(id)).withSelfRel());

            return ResponseEntity.ok().body(itemResource);

        }
        else
        {

            return ResponseEntity.notFound().build();
        }

    }

    @PostMapping
    public ResponseEntity<ItemResource<DirectoryResource>> post(@RequestBody DirectoryResource resource)
    {
        final Optional<ItemResource<DirectoryResource>> optional = fileApplicationService.createDirectory(resource);

        if (optional.isPresent())
        {

            final ItemResource<DirectoryResource> itemResource = optional.get();

            itemResource.add(linkTo(methodOn(FileServiceEndpoints.class).post(resource)).withSelfRel());

            return ResponseEntity.created(URI.create("/rs/directory")).body(itemResource);
        }
        else
        {
            return ResponseEntity.notFound().build();
        }

    }

    @PutMapping("{id}")
    public ResponseEntity<ItemResource<DirectoryResource>> put(@PathVariable("id") final Long id, @RequestBody DirectoryResource resource) {

        final Optional<ItemResource<DirectoryResource>> optional = fileApplicationService.updateDirectory(id, resource);

        if (optional.isPresent())
        {
            final ItemResource<DirectoryResource> itemResource = optional.get();

            itemResource.add(linkTo(methodOn(FileServiceEndpoints.class).put(id, resource)).withSelfRel());

            return ResponseEntity.ok().body(itemResource);

        }
        else
        {
            return ResponseEntity.notFound().build();
        }

    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable("id") final Long id) {

        final Optional<ItemResource<DirectoryResource>> optional = fileApplicationService.deleteDirectory(id);

        if (optional.isPresent())
        {
            return ResponseEntity.noContent().build();
        }
        else
        {
            return ResponseEntity.notFound().build();
        }

    }


    @GetMapping("{directoryId}/files")
    public ResponseEntity<ItemResource<FileResource>> getFiles(@PathVariable("directoryId") final Long id)
    {

        final ItemResource<FileResource> itemResource = ItemResource.<FileResource>builder()
                .items(Collections.singletonList(FileResource.create(1L, "hello.txt"))).build();

        itemResource.add(linkTo(methodOn(FileServiceEndpoints.class).getFiles(id)).withSelfRel());

        return ResponseEntity.ok().body(itemResource);
    }

    @GetMapping("{directoryId}/files/{id}")
    public ResponseEntity<ItemResource<FileResource>> getFiles(@PathVariable("directoryId") final Long directoryId,
            @PathVariable("id") final Long id)
    {
        final ItemResource<FileResource> itemResource = ItemResource.<FileResource>builder().item(FileResource.create(id, "hello.txt")).build();

        itemResource.add(linkTo(methodOn(FileServiceEndpoints.class).getFiles(directoryId, id)).withSelfRel());

        return ResponseEntity.ok().body(itemResource);
    }

    @PostMapping("{directoryId}/files")
    public ResponseEntity<ItemResource<FileResource>> postFile(@PathVariable("directoryId") final Long directoryId,
            @RequestBody FileResource resource)
    {
        final ItemResource<FileResource> itemResource = ItemResource.<FileResource>builder().item(resource).build();

        itemResource.add(linkTo(methodOn(FileServiceEndpoints.class).postFile(directoryId, resource)).withSelfRel());

        return ResponseEntity.created(URI.create("/rs/directories")).body(itemResource);
    }

    @PutMapping("{directoryId}/files/{id}")
    public ResponseEntity<ItemResource<FileResource>> putFile(@PathVariable("directoryId") final Long directoryId,
            @PathVariable("id") final Long id, @RequestBody FileResource resource)
    {
        final ItemResource<FileResource> itemResource = ItemResource.<FileResource>builder().item(resource).build();

        itemResource.add(linkTo(methodOn(FileServiceEndpoints.class).putFile(directoryId, id, resource)).withSelfRel());

        return ResponseEntity.ok().body(itemResource);
    }


    @DeleteMapping("{directoryId}/files/{id}")
    public ResponseEntity deleteFile(@PathVariable("directoryId") final Long directoryId, @PathVariable("id") final Long id) {
        return ResponseEntity.noContent().build();
    }

}
