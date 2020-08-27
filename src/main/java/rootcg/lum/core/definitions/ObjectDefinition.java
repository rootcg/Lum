package rootcg.lum.core.definitions;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static rootcg.lum.util.Validations.Arguments.checkNonNull;

public class ObjectDefinition {

    public static class Builder {

        private String definition;
        private String name;
        private List<AttributeDefinition> attributes;
        private List<MethodDefinition> methods;

        private Builder() {

        }

        public Builder withDefinition(String definition) {
            this.definition = definition;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withAttributes(List<AttributeDefinition> attributes) {
            this.attributes = attributes;
            return this;
        }

        public Builder withMethods(List<MethodDefinition> methods) {
            this.methods = methods;
            return this;
        }

        public ObjectDefinition build() {
            return new ObjectDefinition(this);
        }

    }

    private final Optional<String> definition;
    private final String name;
    private final List<AttributeDefinition> attributes;
    private final List<MethodDefinition> methods;

    public ObjectDefinition(Builder builder) {
        this.definition = Optional.ofNullable(builder.definition);
        this.name = checkNonNull(builder.name);
        this.attributes = builder.attributes != null ? builder.attributes : Collections.emptyList();
        this.methods = builder.methods != null ? builder.methods : Collections.emptyList();
    }

    public static Builder builder() {
        return new Builder();
    }

    public Optional<String> getDefinition() {
        return definition;
    }

    public String getName() {
        return name;
    }

    public List<AttributeDefinition> getAttributes() {
        return attributes;
    }

    public List<MethodDefinition> getMethods() {
        return methods;
    }

}
