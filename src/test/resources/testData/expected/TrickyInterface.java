import java.util.List;

public interface TrickyInterface<R> {

    static String staticFunc() {
        return "I am static";
    }

    <T> List<T> scary(R r, Integer... args);

    public static class Nested {

        public int nestedFunc() {
            return 0;
        }
    }
}
