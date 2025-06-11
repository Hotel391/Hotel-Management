/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utility;

import java.util.function.Predicate;

/**
 *
 * @author HieuTT
 */
public class ValidationRule<T> {
    private final Predicate<T> condition;
    private final String errorMessage;
    
    public ValidationRule(Predicate<T> condition, String errorMessage) {
        this.condition = condition;
        this.errorMessage = errorMessage;
    }

    public boolean isValid(T value) {
        return condition.test(value);
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
