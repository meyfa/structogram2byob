package structogram2byob;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class ScratchTypeTest
{
    @Test
    public void checksAssignability()
    {
        assertFalse(ScratchType.BOOLEAN.isAssignableFrom(ScratchType.NUMBER));

        assertTrue(ScratchType.NUMBER.isAssignableFrom(ScratchType.NUMBER));

        assertTrue(ScratchType.ANY.isAssignableFrom(ScratchType.NUMBER));
        assertTrue(ScratchType.NUMBER.isAssignableFrom(ScratchType.ANY));
    }
}
