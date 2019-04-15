package andrii.app.univ.entity;

import andrii.app.univ.entity.lexema.Lexema;
import andrii.app.univ.entity.lexema.LexemaClass;

public class Variable {

    private Lexema variableIdentifier;
    private LexemaClass variableType;
    private Lexema variableValue;

    public Variable(Lexema variableIdentifier, LexemaClass variableType) {
        this.variableIdentifier = variableIdentifier;
        this.variableType = variableType;
    }

    public Lexema getVariableIdentifier() {
        return variableIdentifier;
    }

    public LexemaClass getVariableType() {
        return variableType;
    }

    public Lexema getVariableValue() {
        return variableValue;
    }

    public void setVariableValue(Lexema variableValue) {
        this.variableValue = variableValue;
    }

    @Override
    public String toString() {
        return "Variable{" +
                "variableValue=" + variableValue +
                '}';
    }
}
