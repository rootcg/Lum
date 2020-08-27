package rootcg.lum.core.definitions;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static rootcg.lum.util.Validations.Arguments.checkAnyNonNull;

public class MethodDefinition {

    public static class Builder {

        private Scope scope;
        private String type;
        private String name;
        private List<ParameterDefinition> parameters;

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

        public Builder withParameters(List<ParameterDefinition> parameters) {
            this.parameters = parameters;
            return this;
        }

        public MethodDefinition build() {
            return new MethodDefinition(this);
        }

    }

    private final Scope scope;
    private final Optional<String> type;
    private final Optional<String> name;
    private final List<ParameterDefinition> parameters;

    private MethodDefinition(Builder builder) {
        checkAnyNonNull("type or name should be specified", builder.type, builder.name);

        this.scope = builder.scope != null ? builder.scope : Scope.PUBLIC;
        this.type = Optional.ofNullable(builder.type);
        this.name = Optional.ofNullable(builder.name);
        this.parameters = builder.parameters != null ? builder.parameters : Collections.emptyList();
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

    public List<ParameterDefinition> getParameters() {
        return parameters;
    }

}
