package structogram2byob.blocks.special;

import java.util.List;

import scratchlib.objects.fixed.collections.ScratchObjectArray;
import scratchlib.objects.fixed.data.ScratchObjectString;
import scratchlib.objects.fixed.data.ScratchObjectSymbol;
import scratchlib.objects.fixed.dimensions.ScratchObjectRectangle;
import scratchlib.objects.user.morphs.ScratchObjectListMorph;
import structogram2byob.ScratchType;
import structogram2byob.blocks.Block;
import structogram2byob.blocks.BlockDescription;
import structogram2byob.blocks.BlockRegistry;
import structogram2byob.program.ScratchConversionException;
import structogram2byob.program.VariableMap;
import structogram2byob.program.expressions.Expression;


/**
 * This is the "list (any...)" block.
 */
public class ListBlock extends Block
{
    private static final BlockDescription description = new BlockDescription.Builder()
            .label("list").paramList(ScratchType.ANY).build();

    /**
     * The singleton instance.
     */
    public static final ListBlock instance = new ListBlock();

    private ListBlock()
    {
        super(description, null);
    }

    @Override
    public ScratchObjectArray toScratch(List<Expression> params, VariableMap vars, BlockRegistry blocks)
            throws ScratchConversionException
    {
        ScratchObjectArray a = new ScratchObjectArray();

        a.add(new ScratchObjectSymbol("byob"));
        a.add(new ScratchObjectString(""));
        a.add(new ScratchObjectSymbol("newList:"));

        ScratchObjectListMorph list = new ScratchObjectListMorph();
        list.setField(ScratchObjectListMorph.FIELD_BOUNDS, new ScratchObjectRectangle(0, 0, 95, 115));

        ScratchObjectArray cellMorphs = new ScratchObjectArray();
        list.setField(ScratchObjectListMorph.FIELD_CELL_MORPHS, cellMorphs);

        for (Expression param : params) {
            cellMorphs.add(param.toScratch(vars, blocks));
        }

        a.add(list);

        return a;
    }
}
