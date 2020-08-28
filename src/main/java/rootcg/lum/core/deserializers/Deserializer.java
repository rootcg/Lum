package rootcg.lum.core.deserializers;

import rootcg.lum.core.deserializers.exceptions.DeserializationException;

import java.util.List;

public interface Deserializer<T> {

    boolean accept(List<String> block);

    T deserialize(List<String> block) throws DeserializationException;

}
