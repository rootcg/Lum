package rootcg.lum.definitions;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static rootcg.lum.util.Validations.checkNonNullArgument;

public class MethodDefinition {

    private static final String VOID_TYPE = "void";

    private final Scope scope;
    private final String type;
    private final String name;
    private final List<ParameterDefinition> parameters;

    public MethodDefinition(Scope scope, String type, String name, List<ParameterDefinition> parameters) {
        this.scope = checkNonNullArgument(scope);
        this.type = type;
        this.name = checkNonNullArgument(name);
        this.parameters = parameters != null ? parameters : Collections.emptyList();
    }

    public static String getVoidType() {
        return VOID_TYPE;
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

    public List<ParameterDefinition> getParameters() {
        return parameters;
    }

}
