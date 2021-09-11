import java.util.List;

public class Tricky<R> {

    public void badFunc1() {
    }

    public void badFunc2() {
    }

    private void badPrivate() {
    }

    public static String staticFunc() {
        return "I am static";
    }

    protected synchronized <T> List<T> scary(R r, Integer... args) {
        return null;
    }

    public static class Nested {
        public int nestedFunc() {
            return 0;
        }
    }
}
