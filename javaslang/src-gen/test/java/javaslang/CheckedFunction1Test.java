/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang;

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
   G E N E R A T O R   C R A F T E D
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

import static org.assertj.core.api.Assertions.assertThat;

import java.security.MessageDigest;
import java.util.concurrent.atomic.AtomicInteger;
import javaslang.control.Try;
import org.junit.Test;

public class CheckedFunction1Test {

    @Test
    public void shouldCreateFromMethodReference() {
        class Type {
            Object methodReference(Object o1) {
                return null;
            }
        }
        final Type type = new Type();
        assertThat(CheckedFunction1.of(type::methodReference)).isNotNull();
    }

    @Test
    public void shouldLiftPartialFunction() {
        assertThat(CheckedFunction1.lift((o1) -> { while(true); })).isNotNull();
    }

    @Test
    public void shouldCreateIdentityFunction() throws Throwable {
        final CheckedFunction1<String, String> identity = CheckedFunction1.identity();
        final String s = "test";
        assertThat(identity.apply(s)).isEqualTo(s);
    }

    @Test
    public void shouldGetArity() {
        final CheckedFunction1<Object, Object> f = (o1) -> null;
        assertThat(f.arity()).isEqualTo(1);
    }

    @Test
    public void shouldConstant() throws Throwable {
        final CheckedFunction1<Object, Object> f = CheckedFunction1.constant(6);
        assertThat(f.apply(1)).isEqualTo(6);
    }

    @Test
    public void shouldCurry() {
        final CheckedFunction1<Object, Object> f = (o1) -> null;
        final CheckedFunction1<Object, Object> curried = f.curried();
        assertThat(curried).isNotNull();
    }

    @Test
    public void shouldTuple() {
        final CheckedFunction1<Object, Object> f = (o1) -> null;
        final CheckedFunction1<Tuple1<Object>, Object> tupled = f.tupled();
        assertThat(tupled).isNotNull();
    }

    @Test
    public void shouldReverse() {
        final CheckedFunction1<Object, Object> f = (o1) -> null;
        assertThat(f.reversed()).isNotNull();
    }

    @Test
    public void shouldMemoize() throws Throwable {
        final AtomicInteger integer = new AtomicInteger();
        final CheckedFunction1<Integer, Integer> f = (i1) -> i1 + integer.getAndIncrement();
        final CheckedFunction1<Integer, Integer> memo = f.memoized();
        // should apply f on first apply()
        final int expected = memo.apply(1);
        // should return memoized value of second apply()
        assertThat(memo.apply(1)).isEqualTo(expected);
        // should calculate new values when called subsequently with different parameters
        assertThat(memo.apply(2 )).isEqualTo(2  + 1);
        // should return memoized value of second apply() (for new value)
        assertThat(memo.apply(2 )).isEqualTo(2  + 1);
    }

    @Test
    public void shouldNotMemoizeAlreadyMemoizedFunction() throws Throwable {
        final CheckedFunction1<Integer, Integer> f = (i1) -> null;
        final CheckedFunction1<Integer, Integer> memo = f.memoized();
        assertThat(memo.memoized() == memo).isTrue();
    }

    @Test
    public void shouldMemoizeValueGivenNullArguments() throws Throwable {
        final CheckedFunction1<Integer, Integer> f = (i1) -> null;
        final CheckedFunction1<Integer, Integer> memo = f.memoized();
        assertThat(memo.apply(null)).isNull();
    }

    @Test
    public void shouldRecognizeMemoizedFunctions() {
        final CheckedFunction1<Integer, Integer> f = (i1) -> null;
        final CheckedFunction1<Integer, Integer> memo = f.memoized();
        assertThat(f.isMemoized()).isFalse();
        assertThat(memo.isMemoized()).isTrue();
    }

    private static final CheckedFunction1<String, MessageDigest> digest = (s1) -> MessageDigest.getInstance(s1);

