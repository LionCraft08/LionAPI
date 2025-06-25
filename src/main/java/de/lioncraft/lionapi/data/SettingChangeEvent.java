package de.lioncraft.lionapi.data;

@FunctionalInterface
public interface SettingChangeEvent<T> {
    /**A Function executed every time the Value of a Setting changes.
     * @param newValue the new value
     * @return whether to apply changes (should be true in most cases)
     */
    T onChange(T oldValue, T newValue);
}
