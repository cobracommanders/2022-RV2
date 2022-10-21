package org.team498.common.util;

public interface Interpolable<T> {
    T interpolate(T other, double t);
}