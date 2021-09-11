# extract-interface [![Build](https://github.com/psurkov/extract-interface/actions/workflows/build.yml/badge.svg)](https://github.com/psurkov/extract-interface/actions/workflows/build.yml)
Утилита для выноса java-интерфейса из java-класса
### Запуск
```bash
./gradlew run --args="аргументы и опции"
```
Аргументы устроены следующим образом:
```bash
Arguments: 
    inputFile -> Input file { String }
Options: 
    --className, -c -> Class name { String }
    --whitelist, -w -> Whitelist { String }
    --blacklist, -b -> Blacklist { String }
    --visibility, -v [PUBLIC] -> Visibility { Value should be one of [private, protected, internal, public] }
    --outputInterfaceName, -i -> Output interface name { String }
    --outputFile, -o -> Output file { String }
    --help, -h -> Usage info
```
### Пример
Имеем файл `Tricky.java` с содержимым
```java
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
```
Вызываем
```bash
./gradlew run --args="Tricky.java -b badFunc1 -b badFunc2 -v public -v protected"
```
Рядом появляется файл `TrickyInterface.java` с содержимым
```java
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
```
А можно было указать `--className Tricky.Nested`, чтобы интерфейс выносился из него