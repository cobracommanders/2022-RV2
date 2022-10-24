package org.team498.lib.util;

public interface InverseInterpolable<T> {
    double inverseInterpolate(T upper, T query);
}
