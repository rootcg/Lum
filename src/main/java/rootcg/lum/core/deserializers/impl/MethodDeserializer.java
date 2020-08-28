package rootcg.lum.core.deserializers.impl;

import rootcg.lum.core.definitions.MethodDefinition;
import rootcg.lum.core.definitions.ParameterDefinition;
import rootcg.lum.core.deserializers.Deserializer;
import rootcg.lum.core.deserializers.exceptions.DeserializationException;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MethodDeserializer implements Deserializer<MethodDefinition> {

    private static final Pattern pattern = Pattern.compile("^([^\\s]*)\\s?([a-zA-Z]*)\\((.*)\\)$");
    private static final Pattern expressionSplitter = Pattern.compile(" ");
    private static final Pattern parameterSplitter = Pattern.compile(",");

    @Override
    public boolean accept(List<String> block) {
        return block.size() == 1 && pattern.matcher(normalize(block.get(0))).matches();
    }

    @Override
    public MethodDefinition deserialize(List<String> block) throws DeserializationException {
        String expression = normalize(block.get(0));

        Matcher methodMatcher = pattern.matcher(expression);
        if (!methodMatcher.matches())
            throw new DeserializationException(expression);

        MethodDefinition.Builder methodBuilder = MethodDefinition.builder();
        if (!methodMatcher.group(1).isBlank() && !methodMatcher.group(2).isBlank())
            methodBuilder = methodBuilder.withType(methodMatcher.group(1)).withName(methodMatcher.group(2));
        else if (!methodMatcher.group(1).isBlank())
            methodBuilder = methodBuilder.withName(methodMatcher.group(1));
        else
            throw new DeserializationException(expression);

        List<String> parameters =
                parameterSplitter.splitAsStream(methodMatcher.group(3))
                                 .map(String::strip)
                                 .filter(Predicate.not(String::isEmpty))
                                 .collect(Collectors.toList());

        List<ParameterDefinition> parameterDefinitions = new ArrayList<>();
        for (String parameter : parameters)
            parameterDefinitions.add(createParameter(parameter));

        methodBuilder.withParameters(parameterDefinitions);
        return methodBuilder.build();
    }

    private String normalize(String source) {
        return source.strip();
    }

    private static ParameterDefinition createParameter(String source) throws DeserializationException {
        String[] expression = expressionSplitter.split(source);
        ParameterDefinition.Builder parameterBuilder = ParameterDefinition.builder();

        if (expression.length == 1)
            parameterBuilder = parameterBuilder.withName(expression[0]);
        else if (expression.length == 2)
            parameterBuilder = parameterBuilder.withType(expression[0]).withName(expression[1]);
        else
            throw new DeserializationException(source);

        return parameterBuilder.build();
    }

}
