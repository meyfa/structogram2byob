package structogram2byob.program;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import scratchlib.objects.ScratchObject;
import scratchlib.objects.fixed.collections.ScratchObjectAbstractCollection;
import scratchlib.objects.fixed.collections.ScratchObjectArray;
import scratchlib.objects.fixed.data.ScratchObjectString;
import scratchlib.objects.fixed.data.ScratchObjectSymbol;
import scratchlib.objects.fixed.data.ScratchObjectUtf8;
import scratchlib.objects.user.ScratchObjectVariableFrame;
import structogram2byob.ScratchType;
import structogram2byob.VariableContext;
import structogram2byob.VariableContext.UnitSpecific;
import structogram2byob.blocks.Block;
import structogram2byob.blocks.BlockDescription;
import structogram2byob.blocks.BlockRegistry;
import structogram2byob.blocks.special.ScriptVariablesBlock;
import structogram2byob.program.expressions.BlockExpression;
import structogram2byob.program.expressions.Expression;


/**
 * A "program unit" is either a script or an invocable method that is part of a
 * {@link Program} and can be converted to a Scratch array of its blocks.
 */
public class ProgramUnit
{
    private final UnitType type;
    private final BlockDescription description;
    private final List<BlockExpression> blocks;
    private final Block unitBlock;

    /**
     * Constructs a new unit from the given header description and the given
     * blocks.
     * 
     * @param type The type of this unit.
     * @param description The unit description.
     * @param blocks The blocks that form the unit body.
     */
    public ProgramUnit(UnitType type, BlockDescription description,
            Collection<? extends BlockExpression> blocks)
    {
        this.type = type;
        this.description = description;
        this.blocks = Collections.unmodifiableList(new ArrayList<>(blocks));

        this.unitBlock = new UnitBlock(description, ScratchType.ANY);
    }

    /**
     * @return Whether this unit is a script, command, reporter or predicate.
     */
    public UnitType getType()
    {
        return type;
    }

    /**
     * @return The blocks that form the unit body.
     */
    public List<BlockExpression> getBlocks()
    {
        return blocks;
    }

    /**
     * @return This unit's user spec, for conversion to a custom block.
     */
    public String getUserSpec()
    {
        return description.toUserSpec();
    }

    /**
     * @return A {@link Block} instance for invoking this as a custom block.
     */
    public Block getInvocationBlock()
    {
        return unitBlock;
    }

    /**
     * Converts this unit into an array of its blocks, given a map of variables
     * and a block registry to distinguish ambiguous parts.
     * 
     * @param vars A map of variable names to {@link VariableContext}s.
     * @param blocks The available blocks, including all custom blocks.
     * @return A {@link ScratchObject}.
     */
    public ScratchObjectAbstractCollection toScratch(
            Map<String, VariableContext> vars, BlockRegistry blocks)
    {
        ScratchObjectArray a = new ScratchObjectArray();

        UnitSpecific thisContext = new UnitSpecific(this);
        Map<String, VariableContext> newVars = new HashMap<>(vars);
        for (int i = 0, n = description.countParts(); i < n; ++i) {
            if (description.isParameter(i)) {
                newVars.put(description.getLabel(i), thisContext);
            }
        }
        newVars = Collections.unmodifiableMap(newVars);

        if (type == UnitType.SCRIPT) {
            Block hat = blocks.lookup(description);
            a.add(hat.toScratch(Arrays.asList(), newVars, blocks));
        }

        for (BlockExpression block : this.blocks) {

            ScratchObject obj = block.toScratch(newVars, blocks);
            a.add(obj);

            // check whether the block is a "script variables" block
            if (ScriptVariablesBlock.instance.getDescription()
                    .isAssignableFrom(block.getDescription())) {

                Map<String, ScratchObjectVariableFrame> frames = ScriptVariablesBlock
                        .retrieveFrames((ScratchObjectArray) obj);

                // extend set of available variables
                newVars = new HashMap<>(newVars);
                for (Expression param : block.getParameters()) {

                    BlockExpression pblock = (BlockExpression) param;
                    String varName = pblock.getDescription().getLabel(0);

                    ScratchObjectVariableFrame f = frames.get(varName);
                    newVars.put(varName, new VariableContext.ScriptSpecific(f));

                }
                newVars = Collections.unmodifiableMap(newVars);

            }

        }

        return a;
    }

    /**
     * Specifies a unit invocation block.
     */
    private static class UnitBlock extends Block
    {
        public UnitBlock(BlockDescription desc, ScratchType returnValue)
        {
            super(desc, returnValue);
        }

        @Override
        public ScratchObjectArray toScratch(List<Expression> params,
                Map<String, VariableContext> vars, BlockRegistry blocks)
        {
            ScratchObjectArray a = new ScratchObjectArray();

            a.add(new ScratchObjectSymbol("byob"));
            a.add(new ScratchObjectString(""));
            a.add(new ScratchObjectSymbol("doCustomBlock"));

            a.add(new ScratchObjectUtf8(getDescription().toUserSpec()));

            for (Expression param : params) {
                a.add(param.toScratch(vars, blocks));
            }

            return a;
        }
    }
}
