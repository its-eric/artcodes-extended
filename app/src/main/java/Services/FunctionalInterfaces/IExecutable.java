package Services.FunctionalInterfaces;

/**
 * Created by gxsha on 11/25/2018.
 */

@FunctionalInterface
public interface IExecutable<T> {
    void Execute(T result);
}
