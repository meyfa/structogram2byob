package structogram2byob.program;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.util.Arrays;

import org.junit.Test;

import scratchlib.objects.fixed.data.ScratchObjectAbstractString;
import scratchlib.objects.user.ScratchObjectCustomBlockDefinition;
import scratchlib.objects.user.morphs.ScratchObjectSpriteMorph;
import scratchlib.objects.user.morphs.ScratchObjectStageMorph;
import scratchlib.project.ScratchProject;
import scratchlib.project.ScratchVersion;
import structogram2byob.ScratchType;
import structogram2byob.blocks.BlockDescription;
import structogram2byob.blocks.BlockRegistry;
import structogram2byob.blocks.FunctionBlock;
import structogram2byob.blocks.hats.StartClickedHatBlock;
import structogram2byob.program.expressions.BlockExpression;
import structogram2byob.program.expressions.StringExpression;


public class ProgramTest
{
    @Test
    public void generatesCorrectVersion()
    {
        Program obj = new Program();
        ScratchProject result = obj.toScratch(new BlockRegistry());

        assertSame(ScratchVersion.BYOB311, result.getVersion());
    }

    @Test
    public void generatesStageAndSprite()
    {
        Program obj = new Program();
        ScratchProject result = obj.toScratch(new BlockRegistry());

        assertNotNull(result.getStage());
        assertEquals(1, result.getStage().getSpriteCount());
    }

    @Test
    public void generatesScripts()
    {
        FunctionBlock say = new FunctionBlock(new BlockDescription.Builder()
                .label("say").param(ScratchType.ANY).build(), null, "say");

        Program obj = new Program();

        for (int i = 0; i < 3; ++i) {
            BlockDescription desc = new BlockDescription.Builder().label("when")
                    .label("start").label("clicked").build();

            ProgramUnit script;
            script = new ProgramUnit(UnitType.SCRIPT, desc, Arrays.asList(//
                    new BlockExpression(say.getDescription(), Arrays.asList(//
                            new StringExpression("hello world " + i)//
                    ))//
            ));

            obj.addUnit(script);
        }

        BlockRegistry reg = new BlockRegistry();
        reg.register(StartClickedHatBlock.instance);
        reg.register(say);

        ScratchProject result = obj.toScratch(reg);
        ScratchObjectSpriteMorph sprite = result.getStage().getSprite(0);

        assertEquals(3, sprite.getScriptCount());
    }

    @Test
    public void generatesCustomBlocks()
    {
        Program obj = new Program();

        for (int i = 0; i < 3; ++i) {
            BlockDescription desc = new BlockDescription.Builder().label("do")
                    .label("something").label(Integer.toString(i)).build();

            ProgramUnit cmd = new ProgramUnit(UnitType.COMMAND, desc,
                    Arrays.asList());

            obj.addUnit(cmd);
        }

        ScratchProject result = obj.toScratch(new BlockRegistry());

        ScratchObjectStageMorph stage = result.getStage();
        assertEquals(3, stage.getCustomBlockCount());

        ScratchObjectSpriteMorph sprite = stage.getSprite(0);
        assertEquals(3, sprite.getCustomBlockCount());
    }

    @Test
    public void generatesReportersWithCorrectReturnType()
    {
        Program obj = new Program();

        BlockDescription desc = new BlockDescription.Builder()
                .label("testreporter").build();
        ProgramUnit rep;
        rep = new ProgramUnit(UnitType.REPORTER, desc, Arrays.asList());
        obj.addUnit(rep);

        ScratchProject result = obj.toScratch(new BlockRegistry());

        ScratchObjectStageMorph stage = result.getStage();
        ScratchObjectCustomBlockDefinition cblock = stage.getCustomBlock(0);
        String type = ((ScratchObjectAbstractString) cblock
                .getField(ScratchObjectCustomBlockDefinition.FIELD_TYPE))
                        .getValue();

        assertEquals("any", type);
    }

    @Test
    public void generatesPredicatesWithCorrectReturnType()
    {
        Program obj = new Program();

        BlockDescription desc = new BlockDescription.Builder()
                .label("testreporter").build();
        ProgramUnit rep;
        rep = new ProgramUnit(UnitType.PREDICATE, desc, Arrays.asList());
        obj.addUnit(rep);

        ScratchProject result = obj.toScratch(new BlockRegistry());

        ScratchObjectStageMorph stage = result.getStage();
        ScratchObjectCustomBlockDefinition cblock = stage.getCustomBlock(0);
        String type = ((ScratchObjectAbstractString) cblock
                .getField(ScratchObjectCustomBlockDefinition.FIELD_TYPE))
                        .getValue();

        assertEquals("boolean", type);
    }

    @Test
    public void supportsCustomBlockInvocations()
    {
        Program obj = new Program();

        BlockDescription cmdDesc = new BlockDescription.Builder().label("do")
                .label("something").build();
        ProgramUnit cmd;
        cmd = new ProgramUnit(UnitType.COMMAND, cmdDesc, Arrays.asList());
        obj.addUnit(cmd);

        BlockDescription scriptDesc = new BlockDescription.Builder()
                .label("when").label("start").label("clicked").build();
        ProgramUnit script;
        script = new ProgramUnit(UnitType.SCRIPT, scriptDesc, Arrays.asList(//
                new BlockExpression(cmd.getInvocationBlock().getDescription(),
                        Arrays.asList())//
        ));
        obj.addUnit(script);

        BlockRegistry reg = new BlockRegistry();
        reg.register(StartClickedHatBlock.instance);

        ScratchProject result = obj.toScratch(reg);

        ScratchObjectStageMorph stage = result.getStage();
        assertEquals(1, stage.getCustomBlockCount());

        ScratchObjectSpriteMorph sprite = stage.getSprite(0);
        assertEquals(1, sprite.getCustomBlockCount());
        assertEquals(1, sprite.getScriptCount());
    }
}
