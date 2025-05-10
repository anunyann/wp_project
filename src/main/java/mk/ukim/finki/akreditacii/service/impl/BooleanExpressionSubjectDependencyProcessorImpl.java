package mk.ukim.finki.akreditacii.service.impl;

import mk.ukim.finki.akreditacii.model.SubjectDependencyType;
import mk.ukim.finki.akreditacii.model.exceptions.InvalidDependencyException;
import mk.ukim.finki.akreditacii.model.exceptions.SubjectValidationException;
import mk.ukim.finki.akreditacii.repository.SubjectRepository;
import mk.ukim.finki.akreditacii.service.specifications.SubjectDependencyProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class BooleanExpressionSubjectDependencyProcessorImpl implements SubjectDependencyProcessor {

    @Autowired
    private SubjectRepository subjectRepository;

    @Override
    public boolean applicableTo(SubjectDependencyType type) {
        return SubjectDependencyType.BOOLEAN_EXPRESSION.equals(type);
    }

    @Override
    public void validate(String dependency) throws SubjectValidationException {
        if (dependency == null || dependency.trim().isEmpty()) {
            throw new SubjectValidationException("BOOLEAN_EXPRESSION dependency cannot be empty");
        }

        // Format validation: Should be in the format "BOOLEAN_EXPRESSION:expression"
        if (!dependency.startsWith("BOOLEAN_EXPRESSION:")) {
            throw new SubjectValidationException("Invalid BOOLEAN_EXPRESSION format: " + dependency);
        }

        String expression = dependency.substring("BOOLEAN_EXPRESSION:".length());

        // Basic validation for balanced parentheses
        if (!hasBalancedParentheses(expression)) {
            throw new SubjectValidationException("Unbalanced parentheses in expression: " + expression);
        }

        // Extract all subject codes from the expression
        Pattern codePattern = Pattern.compile("[A-Z0-9]{3,9}");
        Matcher matcher = codePattern.matcher(expression);

        boolean foundSubject = false;
        while (matcher.find()) {
            foundSubject = true;
            String code = matcher.group();

            if (!subjectRepository.existsById(code)) {
                throw new SubjectValidationException("Subject with code does not exist: " + code, code);
            }
        }

        if (!foundSubject) {
            throw new SubjectValidationException("No valid subject codes found in expression: " + expression);
        }

        // Additional validation could be added here to ensure the expression is syntactically correct
    }

    @Override
    public boolean isSatisfied(String dependency, List<String> subjectCodesPassedByStudent)
            throws InvalidDependencyException {
        if (dependency == null || dependency.trim().isEmpty()) {
            return true; // No BOOLEAN_EXPRESSION requirement
        }

        String expression = dependency.substring("BOOLEAN_EXPRESSION:".length());

        try {
            boolean result = evaluateExpression(expression, subjectCodesPassedByStudent);

            if (!result) {
                throw new InvalidDependencyException(
                        "BOOLEAN_EXPRESSION not satisfied",
                        "The requirement expression was not satisfied: " + expression
                );
            }

            return true;
        } catch (Exception e) {
            throw new InvalidDependencyException(
                    "Error evaluating expression",
                    "Failed to evaluate expression: " + e.getMessage()
            );
        }
    }

    private boolean hasBalancedParentheses(String expression) {
        Stack<Character> stack = new Stack<>();

        for (char c : expression.toCharArray()) {
            if (c == '(') {
                stack.push(c);
            } else if (c == ')') {
                if (stack.isEmpty() || stack.pop() != '(') {
                    return false;
                }
            }
        }

        return stack.isEmpty();
    }

    private boolean evaluateExpression(String expression, List<String> passedSubjects) {
        if (expression == null || expression.trim().isEmpty()) {
            return true;
        }

        // Step 1: Replace subject codes with their pass status (true/false)
        Pattern codePattern = Pattern.compile("[A-Z0-9]{3,9}");
        Matcher matcher = codePattern.matcher(expression);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            String code = matcher.group();
            boolean passed = passedSubjects.contains(code);
            matcher.appendReplacement(sb, String.valueOf(passed));
        }
        matcher.appendTail(sb);

        String boolExpression = sb.toString();

        // Step 2: Replace operators with Java-compatible ones
        boolExpression = boolExpression.replace("&&", " && ");
        boolExpression = boolExpression.replace("||", " || ");
        boolExpression = boolExpression.replace("~", "!");

        // Step 3: Parse and evaluate the expression
        try {
            return evaluateBooleanExpression(boolExpression);
        } catch (Exception e) {
            // Log the error and return false if there was a problem evaluating the expression
            System.err.println("Error evaluating boolean expression: " + e.getMessage());
            return false;
        }
    }

    /**
     * Evaluates a simple boolean expression.
     * This is a simplified implementation that handles basic expressions.
     *
     * @param expression The boolean expression to evaluate
     * @return The result of the evaluation
     */
    private boolean evaluateBooleanExpression(String expression) {
        // Strip any leading/trailing whitespace
        expression = expression.trim();

        // Handle simple true/false cases
        if (expression.equals("true")) return true;
        if (expression.equals("false")) return false;

        // Handle NOT operator
        if (expression.startsWith("!")) {
            return !evaluateBooleanExpression(expression.substring(1).trim());
        }

        // Handle parentheses
        if (expression.startsWith("(")) {
            // Find the matching closing parenthesis
            int depth = 1;
            int closeIndex = 1;
            while (depth > 0 && closeIndex < expression.length()) {
                char c = expression.charAt(closeIndex);
                if (c == '(') depth++;
                if (c == ')') depth--;
                closeIndex++;
            }

            // Extract and evaluate the inner expression
            String innerExpr = expression.substring(1, closeIndex - 1).trim();
            boolean innerResult = evaluateBooleanExpression(innerExpr);

            // If there's nothing after the closing parenthesis
            if (closeIndex >= expression.length()) {
                return innerResult;
            }

            // Get the rest of the expression after the closing parenthesis
            String rest = expression.substring(closeIndex).trim();

            // Handle operators
            if (rest.startsWith("&&")) {
                return innerResult && evaluateBooleanExpression(rest.substring(2).trim());
            } else if (rest.startsWith("||")) {
                return innerResult || evaluateBooleanExpression(rest.substring(2).trim());
            } else {
                // If there's something after the parenthesis but it's not an operator
                throw new IllegalArgumentException("Invalid expression syntax: " + expression);
            }
        }

        // Handle AND and OR operators
        // Find the first occurrence of && or || (not inside parentheses)
        int andIndex = findOperatorOutsideParentheses(expression, "&&");
        int orIndex = findOperatorOutsideParentheses(expression, "||");

        if (andIndex > 0) {
            String left = expression.substring(0, andIndex).trim();
            String right = expression.substring(andIndex + 2).trim();
            return evaluateBooleanExpression(left) && evaluateBooleanExpression(right);
        }

        if (orIndex > 0) {
            String left = expression.substring(0, orIndex).trim();
            String right = expression.substring(orIndex + 2).trim();
            return evaluateBooleanExpression(left) || evaluateBooleanExpression(right);
        }

        // If we reach here, the expression is invalid
        throw new IllegalArgumentException("Invalid boolean expression: " + expression);
    }

    /**
     * Finds the first occurrence of an operator outside of parentheses.
     *
     * @param expression The expression to search
     * @param operator The operator to find
     * @return The index of the operator, or -1 if not found
     */
    private int findOperatorOutsideParentheses(String expression, String operator) {
        int depth = 0;
        int index = 0;

        while (index <= expression.length() - operator.length()) {
            char c = expression.charAt(index);

            if (c == '(') {
                depth++;
            } else if (c == ')') {
                depth--;
            } else if (depth == 0 && expression.substring(index, index + operator.length()).equals(operator)) {
                return index;
            }

            index++;
        }

        return -1;
    }
}