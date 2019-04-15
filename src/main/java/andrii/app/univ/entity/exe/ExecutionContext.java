package andrii.app.univ.entity.exe;

import andrii.app.univ.entity.Variable;
import andrii.app.univ.entity.lexema.Lexema;
import andrii.app.univ.entity.lexema.LexemaClass;
import andrii.app.univ.util.LexHelper;

import java.util.List;
import java.util.stream.Collectors;

public class ExecutionContext {

    private List<Lexema> lexemaList;
    private List<Variable> variables;


    public ExecutionContext(List<Lexema> lexemaList) {
        this.lexemaList = lexemaList;
        init();
    }

    private void init() {
            variables =
                    lexemaList
                    .stream()
                    .filter(lexema -> lexema.getLexemaClass() == LexemaClass.Identifier && lexema.getIndex() != null)
                    .map(Lexema::getIndex)
                    .distinct()
                    .map(idx -> lexemaList.stream().filter(lx ->  idx.equals(lx.getIndex()) && lx.getLexemaClass() == LexemaClass.Identifier).findFirst().get())
                    .map(lexema -> new Variable(lexema, LexHelper.identifierType(lexema, lexemaList)))
                    .collect(Collectors.toList());
    }

    public Variable getVariableByIdx(int idx) {
        return variables.stream().filter(variable -> variable.getVariableIdentifier().getIndex() == idx).findFirst().orElseGet(null);
    }

    public Variable getVariableByName(String name) {
        return variables.stream().filter(variable -> variable.getVariableIdentifier().getValue().equals(name)).findFirst().orElseGet(null);
    }


}
