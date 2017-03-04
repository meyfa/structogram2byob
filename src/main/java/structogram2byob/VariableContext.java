package structogram2byob;

import structogram2byob.program.ProgramUnit;


/**
 * Instances of this class describe where a certain variable is situated, which
 * is necessary information for its accessors.
 */
public class VariableContext
{
    /**
     * Marks a variable as globally available.
     */
    public static final VariableContext GLOBAL = new VariableContext();

    private VariableContext()
    {
    }

    /**
     * Marks a variable as a unit parameter.
     */
    public static class UnitSpecific extends VariableContext
    {
        private final ProgramUnit unit;

        /**
         * @param unit The unit the variable belongs to.
         */
        public UnitSpecific(ProgramUnit unit)
        {
            this.unit = unit;
        }

        /**
         * @return The unit the variable belongs to.
         */
        public ProgramUnit getUnit()
        {
            return unit;
        }
    }
}
