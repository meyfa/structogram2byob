package structogram2byob.program;

import java.util.Collections;

import org.junit.jupiter.api.Test;
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

import static org.junit.jupiter.api.Assertions.*;


public class ProgramTest
{
    @Test
    public void generatesCorrectVersion() throws ScratchConversionException
    {
        Program obj = new Program();
        ScratchProject result = obj.toScratch(new BlockRegistry());

        assertSame(ScratchVersion.BYOB311, result.getVersion());
    }

    @Test
    public void generatesStageAndSprite() throws ScratchConversionException
    {
        Program obj = new Program();
        ScratchProject result = obj.toScratch(new BlockRegistry());

        assertNotNull(result.getStage());
        assertEquals(1, result.getStage().getSpriteCount());
    }

    @Test
    public void generatesScripts() throws ScratchConversionException
    {
        FunctionBlock say = new FunctionBlock(
                new BlockDescription.Builder().label("say").param(ScratchType.ANY).build(), null, "say");

        Program obj = new Program();

        for (int i = 0; i < 3; ++i) {
            BlockDescription desc = new BlockDescription.Builder().label("when")
                    .label("start").label("clicked").build();

            ProgramUnit script;
            script = new ProgramUnit(null, UnitType.SCRIPT, desc, Collections.singletonList(
                    new BlockExpression(null, say.getDescription(),
                            Collections.singletonList(new StringExpression(null, "hello world " + i)))
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
    public void generatesCustomBlocks() throws ScratchConversionException
    {
        Program obj = new Program();

        for (int i = 0; i < 3; ++i) {
            BlockDescription desc = new BlockDescription.Builder().label("do")
                    .label("something").label(Integer.toString(i)).build();

            ProgramUnit cmd = new ProgramUnit(null, UnitType.COMMAND, desc, Collections.emptyList());
            obj.addUnit(cmd);
        }

        ScratchProject result = obj.toScratch(new BlockRegistry());

        ScratchObjectStageMorph stage = result.getStage();
        assertEquals(3, stage.getCustomBlockCount());

        ScratchObjectSpriteMorph sprite = stage.getSprite(0);
        assertEquals(3, sprite.getCustomBlockCount());
    }

    @Test
    public void generatesReportersWithCorrectReturnType() throws ScratchConversionException
    {
        Program obj = new Program();

        BlockDescription desc = new BlockDescription.Builder().label("testreporter").build();
        ProgramUnit rep;
        rep = new ProgramUnit(null, UnitType.REPORTER, desc, Collections.emptyList());
        obj.addUnit(rep);

        ScratchProject result = obj.toScratch(new BlockRegistry());

        ScratchObjectStageMorph stage = result.getStage();
        ScratchObjectCustomBlockDefinition cblock = stage.getCustomBlock(0);
        String type = ((ScratchObjectAbstractString) cblock.getField(ScratchObjectCustomBlockDefinition.FIELD_TYPE))
                        .getValue();

        assertEquals("any", type);
    }

    @Test
    public void generatesPredicatesWithCorrectReturnType() throws ScratchConversionException
    {
        Program obj = new Program();

        BlockDescription desc = new BlockDescription.Builder().label("testreporter").build();
        ProgramUnit rep;
        rep = new ProgramUnit(null, UnitType.PREDICATE, desc, Collections.emptyList());
        obj.addUnit(rep);

        ScratchProject result = obj.toScratch(new BlockRegistry());

        ScratchObjectStageMorph stage = result.getStage();
        ScratchObjectCustomBlockDefinition cblock = stage.getCustomBlock(0);
        String type = ((ScratchObjectAbstractString) cblock.getField(ScratchObjectCustomBlockDefinition.FIELD_TYPE))
                        .getValue();

        assertEquals("boolean", type);
    }

    @Test
    public void supportsCustomBlockInvocations() throws ScratchConversionException
    {
        Program obj = new Program();

        BlockDescription cmdDesc = new BlockDescription.Builder().label("do").label("something").build();
        ProgramUnit cmd;
        cmd = new ProgramUnit(null, UnitType.COMMAND, cmdDesc, Collections.emptyList());
        obj.addUnit(cmd);

        BlockDescription scriptDesc = new BlockDescription.Builder()
                .label("when").label("start").label("clicked").build();
        ProgramUnit script;
        script = new ProgramUnit(null, UnitType.SCRIPT, scriptDesc, Collections.singletonList(
                new BlockExpression(null, cmd.getInvocationBlock().getDescription(), Collections.emptyList())));
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
