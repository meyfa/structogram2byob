package structogram2byob.program;

import scratchlib.objects.ScratchObject;
import scratchlib.objects.fixed.data.ScratchObjectUtf8;
import scratchlib.objects.user.ScratchObjectVariableFrame;

import java.util.Objects;


/**
 * Instances of this class describe where a certain variable is situated, which
 * is necessary information for its accessors.
 *
 * <p>
 * There are three types of contexts:
 *
 * <ul>
 * <li>{@link #getGlobal()} means a variable is available globally;
 * <li>{@link #getForScript(ScratchObjectVariableFrame)} means a variable is a script variable;
 * <li>{@link #getForUnit(ProgramUnit)} can be instantiated with a specific unit (that is
 * not a script) to mark a variable as a parameter of that unit.
 * </ul>
 */
public abstract class VariableContext
{
    private static final VariableContext GLOBAL = new Global();

    /**
     * @return Whether this context is specific to BYOB and requires special handling.
     */
    public abstract boolean requiresBYOB();

    /**
     * When serializing variables with BYOB-specific types (anything except global),
     * a marker object must be appended for read/write actions.
     * <p>
     * This method returns the context's marker object for READ operations.
     * <p>
     * NOTE: The marker MUST NOT be appended when {@link #requiresBYOB()} returns false!
     * Do not call this method in that case, otherwise an exception will be thrown.
     *
     * @return The marker object for this context, for read operations.
     * @throws UnsupportedOperationException When this context is not BYOB-specific.
     */
    public abstract ScratchObject getReadMarker();

    /**
     * When serializing variables with BYOB-specific types (anything except global),
     * a marker object must be appended for read/write actions.
     * <p>
     * This method returns the context's marker object for WRITE operations.
     * <p>
     * NOTE: The marker MUST NOT be appended when {@link #requiresBYOB()} returns false!
     * Do not call this method in that case, otherwise an exception will be thrown.
     *
     * @return The marker object for this context, for write operations.
     * @throws UnsupportedOperationException When this context is not BYOB-specific.
     */
    public abstract ScratchObject getWriteMarker();

    /**
     * @return The global variable context.
     */
    public static VariableContext getGlobal()
    {
        return GLOBAL;
    }

    /**
     * @param frame The variable frame.
     * @return A "script variables"-specific variable context for the block referenced by the frame.
     */
    public static VariableContext getForScript(ScratchObjectVariableFrame frame)
    {
        Objects.requireNonNull(frame);
        return new ScriptSpecific(frame);
    }

    /**
     * @param unit The program unit.
     * @return A custom block-specific variable context for the given unit.
     */
    public static VariableContext getForUnit(ProgramUnit unit)
    {
        Objects.requireNonNull(unit);
        return new UnitSpecific(unit);
    }

    /**
     * Marks the variable as global.
     */
    private static class Global extends VariableContext
    {
        @Override
        public boolean requiresBYOB()
        {
            return false;
        }

        @Override
        public ScratchObject getReadMarker()
        {
            throw new UnsupportedOperationException("serializeForRead() called on global context");
        }

        @Override
        public ScratchObject getWriteMarker()
        {
            throw new UnsupportedOperationException("serializeForRead() called on global context");
        }
    }

    /**
     * Marks a variable as a script parameter.
     */
    private static class ScriptSpecific extends VariableContext
    {
        private final ScratchObjectVariableFrame frame;

        private ScriptSpecific(ScratchObjectVariableFrame frame)
        {
            this.frame = frame;
        }

        @Override
        public boolean requiresBYOB()
        {
            return true;
        }

        @Override
        public ScratchObject getReadMarker()
        {
            return frame;
        }

        @Override
        public ScratchObject getWriteMarker()
        {
            return frame;
        }
    }

    /**
     * Marks a variable as a unit parameter.
     */
    private static class UnitSpecific extends VariableContext
    {
        private final ProgramUnit unit;

        private UnitSpecific(ProgramUnit unit)
        {
            this.unit = unit;
        }

        @Override
        public boolean requiresBYOB()
        {
            return true;
        }

        @Override
        public ScratchObject getReadMarker()
        {
            return new ScratchObjectUtf8(unit.getUserSpec());
        }

        @Override
        public ScratchObject getWriteMarker()
        {
            return ScratchObject.NIL;
        }
    }
}
