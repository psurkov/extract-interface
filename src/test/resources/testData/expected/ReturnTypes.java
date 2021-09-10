import java.util.Collections;
import java.util.List;

public interface ReturnTypesInterface {

    int primitive();

    String string();

    int[] array();

    List<Integer> list();

    List<?> wildcard();

    List<? extends Integer> wildcardExtends();

    List<? super Integer> wildcardSupper();

    <T> T generic();
}
