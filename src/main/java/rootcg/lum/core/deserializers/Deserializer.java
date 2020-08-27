package rootcg.lum.core.deserializers;

import java.util.List;

public interface Deserializer<T> {

    boolean accept(List<String> block);

    T deserialize(List<String> block);

}
