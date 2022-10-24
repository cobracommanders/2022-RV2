package org.team498.lib.util;

public interface Interpolable<T> {
    T interpolate(T other, double t);
}