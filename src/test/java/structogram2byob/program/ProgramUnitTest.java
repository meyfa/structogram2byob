package structogram2byob.program;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import scratchlib.objects.fixed.collections.ScratchObjectAbstractCollection;
import scratchlib.objects.fixed.collections.ScratchObjectArray;
import scratchlib.objects.fixed.data.ScratchObjectString;
import scratchlib.objects.fixed.data.ScratchObjectSymbol;
import scratchlib.objects.fixed.data.ScratchObjectUtf8;
import scratchlib.objects.inline.ScratchObjectAbstractNumber;
import structogram2byob.ScratchType;
import structogram2byob.VariableContext;
import structogram2byob.blocks.Block;
import structogram2byob.blocks.BlockDescription;
import structogram2byob.blocks.BlockRegistry;
import structogram2byob.blocks.FunctionBlock;
import structogram2byob.blocks.hats.StartClickedHatBlock;
import structogram2byob.blocks.special.ScriptVariablesBlock;
import structogram2byob.blocks.structures.ForeverBlock;
import structogram2byob.program.expressions.BlockExpression;
import structogram2byob.program.expressions.Expression;
import structogram2byob.program.expressions.NumberExpression;
import structogram2byob.program.expressions.ScriptExpression;


public class ProgramUnitTest
{
    @Test
    public void returnsProperties()
    {
        BlockDescription desc = new BlockDescription.Builder()//
                .label("do").label("something").build();
        List<BlockExpression> blocks = new ArrayList<>();

        ProgramUnit obj = new ProgramUnit(UnitType.COMMAND, desc, blocks);

        assertSame(UnitType.COMMAND, obj.getType());
        assertEquals(blocks, obj.getBlocks());
        assertEquals("do something", obj.getUserSpec());
    }

    @Test
    public void constructsInvocationBlock()
    {
        BlockDescription desc = new BlockDescription.Builder()//
                .label("add").param(ScratchType.ANY, "a")
                .param(ScratchType.ANY, "b").build();
        List<BlockExpression> blocks = new ArrayList<>();

        ProgramUnit obj = new ProgramUnit(UnitType.COMMAND, desc, blocks);

        Block invoc = obj.getInvocationBlock();

        List<Expression> params = Arrays.asList(new NumberExpression(10),
                new NumberExpression(20));
        Map<String, VariableContext> vars = new HashMap<>();
        BlockRegistry reg = new BlockRegistry();

        ScratchObjectArray arr = invoc.toScratch(params, vars, reg);

        assertEquals(6, arr.size());

        assertEquals("byob", ((ScratchObjectSymbol) arr.get(0)).getValue());
        assertEquals("", ((ScratchObjectString) arr.get(1)).getValue());
        assertEquals("doCustomBlock",
                ((ScratchObjectSymbol) arr.get(2)).getValue());

        assertEquals("add %a %b", ((ScratchObjectUtf8) arr.get(3)).getValue());

        assertEquals(10, ((ScratchObjectAbstractNumber) arr.get(4)).intValue());
        assertEquals(20, ((ScratchObjectAbstractNumber) arr.get(5)).intValue());
    }

    @Test
    public void generatesEmptyCommand()
    {
        BlockDescription desc = new BlockDescription.Builder()//
                .label("do").label("something").build();
        List<BlockExpression> blocks = new ArrayList<>();

        ProgramUnit obj = new ProgramUnit(UnitType.COMMAND, desc, blocks);

        Map<String, VariableContext> vars = new HashMap<>();
        BlockRegistry reg = new BlockRegistry();

        ScratchObjectAbstractCollection scratch = obj.toScratch(vars, reg);

        assertEquals(0, scratch.size());
    }

