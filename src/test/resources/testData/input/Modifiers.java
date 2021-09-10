public abstract class Modifiers {

    static void staticFunc() {
        System.out.println("I am static");
    }

    abstract void abstractFunc();

    final void finalFunc() {
    }

    synchronized void synchronizedFunc() {
    }

    native void nativeFunc();

    strictfp void strictfpFunc() {
    }
}
