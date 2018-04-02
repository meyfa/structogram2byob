package structogram2byob.program;

import scratchlib.objects.user.ScratchObjectVariableFrame;


/**
 * Instances of this class describe where a certain variable is situated, which
 * is necessary information for its accessors.
 *
 * <p>
 * There are three types of contexts:
 *
 * <ul>
 * <li>{@link #GLOBAL} means a variable is available globally;
 * <li>{@link ScriptSpecific} means a variable is a script variable;
 * <li>{@link UnitSpecific} can be instantiated with a specific unit (that is
 * not a script) to mark a variable as a parameter of that unit.
 * </ul>
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
    public static class ScriptSpecific extends VariableContext
    {
        private final ScratchObjectVariableFrame frame;

        /**
         * @param frame The frame the variable belongs to.
         */
        public ScriptSpecific(ScratchObjectVariableFrame frame)
        {
            this.frame = frame;
        }

        /**
         * @return The frame the variable belongs to.
         */
        public ScratchObjectVariableFrame getFrame()
        {
            return frame;
        }
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
