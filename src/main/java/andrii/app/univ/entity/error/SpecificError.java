package andrii.app.univ.entity.error;

import java.util.Objects;

public class SpecificError implements TranslateError {

    private String text;

    public SpecificError(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "Error{" +
                "text='" + text + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpecificError error = (SpecificError) o;
        return Objects.equals(text, error.text);
    }

    @Override
    public int hashCode() {

        return Objects.hash(text);
    }
}
