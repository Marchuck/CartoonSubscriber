package pl.marczak.cartoonsubscriber.utils;

import rx.Observable;

/**
 * @author Lukasz Marczak
 * @since 23.06.16.
 */
public class Test {
    public static void main(String[] args) {
        Observable.range(1, 100).map(i -> String.valueOf(i).concat(
                (i % 15 == 0 ? "FizzBuzz" : i % 3 == 0 ? "Fizz" : i % 5 == 0 ? "Buzz" : "")))
                .subscribe(System.out::println);
    }
}
