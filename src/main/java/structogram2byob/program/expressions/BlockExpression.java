package structogram2byob.program.expressions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import scratchlib.objects.ScratchObject;
import scratchlib.objects.fixed.collections.ScratchObjectArray;
import scratchlib.objects.fixed.data.ScratchObjectString;
import scratchlib.objects.fixed.data.ScratchObjectSymbol;
import scratchlib.objects.fixed.data.ScratchObjectUtf8;
import structogram2byob.ScratchType;
import structogram2byob.blocks.Block;
import structogram2byob.blocks.BlockDescription;
import structogram2byob.blocks.BlockRegistry;
import structogram2byob.program.ProgramUnit;
import structogram2byob.program.VariableContext;
import structogram2byob.program.VariableContext.ScriptSpecific;
import structogram2byob.program.VariableContext.UnitSpecific;


/**
 * Expression type for block invocations, including calls to variables.
 */
public class BlockExpression extends Expression
{
    private final BlockDescription description;
    private final List<Expression> parameters;

    /**
     * Constructs a new expression with the given description, and the given
     * parameters to substitute into the description.
     *
     * @param description The block description.
     * @param params The block parameters.
     */
    public BlockExpression(BlockDescription description,
            Collection<? extends Expression> params)
    {
        this.description = description;
        this.parameters = Collections.unmodifiableList(new ArrayList<>(params));
    }

    /**
     * @return The underlying block description.
     */
    public BlockDescription getDescription()
    {
        return description;
    }

    /**
     * @return The parameters the block is called with.
     */
    public List<Expression> getParameters()
    {
        return parameters;
    }

    @Override
    public ScratchType getType()
    {
        return ScratchType.ANY;
    }

    @Override
    public ScratchObject toScratch(Map<String, VariableContext> vars,
            BlockRegistry blocks)
    {
        // check if this is a variable
        if (description.countParts() == 1 && !description.isParameter(0)) {
            String varName = description.getLabel(0);
            VariableContext var = vars.get(varName);
            if (var != null) {
                // this is a variable
                return serializeAsVariable(var);
            }
        }

        // serialize as block
        Block b = blocks.lookup(description);
        if (b == null) {
            throw new IllegalArgumentException("unknown block: " + description);
        }

        return b.toScratch(parameters, vars, blocks);
    }

    private ScratchObject serializeAsVariable(VariableContext ctx)
    {
        String name = description.getLabel(0);

        ScratchObjectArray a = new ScratchObjectArray();

        if (ctx == VariableContext.GLOBAL) {

            a.add(new ScratchObjectSymbol("readVariable"));
            a.add(new ScratchObjectUtf8(name));

        } else if (ctx instanceof VariableContext.ScriptSpecific) {

            a.add(new ScratchObjectSymbol("byob"));
            a.add(new ScratchObjectString(""));
            a.add(new ScratchObjectSymbol("readBlockVariable"));

            a.add(new ScratchObjectUtf8(name));

            a.add(((ScriptSpecific) ctx).getFrame());

        } else if (ctx instanceof VariableContext.UnitSpecific) {

            ProgramUnit unit = ((UnitSpecific) ctx).getUnit();

            a.add(new ScratchObjectSymbol("byob"));
            a.add(new ScratchObjectString(""));
            a.add(new ScratchObjectSymbol("readBlockVariable"));

            a.add(new ScratchObjectUtf8(name));

            a.add(new ScratchObjectUtf8(unit.getUserSpec()));

        }

        return a;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        sb.append('(');
        for (int i = 0, n = description.countParts(), pi = 0; i < n; ++i) {
            if (i > 0) {
                sb.append(' ');
            }
            if (description.isParameter(i)) {
                sb.append(parameters.get(pi++));
            } else {
                sb.append(description.getLabel(i));
            }
        }
        sb.append(')');

        return sb.toString();
    }
}
