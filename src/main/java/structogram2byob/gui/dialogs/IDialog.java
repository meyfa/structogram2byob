package structogram2byob.gui.dialogs;

/**
 * Represents a modal dialog that requests a value from the user.
 *
 * @param <T> The type of value.
 */
public interface IDialog<T>
{
    /**
     * Opens this dialog and obtains a result value from the user.
     *
     * @return The result value.
     */
    T show();
}
