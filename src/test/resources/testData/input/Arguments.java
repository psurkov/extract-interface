import java.util.List;

class Arguments {

    void primitive(int x) {
    }

    void string(String s) {
    }

    void array(int[] a) {
    }

    void list(List<Integer> l) {
    }

    void wildcard(List<?> l) {
    }

    void wildcardExtends(List<? extends Integer> l) {
    }

    void wildcardSupper(List<? super Integer> l) {
    }

    <T> void generic(T t) {
    }

    void varargs(int... args) {
    }

    void arg0() {
    }

    void arg1(int a) {
    }

    void arg2(int a, int b) {
    }

    void arg3(int a, int b, int c) {
    }

    void arg4(int a, int b, int c, int d) {
    }

    void arg5(int a, int b, int c, int d, int e) {
    }
}
