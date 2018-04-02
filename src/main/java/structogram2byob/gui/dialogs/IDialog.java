package structogram2byob.gui.dialogs;

/**
 * Represents a modal dialog that requests a value from the user.
 *
 * @param <T> The type of value.
 */
public interface IDialog<T>
{
    T show();
}
