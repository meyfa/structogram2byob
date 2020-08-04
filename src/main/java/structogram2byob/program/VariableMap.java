package structogram2byob.program;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


/**
 * A map of variable names to their respective contexts ({@link VariableContext}).
 * All instances are immutable.
 */
public class VariableMap
{
    /**
     * The empty variable mapping.
     */
    public static final VariableMap EMPTY = new VariableMap(Collections.emptyMap());

    private final Map<String, VariableContext> contexts;

    /**
     * Construct a new map with the given existing entries.
     *
     * @param entries The entries.
     */
    public VariableMap(Map<String, VariableContext> entries)
    {
        this.contexts = new HashMap<>(entries);
    }

    /**
     * Get the context for the given name. Returns null if variable unknown.
     *
     * @param name The variable name.
     * @return The context or null.
     */
    public VariableContext get(String name)
    {
        return contexts.get(name);
    }

    /**
     * Create a copy of this map, then add all entries of the 'additions' map into that map,
     * potentially overwriting existing definitions.
     * <p>
     * This leaves both existing maps unchanged.
     *
     * @param additions The new entries.
     * @return The new map that is a combination of this one and the given one.
     */
    public VariableMap combine(VariableMap additions)
    {
        Map<String, VariableContext> combined = new HashMap<>();
        combined.putAll(contexts);
        combined.putAll(additions.contexts);

        return new VariableMap(combined);
    }
}
