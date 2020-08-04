package structogram2byob.program;

import org.junit.jupiter.api.Test;
import scratchlib.objects.ScratchObject;
import scratchlib.objects.fixed.data.ScratchObjectAbstractString;
import scratchlib.objects.user.ScratchObjectVariableFrame;
import structogram2byob.blocks.BlockDescription;
import structogram2byob.program.expressions.BlockExpression;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class VariableContextTest
{
    @Test
    public void global()
    {
        VariableContext obj = VariableContext.getGlobal();

        assertFalse(obj.requiresBYOB());
        assertThrows(UnsupportedOperationException.class, obj::getReadMarker);
        assertThrows(UnsupportedOperationException.class, obj::getWriteMarker);
    }

    @Test
    public void unit()
    {
        BlockDescription desc = new BlockDescription.Builder().label("do").label("something").build();
        List<BlockExpression> blocks = new ArrayList<>();
        ProgramUnit unit = new ProgramUnit(null, UnitType.COMMAND, desc, blocks);

        VariableContext obj = VariableContext.getForUnit(unit);

        assertTrue(obj.requiresBYOB());

        ScratchObject readMarker = obj.getReadMarker();
        assertTrue(readMarker instanceof ScratchObjectAbstractString);
        assertEquals("do something", ((ScratchObjectAbstractString) readMarker).getValue());

        assertSame(ScratchObject.NIL, obj.getWriteMarker());
    }

    @Test
    public void script()
    {
        ScratchObjectVariableFrame frame = new ScratchObjectVariableFrame();
        VariableContext obj = VariableContext.getForScript(frame);

        assertTrue(obj.requiresBYOB());
        assertSame(frame, obj.getReadMarker());
        assertSame(frame, obj.getWriteMarker());
    }
}
