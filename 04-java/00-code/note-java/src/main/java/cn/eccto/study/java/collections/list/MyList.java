package cn.eccto.study.java.collections.list;

/**
 * An ordered collection (also know as a <i>sequence</i>
 *
 * @author EricChen 2020/01/24 19:54
 */
public interface MyList<E> {

    /**
     * Returns the number of elements in this list
     * @return
     */
    int size();

    /**
     * Return <code>true</code> if this List contains no element
     */
    boolean isEmpty();

    /**
     *
     * @param index
     * @return
     */
    E remove(int index);

    /**
     * Removes the element at the specified position in this list , Shifts any subsequent elements to the
     * left ( subtracts one from their indices)
     * @param e
     * @return
     */
    boolean add(E e);

}
