package andrii.app.univ;

import andrii.app.univ.entity.error.TranslateError;
import andrii.app.univ.entity.expr.Executable;
import andrii.app.univ.entity.lexema.Lexema;
import andrii.app.univ.entity.lexema.LexemaClass;
import andrii.app.univ.exception.ParseException;
import andrii.app.univ.exe.Executor;
import andrii.app.univ.lexical.LexicalAnalyzer;
import andrii.app.univ.lexical.recognizer.LexemaRecognizer;
import andrii.app.univ.lexical.recognizer.impl.DoubleVariableRecognizer;
import andrii.app.univ.lexical.recognizer.impl.IdentifierRecognizer;
import andrii.app.univ.lexical.recognizer.impl.IntegerVariableRecognizer;
import andrii.app.univ.lexical.recognizer.impl.StaticValueLexemaRecognizer;
import andrii.app.univ.rpe.RPEBuilder;
import andrii.app.univ.semantic.SemanticAnalyzer;
import andrii.app.univ.syntax.SyntaxAnalyzer;
import andrii.app.univ.util.SourceScrapper;
import andrii.app.univ.util.printer.Serializer;
import andrii.app.univ.util.printer.ToExcelPrinter;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Translator {

    public static void main(String[] args) throws IOException {
        boolean debug = false;

        PrintStream origin = System.out;
        if (!debug) {
            System.setOut(new PrintStream(new ByteArrayOutputStream()));
        }

        String sources = SourceScrapper.getSources(args);

        List<Lexema> result = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        List<Executable> executables = new ArrayList<>();

        try {
            result = new LexicalAnalyzer(recognizers()).recognizeText(sources);
        } catch (ParseException e) {
            e.printStackTrace();
            errors.add(e.getMessage());
        }
        serialize(result, errors);

        Map results = deserialize();
        errors = results.get("err") == null ? new ArrayList<>() : (List<String>) results.get("err");
        result = results.get("lex") == null ? new ArrayList<>() : (List<Lexema>) results.get("lex");

        try {
            errors.addAll(new SyntaxAnalyzer().detectErrors(result).stream().map(TranslateError::getText).collect(Collectors.toList()));
        } catch (Exception e) {
            errors.add(e.getMessage());
            e.printStackTrace();
        }
        try {
            errors.addAll(new SemanticAnalyzer().analyze(result).stream().map(TranslateError::getText).collect(Collectors.toList()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            executables = new RPEBuilder().toReverseExp(RPEBuilder.normalizedLexemas(result));
        } catch (Exception e) {}
/*
                for (int i = 0; i < executables.size(); i++) {
                    System.out.println("----------------------------------");
                    System.out.println("Expression #" + (i + 1));
                    executables.get(i).getConstructItems().forEach(System.out::println);
                    System.out.println("----------------------------------");
                }
*/
        ToExcelPrinter.printMap(result.stream().map(Lexema::toMap).collect(Collectors.toList()), errors, executables);

        System.setOut(origin);

        if (errors.isEmpty()) {
            Executor.exe(executables, result);
        }

    }



    public static List<LexemaRecognizer> recognizers() {
        List<LexemaRecognizer> recognizers = new ArrayList<>();
        Stream.of(LexemaClass.class.getEnumConstants())
                .filter(lexemaClass -> lexemaClass.getRepresentaion() != null)
                .forEach(lexemaClass -> recognizers.add(new StaticValueLexemaRecognizer(lexemaClass)));
        recognizers.addAll(Arrays.asList(new DoubleVariableRecognizer(), new IdentifierRecognizer(), new IntegerVariableRecognizer()));
        return recognizers;
    }

    private static void serialize(List<Lexema> lexemas, List<String> errors) {
        Map toSerialize = new HashMap();
        toSerialize.put("lex", lexemas);
        toSerialize.put("err", errors);
        Serializer.serialize(toSerialize);
    }

    private static Map deserialize() {
        return Serializer.deserialize();
    }

}
