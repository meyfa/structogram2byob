package structogram2byob.program;

import org.junit.jupiter.api.Test;
import structogram2byob.ScratchType;

import static org.junit.jupiter.api.Assertions.*;


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
