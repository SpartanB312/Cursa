package net.spartanb312.cursa.utils.math;

import java.util.Objects;

public class Pair<T, V> {
    public T a;
    public V b;

    public Pair(T a, V b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj) || (obj instanceof Pair && ((Pair<?, ?>) obj).a.equals(a) && ((Pair<?, ?>) obj).b.equals(b));
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b);
    }

}
