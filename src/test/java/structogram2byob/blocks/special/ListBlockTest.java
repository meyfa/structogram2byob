package structogram2byob.blocks.special;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import scratchlib.objects.fixed.collections.ScratchObjectAbstractCollection;
import scratchlib.objects.fixed.collections.ScratchObjectArray;
import scratchlib.objects.fixed.data.ScratchObjectAbstractString;
import scratchlib.objects.fixed.data.ScratchObjectString;
import scratchlib.objects.fixed.data.ScratchObjectSymbol;
import scratchlib.objects.user.morphs.ScratchObjectListMorph;
import structogram2byob.blocks.BlockRegistry;
import structogram2byob.program.ScratchConversionException;
import structogram2byob.program.VariableContext;
import structogram2byob.program.expressions.Expression;
import structogram2byob.program.expressions.StringExpression;


public class ListBlockTest
{
    @Test
    public void convertsToScratch() throws ScratchConversionException
    {
        ListBlock obj = ListBlock.instance;

        List<Expression> params = Arrays.asList(
                new StringExpression(null, "hello"),
                new StringExpression(null, "world"));
        Map<String, VariableContext> vars = new HashMap<>();
        BlockRegistry blocks = new BlockRegistry();

        ScratchObjectArray scratch = obj.toScratch(params, vars, blocks);

        assertEquals(4, scratch.size());

        // starts with method call
        assertEquals("byob", ((ScratchObjectSymbol) scratch.get(0)).getValue());
        assertEquals("", ((ScratchObjectString) scratch.get(1)).getValue());
        assertEquals("newList:",
                ((ScratchObjectSymbol) scratch.get(2)).getValue());

        // continues with list morph containing params
        ScratchObjectListMorph list = (ScratchObjectListMorph) scratch.get(3);
        ScratchObjectAbstractCollection cellMorphs = (ScratchObjectAbstractCollection) list
                .getField(ScratchObjectListMorph.FIELD_CELL_MORPHS);
        assertEquals(2, cellMorphs.size());
        assertEquals("hello",
                ((ScratchObjectAbstractString) cellMorphs.get(0)).getValue());
        assertEquals("world",
                ((ScratchObjectAbstractString) cellMorphs.get(1)).getValue());
    }
}
