package com.zphinx.sortattributes;

/**
 * An interface used to define additional methods for the comparable interface
 * Created by rogue on 07/09/15.
 */
public interface Sortable<T> extends Comparable {


    /**
     * Compares this object to the specified object to determine their relative
     * order.
     *
     * @param another the object to compare to this instance.
     * @param index   An index used to specify the property which will determine the comparator
     * @return a negative integer if this instance is less than {@code another};
     * a positive integer if this instance is greater than
     * {@code another}; 0 if this instance has the same order as
     * {@code another}.
     * @throws ClassCastException if {@code another} cannot be converted into something
     *                            comparable to {@code this} instance.
     */

    public int compareTo(T another, int index);

}