    @Test
    public void generatesScriptHat()
    {
        BlockDescription desc = new BlockDescription.Builder()//
                .label("when").label("start").label("clicked").build();
        List<BlockExpression> blocks = new ArrayList<>();

        ProgramUnit obj = new ProgramUnit(UnitType.SCRIPT, desc, blocks);

        Map<String, VariableContext> vars = new HashMap<>();
        BlockRegistry reg = new BlockRegistry();
        reg.register(StartClickedHatBlock.instance);

        ScratchObjectAbstractCollection scratch = obj.toScratch(vars, reg);

        assertEquals(1, scratch.size());
    }

    @Test
    public void generatesBlocks()
    {
        BlockDescription desc = new BlockDescription.Builder()//
                .label("when").label("start").label("clicked").build();
        List<BlockExpression> blocks = new ArrayList<>();
        blocks.add(new BlockExpression(ForeverBlock.instance.getDescription(),
                Arrays.asList(new ScriptExpression(Arrays.asList()))));

        ProgramUnit obj = new ProgramUnit(UnitType.SCRIPT, desc, blocks);

        Map<String, VariableContext> vars = new HashMap<>();
        BlockRegistry reg = new BlockRegistry();
        reg.register(StartClickedHatBlock.instance);
        reg.register(ForeverBlock.instance);

        ScratchObjectAbstractCollection scratch = obj.toScratch(vars, reg);

        assertEquals(2, scratch.size());
    }

    @Test
    public void supportsCustomBlockParameters()
    {
        FunctionBlock report = new FunctionBlock(new BlockDescription.Builder()
                .label("report").param(ScratchType.ANY).build(), null,
                "doAnswer");
        FunctionBlock add = new FunctionBlock(
                new BlockDescription.Builder().param(ScratchType.NUMBER)
                        .label("+").param(ScratchType.NUMBER).build(),
                ScratchType.NUMBER, "+");

        BlockDescription desc = new BlockDescription.Builder()//
                .label("add").param(ScratchType.ANY, "a")
                .param(ScratchType.ANY, "b").build();
        List<BlockExpression> blocks = new ArrayList<>();
        blocks.add(new BlockExpression(report.getDescription(), Arrays.asList(//
                new BlockExpression(add.getDescription(), Arrays.asList(//
                        new BlockExpression(new BlockDescription.Builder()
                                .label("a").build(), Arrays.asList()), //
                        new BlockExpression(new BlockDescription.Builder()
                                .label("b").build(), Arrays.asList())//
                ))//
        )));

        ProgramUnit obj = new ProgramUnit(UnitType.COMMAND, desc, blocks);

        Map<String, VariableContext> vars = new HashMap<>();
        BlockRegistry reg = new BlockRegistry();
        reg.register(report);
        reg.register(add);

        ScratchObjectAbstractCollection scratch = obj.toScratch(vars, reg);

        assertEquals(1, scratch.size());
    }

    @Test
    public void supportsScriptVariablesBlock()
    {
        ScriptVariablesBlock svars = ScriptVariablesBlock.instance;
        FunctionBlock say = new FunctionBlock(new BlockDescription.Builder()
                .label("say").param(ScratchType.ANY).build(), null, "say");

        BlockDescription desc = new BlockDescription.Builder()//
                .label("do").label("something").build();
        List<BlockExpression> blocks = new ArrayList<>();
        blocks.add(new BlockExpression(svars.getDescription(), Arrays.asList(//
                new BlockExpression(
                        new BlockDescription.Builder().label("foo").build(),
                        Arrays.asList()))//
        ));
        blocks.add(new BlockExpression(say.getDescription(), Arrays.asList(//
                new BlockExpression(
                        new BlockDescription.Builder().label("foo").build(),
                        Arrays.asList())//
        )));

        ProgramUnit obj = new ProgramUnit(UnitType.COMMAND, desc, blocks);

        Map<String, VariableContext> vars = new HashMap<>();
        BlockRegistry reg = new BlockRegistry();
        reg.register(svars);
        reg.register(say);

        ScratchObjectAbstractCollection scratch = obj.toScratch(vars, reg);

        assertEquals(2, scratch.size());
    }
}
