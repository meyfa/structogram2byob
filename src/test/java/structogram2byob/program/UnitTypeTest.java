package structogram2byob.program;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import structogram2byob.ScratchType;


public class UnitTypeTest
{
    @Test
    public void hasCorrectReturnTypes()
    {
        assertNull(UnitType.SCRIPT.getReturnType());

        assertNull(UnitType.COMMAND.getReturnType());
        assertSame(ScratchType.ANY, UnitType.REPORTER.getReturnType());
        assertSame(ScratchType.BOOLEAN, UnitType.PREDICATE.getReturnType());
    }
}
