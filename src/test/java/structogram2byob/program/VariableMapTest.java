package structogram2byob.program;

import org.junit.jupiter.api.Test;
import scratchlib.objects.user.ScratchObjectVariableFrame;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


public class VariableMapTest
{
    @Test
    public void get()
    {
        VariableContext global = VariableContext.getGlobal();
        VariableMap obj = new VariableMap(Collections.singletonMap("foo", global));

        assertSame(global, obj.get("foo"));
        assertNull(obj.get("bar"));
    }

    @Test
    public void combine()
    {
        VariableContext global = VariableContext.getGlobal();
        VariableContext frame = VariableContext.getForScript(new ScratchObjectVariableFrame());

        Map<String, VariableContext> exist = new HashMap<>();
        exist.put("foo", global);
        exist.put("bar", global);

        Map<String, VariableContext> add = new HashMap<>();
        add.put("bar", frame);
        add.put("baz", frame);

        VariableMap combined = new VariableMap(exist).combine(new VariableMap(add));

        assertSame(global, combined.get("foo"));
        assertSame(frame, combined.get("bar"));
        assertSame(frame, combined.get("baz"));
    }
}
