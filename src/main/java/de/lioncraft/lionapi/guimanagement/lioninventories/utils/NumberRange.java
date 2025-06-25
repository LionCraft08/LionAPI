package de.lioncraft.lionapi.guimanagement.lioninventories.utils;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation to specify a valid numerical range for a field.
 * The validation logic will check if the field's value is between min() and max() (inclusive).
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface NumberRange {

    /**
     * The minimum value for the range (inclusive).
     */
    long min() default Long.MIN_VALUE;

    /**
     * The maximum value for the range (inclusive).
     */
    long max() default Long.MAX_VALUE;

    /**
     * A custom error message to be used if validation fails.
     */
    String message() default "Value is outside the allowed range.";
}