public interface ModifiersInterface {

    static void staticFunc() {
        System.out.println("I am static");
    }

    void abstractFunc();

    void finalFunc();

    void synchronizedFunc();

    void nativeFunc();

    strictfp void strictfpFunc();
}
