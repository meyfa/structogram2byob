package structogram2byob.program;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import scratchlib.objects.ScratchObject;
import scratchlib.objects.fixed.collections.ScratchObjectArray;
import structogram2byob.VariableContext;
import structogram2byob.VariableContext.UnitSpecific;
import structogram2byob.blocks.BlockDescription;
import structogram2byob.blocks.BlockRegistry;
import structogram2byob.program.expressions.BlockExpression;


/**
 * A "program unit" is either a script or an invocable method that is part of a
 * {@link Program} and can be converted to a Scratch array of its blocks.
 */
public class ProgramUnit
{
    private final UnitType type;
    private final BlockDescription description;
    private final List<BlockExpression> blocks;

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
    }

    /**
     * @return Whether this unit is a script, command, reporter or predicate.
     */
    public UnitType getType()
    {
        return type;
    }

    /**
     * @return This unit's user spec, for conversion to a custom block.
     */
    public String getUserSpec()
    {
        return description.toUserSpec();
    }

    /**
     * Converts this unit into an array of its blocks, given a map of variables
     * and a block registry to distinguish ambiguous parts.
     * 
     * @param vars A map of variable names to {@link VariableContext}s.
     * @param blocks The available blocks, including all custom blocks.
     * @return A {@link ScratchObject}.
     */
    public ScratchObject toScratch(Map<String, VariableContext> vars,
            BlockRegistry blocks)
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

        for (BlockExpression block : this.blocks) {

            ScratchObject obj = block.toScratch(newVars, blocks);
            a.add(obj);

        }

        return a;
    }
}
