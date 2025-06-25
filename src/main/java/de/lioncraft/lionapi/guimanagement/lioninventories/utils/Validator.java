package de.lioncraft.lionapi.guimanagement.lioninventories.utils;

import java.lang.reflect.Field;

public class Validator {

    /**
     * Validates an object's fields that are annotated with @NumberRange.
     *
     * @param obj The object to validate.
     * @throws IllegalAccessException If a field is inaccessible.
     * @throws IllegalArgumentException If a validation rule is violated.
     */
    public static void validate(Object obj) throws IllegalAccessException {
        // Get all fields declared in the object's class
        for (Field field : obj.getClass().getDeclaredFields()) {

            // Check if the field has the @NumberRange annotation
            if (field.isAnnotationPresent(NumberRange.class)) {

                // Get the annotation instance from the field
                NumberRange range = field.getAnnotation(NumberRange.class);

                // Make the private field accessible for reading its value
                field.setAccessible(true);

                Object value = field.get(obj);

                // We assume the field is a number. Check if its value is valid.
                if (value instanceof Number) {
                    double numberValue = ((Number) value).doubleValue();

                    if (numberValue < range.min() || numberValue > range.max()) {
                        // If validation fails, throw an exception with the custom message
                        throw new IllegalArgumentException(
                                "Validation failed for field '" + field.getName() + "': " + range.message()
                        );
                    }
                }
            }
        }
    }
}