package rootcg.lum.core.definitions;

import java.util.Optional;

public final class AttributeDefinition {

    public static class Builder {

        private Scope scope;
        private String type;
        private String name;

        private Builder() {

        }

        public Builder withScope(Scope scope) {
            this.scope = scope;
            return this;
        }

        public Builder withType(String type) {
            this.type = type;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public AttributeDefinition build() {
            return new AttributeDefinition(this);
        }

    }

    private final Scope scope;
    private final Optional<String> type;
    private final Optional<String> name;

    private AttributeDefinition(Builder builder) {
        this.scope = builder.scope != null ? builder.scope : Scope.PUBLIC;
        this.type = Optional.ofNullable(builder.type);
        this.name = Optional.ofNullable(builder.name);
    }

    public static Builder builder() {
        return new Builder();
    }

    public Scope getScope() {
        return scope;
    }

    public Optional<String> getType() {
        return type;
    }

    public Optional<String> getName() {
        return name;
    }

}
