package rootcg.lum.core.definitions;

import static rootcg.lum.util.Validations.Arguments.checkNonNull;

public class RelationDefinition {

    public static class Builder {

        private String source;
        private String target;
        private RelationType type;

        private Builder() {

        }

        public Builder withSource(String source) {
            this.source = source;
            return this;
        }

        public Builder withTarget(String target) {
            this.target = target;
            return this;
        }

        public Builder withType(RelationType type) {
            this.type = type;
            return this;
        }

        public RelationDefinition build() {
            return new RelationDefinition(this);
        }

    }

    private final String source;
    private final String target;
    private final RelationType type;

    private RelationDefinition(Builder builder) {
        this.source = checkNonNull(builder.source);
        this.target = checkNonNull(builder.target);
        this.type = checkNonNull(builder.type);
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }

    public RelationType getType() {
        return type;
    }

}
