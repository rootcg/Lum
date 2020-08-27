package rootcg.lum.core.definitions;

import java.util.Optional;

import static rootcg.lum.util.Validations.Arguments.checkAnyNonNull;

public final class ParameterDefinition {

    public static class Builder {

        private String type;
        private String name;

        private Builder() {

        }

        public Builder withType(String type) {
            this.type = type;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public ParameterDefinition build() {
            return new ParameterDefinition(this);
        }

    }

    private final Optional<String> type;
    private final Optional<String> name;

    private ParameterDefinition(Builder builder) {
        checkAnyNonNull("type or name should be specified", builder.type, builder.name);

        this.type = Optional.ofNullable(builder.type);
        this.name = Optional.ofNullable(builder.name);
    }

    public static Builder builder() {
        return new Builder();
    }

    public Optional<String> getType() {
        return type;
    }

    public Optional<String> getName() {
        return name;
    }

}
