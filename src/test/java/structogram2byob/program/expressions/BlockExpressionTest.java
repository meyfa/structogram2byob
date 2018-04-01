package structogram2byob.program.expressions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import scratchlib.objects.fixed.collections.ScratchObjectArray;
import scratchlib.objects.fixed.data.ScratchObjectString;
import scratchlib.objects.fixed.data.ScratchObjectSymbol;
import scratchlib.objects.fixed.data.ScratchObjectUtf8;
import scratchlib.objects.user.ScratchObjectVariableFrame;
import structogram2byob.ScratchType;
import structogram2byob.VariableContext;
import structogram2byob.blocks.Block;
import structogram2byob.blocks.BlockDescription;
import structogram2byob.blocks.BlockRegistry;
import structogram2byob.blocks.FunctionBlock;
import structogram2byob.program.ProgramUnit;
import structogram2byob.program.UnitType;


public class BlockExpressionTest
{
    private static final BlockDescription DESC = new BlockDescription.Builder()
            .label("move").param(ScratchType.NUMBER).label("steps").build();

    private static final BlockDescription DESC_VAR = new BlockDescription.Builder()
            .label("foobar").build();

    @Test
    public void returnsDescription()
    {
        BlockExpression obj = new BlockExpression(DESC,
                Arrays.asList(new NumberExpression(42)));

        assertSame(DESC, obj.getDescription());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void returnsUnmodifiableParametersList()
    {
        List<Expression> params = new ArrayList<>();
        params.add(new NumberExpression(42));

        BlockExpression obj = new BlockExpression(DESC, params);

        assertEquals(params, obj.getParameters());

        // throws
        obj.getParameters().set(0, new NumberExpression(37));
    }

    @Test
    public void returnsCorrectType()
    {
        BlockExpression obj = new BlockExpression(DESC,
                Arrays.asList(new NumberExpression(42)));

        assertSame(ScratchType.ANY, obj.getType());
    }

    @Test
    public void convertsFunctionBlockToScratch()
    {
        Map<String, VariableContext> vars = new HashMap<>();
        BlockRegistry blocks = new BlockRegistry();
        Block block = new FunctionBlock(DESC, null, "forward:");
        blocks.register(block);

        BlockExpression obj = new BlockExpression(DESC,
                Arrays.asList(new NumberExpression(42)));

        ScratchObjectArray blockResult = block.toScratch(
                Arrays.asList(new NumberExpression(42)), vars, blocks);
        ScratchObjectArray objResult = (ScratchObjectArray) obj.toScratch(vars,
                blocks);

        assertEquals(blockResult.size(), objResult.size());

        for (int i = 0; i < blockResult.size(); ++i) {
            assertEquals(blockResult.get(i).getClass(),
                    objResult.get(i).getClass());
        }
    }

    @Test
    public void convertsGlobalVariableToScratch()
    {
        Map<String, VariableContext> vars = new HashMap<>();
        vars.put("foobar", VariableContext.GLOBAL);
        BlockRegistry blocks = new BlockRegistry();

        BlockExpression obj = new BlockExpression(DESC_VAR, Arrays.asList());

        ScratchObjectArray result = (ScratchObjectArray) obj.toScratch(vars,
                blocks);

        assertEquals(2, result.size());
        assertEquals("readVariable",
                ((ScratchObjectSymbol) result.get(0)).getValue());
        assertEquals("foobar", ((ScratchObjectUtf8) result.get(1)).getValue());
    }

    @Test
    public void convertsUnitVariableToScratch()
    {
        ProgramUnit unit = new ProgramUnit(//
                UnitType.COMMAND, // type
                new BlockDescription.Builder().label("doSomething")
                        .param(ScratchType.ANY, "foobar").build(), // desc
                Arrays.asList() // blocks
        );

        Map<String, VariableContext> vars = new HashMap<>();
        vars.put("foobar", new VariableContext.UnitSpecific(unit));
        BlockRegistry blocks = new BlockRegistry();

        BlockExpression obj = new BlockExpression(DESC_VAR, Arrays.asList());

        ScratchObjectArray result = (ScratchObjectArray) obj.toScratch(vars,
                blocks);

        assertEquals(5, result.size());
        assertEquals("byob", ((ScratchObjectSymbol) result.get(0)).getValue());
        assertEquals("", ((ScratchObjectString) result.get(1)).getValue());
        assertEquals("readBlockVariable",
                ((ScratchObjectSymbol) result.get(2)).getValue());
        assertEquals("foobar", ((ScratchObjectUtf8) result.get(3)).getValue());
        assertEquals("doSomething %foobar",
                ((ScratchObjectUtf8) result.get(4)).getValue());
    }

    @Test
    public void convertsScriptVariableToScratch()
    {
        Map<String, VariableContext> vars = new HashMap<>();
        ScratchObjectVariableFrame frame = new ScratchObjectVariableFrame();
        vars.put("foobar", new VariableContext.ScriptSpecific(frame));
        BlockRegistry blocks = new BlockRegistry();

        BlockExpression obj = new BlockExpression(DESC_VAR, Arrays.asList());

        ScratchObjectArray result = (ScratchObjectArray) obj.toScratch(vars,
                blocks);

        assertEquals(5, result.size());
        assertEquals("byob", ((ScratchObjectSymbol) result.get(0)).getValue());
        assertEquals("", ((ScratchObjectString) result.get(1)).getValue());
        assertEquals("readBlockVariable",
                ((ScratchObjectSymbol) result.get(2)).getValue());
        assertEquals("foobar", ((ScratchObjectUtf8) result.get(3)).getValue());
        assertSame(frame, result.get(4));
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsForUnknownBlock()
    {
        Map<String, VariableContext> vars = new HashMap<>();
        BlockRegistry blocks = new BlockRegistry();

        BlockExpression obj = new BlockExpression(DESC,
                Arrays.asList(new NumberExpression(42)));

        obj.toScratch(vars, blocks);
    }

    @Test
    public void convertsToString()
    {
        BlockExpression obj;

        obj = new BlockExpression(
                new BlockDescription.Builder().label("foo").build(),
                Arrays.asList());
        assertEquals("(foo)", obj.toString());

        obj = new BlockExpression(
                new BlockDescription.Builder().label("foo")
                        .param(ScratchType.ANY).label("bar").build(),
                Arrays.asList(//
                        new BlockExpression(new BlockDescription.Builder()
                                .label("param").build(), Arrays.asList())//
                ));
        assertEquals("(foo (param) bar)", obj.toString());
    }
}
