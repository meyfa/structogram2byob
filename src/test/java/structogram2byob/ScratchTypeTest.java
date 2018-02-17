package structogram2byob;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;


public class ScratchTypeTest
{
    @Test
    public void checksAssignability()
    {
        assertFalse(ScratchType.BOOLEAN.isAssignableFrom(ScratchType.NUMBER));

        assertTrue(ScratchType.ANY.isAssignableFrom(ScratchType.NUMBER));
        assertTrue(ScratchType.NUMBER.isAssignableFrom(ScratchType.ANY));
    }
}
