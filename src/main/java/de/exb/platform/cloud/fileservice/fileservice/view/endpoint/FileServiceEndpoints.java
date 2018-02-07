package de.exb.platform.cloud.fileservice.fileservice.view.endpoint;

import de.exb.platform.cloud.fileservice.fileservice.view.resource.DirectoryResource;
import de.exb.platform.cloud.fileservice.fileservice.view.resource.FileResource;
import de.exb.platform.cloud.fileservice.fileservice.view.resource.ItemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Collections;

@RestController
@RequestMapping("/rs/directories")
public class FileServiceEndpoints {

    @GetMapping
    public ResponseEntity<ItemResource<DirectoryResource>> get() {
        return ResponseEntity.ok().body(ItemResource.<DirectoryResource>builder()
                .items(Collections.singletonList(DirectoryResource.create(1L)))
                .build());
    }

    @GetMapping("{id}")
    public ResponseEntity<ItemResource<DirectoryResource>> get(@PathVariable("id") final Long id) {
        return ResponseEntity.ok().body(ItemResource.<DirectoryResource>builder()
                .item(DirectoryResource.create(1L))
                .build());
    }

    @PostMapping
    public ResponseEntity<ItemResource<DirectoryResource>> post(@RequestBody DirectoryResource resource)
    {
        return ResponseEntity.created(URI.create("/rs/directory"))
                .body(ItemResource.<DirectoryResource>builder()
                        .item(resource)
                        .build());
    }

    @PutMapping("{id}")
    public ResponseEntity<ItemResource<DirectoryResource>> put(@PathVariable("id") final Long id, @RequestBody DirectoryResource resource) {
        return ResponseEntity.ok()
                .body(ItemResource.<DirectoryResource>builder()
                        .item(resource)
                        .build());
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable("id") final Long id) {
        return ResponseEntity.noContent().build();
    }


    @GetMapping("{directoryId}/files")
    public ResponseEntity<ItemResource<FileResource>> getFiles(@PathVariable("directoryId") final Long id)
    {
        return ResponseEntity.ok().body(ItemResource.<FileResource>builder()
                .items(Collections.emptyList())
                .build());
    }

    @GetMapping("{directoryId}/files/{id}")
    public ResponseEntity<ItemResource<FileResource>> getFiles(@PathVariable("directoryId") final Long directoryId,
            @PathVariable("id") final Long id)
    {
        return ResponseEntity.ok()
                .body(ItemResource.<FileResource>builder()
                        .item(FileResource.create(id))
                        .build());
    }

    @PostMapping("{directoryId}/files")
    public ResponseEntity<ItemResource<FileResource>> postFile(@PathVariable("directoryId") final Long directoryId,
            FileResource resource)
    {
        return ResponseEntity.created(URI.create("/rs/directories"))
                .body(ItemResource.<FileResource>builder()
                        .item(resource)
                        .build());
    }

    @PutMapping("{directoryId}/files/{id}")
    public ResponseEntity<ItemResource<FileResource>> putFile(@PathVariable("directoryId") final Long directoryId,
            @PathVariable("id") final Long id, FileResource resource)
    {
        return ResponseEntity.ok()
                .body(ItemResource.<FileResource>builder()
                        .item(resource)
                        .build());
    }


    @DeleteMapping("{directoryId}/files/{id}")
    public ResponseEntity deleteFile(@PathVariable("directoryId") final Long directoryId, @PathVariable("id") final Long id) {
        return ResponseEntity.noContent().build();
    }

}
