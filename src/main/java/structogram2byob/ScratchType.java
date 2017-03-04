package structogram2byob;

/**
 * Specifies all types that Scratch/BYOB supports as block inputs.
 */
public enum ScratchType
{
    /**
     * Anything is supported.
     */
    ANY,

    /**
     * Sprite, stage, ...
     */
    OBJECT,

    /**
     * Integer or floating-point number.
     */
    NUMBER,

    /**
     * String / textual value.
     */
    TEXT,

    /**
     * A boolean value.
     */
    BOOLEAN,

    /**
     * A list object.
     */
    LIST,

    /**
     * Inline block.
     */
    COMMAND,

    /**
     * Script (list of blocks).
     */
    LOOP,

    /**
     * A reporter block.
     */
    REPORTER,

    /**
     * A predicate block.
     */
    PREDICATE,

    /**
     * An unevaluated value.
     */
    UNEVALUATED,

    /**
     * An unevaluated boolean value.
     */
    UNEVALUATED_BOOLEAN;

    /**
     * Checks whether a value of the given type would be supported for a
     * parameter of the type specified by this instance.
     * 
     * @param o The type to check assignability for.
     * @return Whether the given type can be assigned to a field of this type.
     */
    public boolean isAssignableFrom(ScratchType o)
    {
        if (this == ANY || o == ANY) {
            return true;
        }
        return this == o;
    }
}
