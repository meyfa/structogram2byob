package structogram2byob.blocks.special;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import scratchlib.objects.fixed.collections.ScratchObjectArray;
import scratchlib.objects.fixed.data.ScratchObjectString;
import scratchlib.objects.fixed.data.ScratchObjectSymbol;
import scratchlib.objects.fixed.data.ScratchObjectUtf8;
import scratchlib.objects.user.ScratchObjectVariableFrame;
import structogram2byob.blocks.BlockDescription;
import structogram2byob.blocks.BlockRegistry;
import structogram2byob.program.VariableContext;
import structogram2byob.program.expressions.BlockExpression;
import structogram2byob.program.expressions.Expression;

import static org.junit.jupiter.api.Assertions.*;


public class ScriptVariablesBlockTest
{
    @Test
    public void convertsToScratch()
    {
        ScriptVariablesBlock obj = ScriptVariablesBlock.instance;

        BlockExpression varA = new BlockExpression(null,
                new BlockDescription.Builder().label("a").build(),
                Collections.emptyList());
        BlockExpression varB = new BlockExpression(null,
                new BlockDescription.Builder().label("b").build(),
                Collections.emptyList());

        List<Expression> params = Arrays.asList(varA, varB);
        Map<String, VariableContext> vars = new HashMap<>();
        BlockRegistry blocks = new BlockRegistry();

        ScratchObjectArray scratch = obj.toScratch(params, vars, blocks);

        assertEquals(5, scratch.size());

        // starts with method call
        assertEquals("byob", ((ScratchObjectSymbol) scratch.get(0)).getValue());
        assertEquals("", ((ScratchObjectString) scratch.get(1)).getValue());
        assertEquals("doDeclareVariables", ((ScratchObjectSymbol) scratch.get(2)).getValue());

        // continues with first variable
        ScratchObjectArray v0 = (ScratchObjectArray) scratch.get(3);
        assertEquals(5, v0.size());
        // - method call
        assertEquals("byob", ((ScratchObjectSymbol) v0.get(0)).getValue());
        assertEquals("", ((ScratchObjectString) v0.get(1)).getValue());
        assertEquals("readBlockVariable", ((ScratchObjectSymbol) v0.get(2)).getValue());
        // - name
        assertEquals("a", ((ScratchObjectUtf8) v0.get(3)).getValue());
        // - variable frame
        assertTrue(v0.get(4) instanceof ScratchObjectVariableFrame);

        // ends with second variable
        ScratchObjectArray v1 = (ScratchObjectArray) scratch.get(4);
        assertEquals(5, v1.size());
        // - method call
        assertEquals("byob", ((ScratchObjectSymbol) v1.get(0)).getValue());
        assertEquals("", ((ScratchObjectString) v1.get(1)).getValue());
        assertEquals("readBlockVariable", ((ScratchObjectSymbol) v1.get(2)).getValue());
        // - name
        assertEquals("b", ((ScratchObjectUtf8) v1.get(3)).getValue());
        // - variable frame
        assertTrue(v1.get(4) instanceof ScratchObjectVariableFrame);
    }

    @Test
    public void retrievesFrames()
    {
        ScriptVariablesBlock obj = ScriptVariablesBlock.instance;

        BlockExpression varA = new BlockExpression(null,
                new BlockDescription.Builder().label("a").build(),
                Collections.emptyList());
        BlockExpression varB = new BlockExpression(null,
                new BlockDescription.Builder().label("b").build(),
                Collections.emptyList());

        List<Expression> params = Arrays.asList(varA, varB);
        Map<String, VariableContext> vars = new HashMap<>();
        BlockRegistry blocks = new BlockRegistry();

        ScratchObjectArray scratch = obj.toScratch(params, vars, blocks);

        Map<String, ScratchObjectVariableFrame> ret = ScriptVariablesBlock.retrieveFrames(scratch);

        assertEquals(2, ret.size());

        assertNotNull(ret.get("a"));
        assertNotNull(ret.get("b"));

        assertNotSame(ret.get("a"), ret.get("b"));
    }
}
