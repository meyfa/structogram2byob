package structogram2byob.program;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import scratchlib.objects.fixed.collections.ScratchObjectArray;
import scratchlib.objects.fixed.collections.ScratchObjectOrderedCollection;
import scratchlib.objects.fixed.data.ScratchObjectSymbol;
import scratchlib.objects.fixed.dimensions.ScratchObjectPoint;
import scratchlib.objects.user.ScratchObjectCustomBlockDefinition;
import scratchlib.objects.user.morphs.ScratchObjectSpriteMorph;
import scratchlib.objects.user.morphs.ScratchObjectStageMorph;
import scratchlib.project.ScratchProject;
import scratchlib.project.ScratchVersion;
import structogram2byob.ScratchType;
import structogram2byob.VariableContext;
import structogram2byob.blocks.BlockRegistry;
import structogram2byob.program.expressions.BlockExpression;
import structogram2byob.program.expressions.Expression;
import structogram2byob.program.expressions.ScriptExpression;


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

        ScratchObjectStageMorph stage = project.getStage();

        ScratchObjectSpriteMorph sprite = new ScratchObjectSpriteMorph();
        stage.addSprite(sprite);

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
        Map<String, VariableContext> vars = new HashMap<>();
        vars = Collections.unmodifiableMap(vars);

        // extend the block registry by all available custom blocks
        BlockRegistry blocksExtended = new BlockRegistry(blocks);
        for (ProgramUnit u : units) {
            if (u.getType() != UnitType.SCRIPT) {
                blocksExtended.register(u.getInvocationBlock());
            }
        }

        // write the units
        int y = 20;
        for (ProgramUnit u : units) {
            if (u.getType() == UnitType.SCRIPT) {
                scripts.add(serializeUnitAsScript(u, vars, blocksExtended, y));
                y += 50 + u.getBlocks().stream().mapToInt(this::estimateHeight)
                        .sum();
            } else {
                cBlocks.add(serializeUnitAsBlock(u, vars, blocksExtended));
            }
        }
    }

    private int estimateHeight(BlockExpression exp)
    {
        int height = 28;

        for (Expression param : exp.getParameters()) {
            if (param instanceof ScriptExpression) {
                height += ((ScriptExpression) param).getBlocks().stream()
                        .mapToInt(this::estimateHeight).sum();
            }
        }

        return height;
    }

    /**
     * Converts the given unit into a Scratch script array, consisting of the
     * script's location point and its body as another array.
     * 
     * @param u The unit to serialize.
     * @param vars A map of variable names to {@link VariableContext}s.
     * @param blocks The available blocks.
     * @param y The y coordinate for script placement.
     * @return A Scratch object describing a script.
     */
    private ScratchObjectArray serializeUnitAsScript(ProgramUnit u,
            Map<String, VariableContext> vars, BlockRegistry blocks, int y)
    {
        ScratchObjectArray script = new ScratchObjectArray();

        script.add(new ScratchObjectPoint(20, y));
        script.add(u.toScratch(vars, blocks));

        return script;
    }

    /**
     * Converts the given unit into a Scratch custom block definition.
     * 
     * @param u The unit to serialize.
     * @param vars A map of variable names to {@link VariableContext}s.
     * @param blocks The available blocks.
     * @return A {@link ScratchObjectCustomBlockDefinition} instance.
     */
    private ScratchObjectCustomBlockDefinition serializeUnitAsBlock(
            ProgramUnit u, Map<String, VariableContext> vars,
            BlockRegistry blocks)
    {
        ScratchObjectCustomBlockDefinition cbd = new ScratchObjectCustomBlockDefinition();

        cbd.setUserSpec(u.getUserSpec());
        cbd.setBody(u.toScratch(vars, blocks));

        ScratchType type = u.getType().getReturnType();
        String typeName = type == null ? "none" : type.name().toLowerCase();
        cbd.setField(ScratchObjectCustomBlockDefinition.FIELD_TYPE,
                new ScratchObjectSymbol(typeName));

        return cbd;
    }
}
