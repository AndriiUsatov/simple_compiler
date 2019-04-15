package andrii.app.univ.syntax.analyzer;

import andrii.app.univ.entity.error.TranslateError;
import andrii.app.univ.entity.lexema.Lexema;

import java.util.List;

public interface Analyzer {

    List<TranslateError> detectErrors(List<Lexema> lexemas);
}
