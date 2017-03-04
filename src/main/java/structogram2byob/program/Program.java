package structogram2byob.program;

import java.util.ArrayList;
import java.util.List;

import scratchlib.objects.fixed.collections.ScratchObjectAbstractCollection;
import scratchlib.objects.fixed.collections.ScratchObjectArray;
import scratchlib.objects.fixed.collections.ScratchObjectOrderedCollection;
import scratchlib.objects.fixed.dimensions.ScratchObjectPoint;
import scratchlib.objects.user.morphs.ScratchObjectSpriteMorph;
import scratchlib.objects.user.morphs.ScratchObjectStageMorph;
import scratchlib.project.ScratchProject;
import scratchlib.project.ScratchVersion;
import structogram2byob.blocks.BlockRegistry;


/**
 * A "program" is a collection of "program units", which are scripts or
 * invocable methods that, taken together, form a concrete project.
 */
public class Program
{
    private final List<ProgramUnit> units = new ArrayList<>();

    /**
     * Adds the given unit to this program.
     * 
     * @param unit The unit to add.
     */
    public void addUnit(ProgramUnit unit)
    {
        units.add(unit);
    }

    /**
     * Converts this program into a {@link ScratchProject} by setting up the
     * stage and a sprite, as well as serializing all the units.
     * 
     * @param blocks The available blocks.
     * @return This program converted to a Scratch project.
     */
    public ScratchProject toScratch(BlockRegistry blocks)
    {
        ScratchProject project = new ScratchProject(ScratchVersion.BYOB311);

        ScratchObjectStageMorph stage = (ScratchObjectStageMorph) (project
                .getStageSection().get());

        ScratchObjectSpriteMorph sprite = new ScratchObjectSpriteMorph();
        ((ScratchObjectAbstractCollection) stage
                .getField(ScratchObjectStageMorph.FIELD_SPRITES)).add(sprite);
        ((ScratchObjectAbstractCollection) stage
                .getField(ScratchObjectStageMorph.FIELD_SUBMORPHS)).add(sprite);
        sprite.setField(ScratchObjectSpriteMorph.FIELD_OWNER, stage);

        ScratchObjectOrderedCollection cBlocks = new ScratchObjectOrderedCollection();
        stage.setField(ScratchObjectStageMorph.FIELD_CUSTOM_BLOCKS, cBlocks);
        sprite.setField(ScratchObjectSpriteMorph.FIELD_CUSTOM_BLOCKS, cBlocks);

        ScratchObjectArray blocksBin = new ScratchObjectArray();
        sprite.setField(ScratchObjectSpriteMorph.FIELD_BLOCKS_BIN, blocksBin);

        serializeUnits(blocks, blocksBin, cBlocks);

        return project;
    }

    /**
     * Converts all program units into Scratch objects and stores them either as
     * a script or as a custom block.
     * 
     * @param blocks The available blocks.
     * @param scripts The array of scripts to write to.
     * @param cBlocks The array of custom blocks to write to.
     */
    private void serializeUnits(BlockRegistry blocks,
            ScratchObjectArray scripts, ScratchObjectOrderedCollection cBlocks)
    {
        for (ProgramUnit u : units) {
            scripts.add(serializeUnitAsScript(u, blocks));
        }
    }

    /**
     * Converts the given unit into a Scratch script array, consisting of the
     * script's location point and its body as another array.
     * 
     * @param u The unit to serialize.
     * @param blocks The available blocks.
     * @return A Scratch object describing a script.
     */
    private ScratchObjectArray serializeUnitAsScript(ProgramUnit u,
            BlockRegistry blocks)
    {
        ScratchObjectArray script = new ScratchObjectArray();

        script.add(new ScratchObjectPoint((short) 20, (short) 20));
        script.add(u.toScratch(blocks));

        return script;
    }
}
