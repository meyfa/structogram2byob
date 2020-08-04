package structogram2byob.program;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nsdlib.elements.NSDRoot;
import scratchlib.objects.ScratchObject;
import scratchlib.objects.fixed.collections.ScratchObjectAbstractCollection;
import scratchlib.objects.fixed.collections.ScratchObjectArray;
import scratchlib.objects.fixed.data.ScratchObjectString;
import scratchlib.objects.fixed.data.ScratchObjectSymbol;
import scratchlib.objects.fixed.data.ScratchObjectUtf8;
import scratchlib.objects.user.ScratchObjectVariableFrame;
import structogram2byob.ScratchType;
import structogram2byob.blocks.Block;
import structogram2byob.blocks.BlockDescription;
import structogram2byob.blocks.BlockRegistry;
import structogram2byob.blocks.special.ScriptVariablesBlock;
import structogram2byob.program.expressions.BlockExpression;
import structogram2byob.program.expressions.Expression;


/**
 * A "program unit" is either a script or an invokable method that is part of a
 * {@link Program} and can be converted to a Scratch array of its blocks.
 */
public class ProgramUnit
{
    private final NSDRoot element;
    private final UnitType type;
    private final BlockDescription description;
    private final List<BlockExpression> blocks;
    private final Block unitBlock;

    private final VariableContext thisContext = VariableContext.getForUnit(this);

    /**
     * Constructs a new unit from the given header description and the given blocks.
     *
     * @param element The element this unit was constructed from.
     * @param type The type of this unit.
     * @param description The unit description.
     * @param blocks The blocks that form the unit body.
     */
    public ProgramUnit(NSDRoot element, UnitType type, BlockDescription description,
            Collection<? extends BlockExpression> blocks)
    {
        this.element = element;

        this.type = type;
        this.description = description;
        this.blocks = Collections.unmodifiableList(new ArrayList<>(blocks));

        this.unitBlock = new UnitBlock(description, ScratchType.ANY);
    }

    /**
     * @return The element this unit was constructed from.
     */
    public NSDRoot getElement()
    {
        return element;
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
     *
     * @throws ScratchConversionException When the conversion fails.
     */
    public ScratchObjectAbstractCollection toScratch(VariableMap vars, BlockRegistry blocks)
            throws ScratchConversionException
    {
        ScratchObjectArray arr = new ScratchObjectArray();

        // add parameters to available variables
        vars = vars.combine(getParameterVariables());

        if (type == UnitType.SCRIPT) {
            // create hat block
            Block hat = blocks.lookup(description);
            if (hat == null) {
                throw new ScratchConversionException(element, "unknown block: " + description);
            }
            arr.add(hat.toScratch(Collections.emptyList(), vars, blocks));
        }

        // add script body
        for (BlockExpression block : this.blocks) {
            ScratchObject obj = block.toScratch(vars, blocks);
            arr.add(obj);

            // check whether the block is a "script variables" block
            if (ScriptVariablesBlock.instance.getDescription().isAssignableFrom(block.getDescription())) {
                // extend available variables
                vars = vars.combine(retrieveScriptVariables(block, obj));
            }
        }

        return arr;
    }

    /**
     * Obtains this unit's parameters as a variable map for use in script
     * serialization.
     *
     * @return A variable map.
     */
    private VariableMap getParameterVariables()
    {
        Map<String, VariableContext> variables = new HashMap<>();
        for (int i = 0, n = description.countParts(); i < n; ++i) {
            if (description.isParameter(i)) {
                variables.put(description.getLabel(i), thisContext);
            }
        }
        return new VariableMap(variables);
    }

    /**
     * Given a "script variables" block and its object representation, retrieves
     * all the variable definitions.
     *
     * @param block The "script variables" block.
     * @param obj The block's Scratch object conversion result.
     * @return A variable map.
     */
    private VariableMap retrieveScriptVariables(BlockExpression block, ScratchObject obj)
    {
        Map<String, VariableContext> variables = new HashMap<>();

        // retrieve the frames for context construction
        Map<String, ScratchObjectVariableFrame> frames = ScriptVariablesBlock.retrieveFrames((ScratchObjectArray) obj);

        for (Expression param : block.getParameters()) {
            // get the variable name and corresponding frame
            BlockExpression pblock = (BlockExpression) param;
            String varName = pblock.getDescription().getLabel(0);
            ScratchObjectVariableFrame frame = frames.get(varName);

            variables.put(varName, VariableContext.getForScript(frame));
        }

        return new VariableMap(variables);
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
        public ScratchObjectArray toScratch(List<Expression> params, VariableMap vars, BlockRegistry blocks)
                throws ScratchConversionException
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
