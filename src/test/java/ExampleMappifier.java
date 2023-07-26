import nl.sander.reflective.tomap.AbstractToMap;
import nl.sander.reflective.compare.PlumBean;

import java.util.HashMap;
import java.util.Map;

public class ExampleMappifier extends AbstractToMap {

    public Map<String, Object> toMap(Object o) {
        HashMap<String, Object> m = new HashMap<>();
        add(m, "core", ((PlumBean) o).getCore());
        add(m, "number", ((PlumBean) o).getNumber());
        return m;
    }
}
