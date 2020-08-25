package rootcg.lum.definitions;

import java.util.Optional;

public final class AttributeDefinition {

    private final Scope scope;
    private final String type;
    private final String name;

    public AttributeDefinition(String name) {
        this(null, name);
    }

    public AttributeDefinition(String type, String name) {
        this(Scope.PUBLIC, type, name);
    }

    public AttributeDefinition(Scope scope, String type, String name) {
        this.scope = scope;
        this.type = type;
        this.name = name;
    }

    public Scope getScope() {
        return scope;
    }

    public Optional<String> getType() {
        return Optional.ofNullable(type);
    }

    public String getName() {
        return name;
    }

}
