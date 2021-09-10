import java.util.Collections;
import java.util.List;

public class ReturnTypes {

    int primitive() {
        return 0;
    }

    String string() {
        return "test";
    }

    int[] array() {
        return new int[0];
    }

    List<Integer> list() {
        return Collections.emptyList();
    }

    List<?> wildcard() {
        return Collections.emptyList();
    }

    List<? extends Integer> wildcardExtends() {
        return Collections.emptyList();
    }

    List<? super Integer> wildcardSupper() {
        return Collections.emptyList();
    }

    <T> T generic() {
        return null;
    }
}
