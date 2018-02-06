package de.exb.platform.cloud.fileservice.fileservice.view.resource;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.hateoas.ResourceSupport;

import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Value(staticConstructor = "create")
@Builder
public class ItemResource<T> extends ResourceSupport {

    T item;

    List<T> items;

}
