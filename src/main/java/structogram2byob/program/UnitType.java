package structogram2byob.program;

import structogram2byob.ScratchType;


/**
 * Enum for describing a {@link ProgramUnit}'s type.
 */
public enum UnitType
{
    /**
     * A script unit not representable as a custom block.
     */
    SCRIPT(null),

    /**
     * A command block that returns nothing.
     */
    COMMAND(null),

    /**
     * A reporter block that can return any value.
     */
    REPORTER(ScratchType.ANY),

    /**
     * A predicate block that can return boolean values.
     */
    PREDICATE(ScratchType.BOOLEAN);

    private final ScratchType returnType;

    UnitType(ScratchType returnType)
    {
        this.returnType = returnType;
    }

    /**
     * Obtains the {@link ScratchType} that would be returned by a unit of this
     * type. The following values are possible:
     *
     * <ul>
     * <li>{@code SCRIPT}: {@code null}
     * <li>{@code COMMAND}: {@code null}
     * <li>{@code REPORTER}: {@link ScratchType#ANY}
     * <li>{@code PREDICATE}: {@link ScratchType#BOOLEAN}
     * </ul>
     *
     * @return The appropriate return type for this unit type.
     */
    public ScratchType getReturnType()
    {
        return returnType;
    }
}
