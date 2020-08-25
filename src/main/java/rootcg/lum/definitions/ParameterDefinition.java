package rootcg.lum.definitions;

import java.util.Optional;

public class ParameterDefinition {

    private final String type;
    private final String name;

    public ParameterDefinition(String name) {
        this(null, name);
    }

    public ParameterDefinition(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public Optional<String> getType() {
        return Optional.ofNullable(type);
    }

    public String getName() {
        return name;
    }

}
