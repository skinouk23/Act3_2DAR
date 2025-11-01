package Process;

import java.io.Serializable;

public class Operation implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private double operand1;
    private double operand2;
    private char operator;
    private double result;
    private int operationId;

    public Operation(double operand1, double operand2, char operator) {
        this.operand1 = operand1;
        this.operand2 = operand2;
        this.operator = operator;
    }

    // Getters et Setters
    public double getOperand1() { return operand1; }
    public void setOperand1(double operand1) { this.operand1 = operand1; }
    
    public double getOperand2() { return operand2; }
    public void setOperand2(double operand2) { this.operand2 = operand2; }
    
    public char getOperator() { return operator; }
    public void setOperator(char operator) { this.operator = operator; }
    
    public double getResult() { return result; }
    public void setResult(double result) { this.result = result; }
    
    public int getOperationId() { return operationId; }
    public void setOperationId(int operationId) { this.operationId = operationId; }

    public void calculate() {
        switch (operator) {
            case '+':
                result = operand1 + operand2;
                break;
            case '-':
                result = operand1 - operand2;
                break;
            case '*':
                result = operand1 * operand2;
                break;
            case '/':
                if (operand2 != 0) {
                    result = operand1 / operand2;
                } else {
                    throw new ArithmeticException("Division par zéro");
                }
                break;
            default:
                throw new IllegalArgumentException("Opérateur non supporté: " + operator);
        }
    }

    @Override
    public String toString() {
        return operand1 + " " + operator + " " + operand2 + " = " + result + " (ID: " + operationId + ")";
    }
}