    @Test
    public void shouldRecover() {
        Function1<String, MessageDigest> recover = digest.recover(throwable -> (s1) -> null);
        MessageDigest md5 = recover.apply("MD5");
        assertThat(md5).isNotNull();
        assertThat(md5.getAlgorithm()).isEqualToIgnoringCase("MD5");
        assertThat(md5.getDigestLength()).isEqualTo(16);
        assertThat(recover.apply("Unknown")).isNull();
    }

    @Test
    public void shouldRecoverNonNull() {
        Function1<String, MessageDigest> recover = digest.recover(throwable -> null);
        MessageDigest md5 = recover.apply("MD5");
        assertThat(md5).isNotNull();
        assertThat(md5.getAlgorithm()).isEqualToIgnoringCase("MD5");
        assertThat(md5.getDigestLength()).isEqualTo(16);
        Try<MessageDigest> unknown = Function1.liftTry(recover).apply("Unknown");
        assertThat(unknown).isNotNull();
        assertThat(unknown.isFailure()).isTrue();
        assertThat(unknown.getCause()).isNotNull().isInstanceOf(NullPointerException.class);
        assertThat(unknown.getCause().getMessage()).isNotEmpty().isEqualToIgnoringCase("recover return null for class java.security.NoSuchAlgorithmException: Unknown MessageDigest not available");
    }

    @Test
    public void shouldUncheckedWork() {
        Function1<String, MessageDigest> unchecked = digest.unchecked();
        MessageDigest md5 = unchecked.apply("MD5");
        assertThat(md5).isNotNull();
        assertThat(md5.getAlgorithm()).isEqualToIgnoringCase("MD5");
        assertThat(md5.getDigestLength()).isEqualTo(16);
    }

    @Test(expected = IllegalStateException.class)
    public void shouldUncheckedThrowIllegalState() {
        Function1<String, MessageDigest> unchecked = digest.unchecked();
        unchecked.apply("Unknown");
    }

    @Test
    public void shouldLiftTryPartialFunction() {
        Function1<String, Try<MessageDigest>> liftTry = CheckedFunction1.liftTry(digest);
        Try<MessageDigest> md5 = liftTry.apply("MD5");
        assertThat(md5.isSuccess()).isTrue();
        assertThat(md5.get()).isNotNull();
        assertThat(md5.get().getAlgorithm()).isEqualToIgnoringCase("MD5");
        assertThat(md5.get().getDigestLength()).isEqualTo(16);
        Try<MessageDigest> unknown = liftTry.apply("Unknown");
        assertThat(unknown.isFailure()).isTrue();
        assertThat(unknown.getCause()).isNotNull();
        assertThat(unknown.getCause().getMessage()).isEqualToIgnoringCase("Unknown MessageDigest not available");
    }

    private static final CheckedFunction1<Integer, Integer> recurrent1 = (i1) -> i1 <= 0 ? i1 : CheckedFunction1Test.recurrent2.apply(i1 - 1) + 1;
    private static final CheckedFunction1<Integer, Integer> recurrent2 = CheckedFunction1Test.recurrent1.memoized();

    @Test
    public void shouldCalculatedRecursively() throws Throwable {
        assertThat(recurrent1.apply(11)).isEqualTo(11);
        assertThat(recurrent1.apply(22)).isEqualTo(22);
    }

    @Test
    public void shouldComposeWithAndThen() {
        final CheckedFunction1<Object, Object> f = (o1) -> null;
        final CheckedFunction1<Object, Object> after = o -> null;
        final CheckedFunction1<Object, Object> composed = f.andThen(after);
        assertThat(composed).isNotNull();
    }

    @Test
    public void shouldComposeWithCompose() {
        final CheckedFunction1<Object, Object> f = (o1) -> null;
        final CheckedFunction1<Object, Object> before = o -> null;
        final CheckedFunction1<Object, Object> composed = f.compose(before);
        assertThat(composed).isNotNull();
    }
}