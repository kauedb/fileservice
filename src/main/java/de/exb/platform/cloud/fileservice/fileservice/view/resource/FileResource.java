package de.exb.platform.cloud.fileservice.fileservice.view.resource;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@NoArgsConstructor(force = true)
@AllArgsConstructor(staticName = "create")
public class FileResource {

    Long id;

    String name;

}
