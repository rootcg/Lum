package rootcg.lum.core.deserializers.impl;

import rootcg.lum.core.definitions.ObjectDefinition;
import rootcg.lum.core.deserializers.Deserializer;

import java.util.List;

public class ObjectDeserializer implements Deserializer<ObjectDefinition> {

    @Override
    public boolean accept(List<String> block) {
        return false;
    }

    @Override
    public ObjectDefinition deserialize(List<String> block) {
        throw new IllegalStateException("not implemented");
    }

}
