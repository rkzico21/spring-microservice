package userservice.integration;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.core.EmbeddedWrapper;
import org.springframework.hateoas.core.EmbeddedWrappers;

import java.util.Arrays;

public class ResourceWithEmbeddable<T> extends Resource<T> {

    @JsonUnwrapped
    private Resources<EmbeddedWrapper> wrappers;

    //required to match constructor
    public ResourceWithEmbeddable(final T content, final Link... links) {

        super(content, links);
    }

    //not required
    public ResourceWithEmbeddable(final T content, final Iterable<EmbeddedWrapper> wrappers, final Link... links) {

        super(content, links);
        this.wrappers = new Resources<>(wrappers);
    }

    //required to match constructor
    public ResourceWithEmbeddable(final T content, final Iterable<Link> links) {

        super(content, links);
    }

    //not required
    public ResourceWithEmbeddable(final T content, final Iterable<EmbeddedWrapper> wrappers, 
                                  final Iterable<Link> links) {

        super(content, links);
        this.wrappers = new Resources<>(wrappers);
    }

    public static <T> ResourceWithEmbeddable<T> embeddedRes(final T content,
                                                            final EmbeddedWrapper... wrappers ){

        return new ResourceWithEmbeddable<>(content, Arrays.asList(wrappers));

    }

    public static <T> ResourceWithEmbeddable<T> embeddedRes(final T content,
                                                            final Iterable<EmbeddedWrapper> wrappers ){

        return new ResourceWithEmbeddable<>(content, wrappers);

    }

    public static EmbeddedWrapper resWrapper(final Object source, final String rel) {
        return new EmbeddedWrappers(false).wrap(source, rel);
    }

    public static EmbeddedWrapper resWrapper(final Object source) {
        return new EmbeddedWrappers(false).wrap(source);
    }
}