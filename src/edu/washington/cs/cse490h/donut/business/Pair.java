package edu.washington.cs.cse490h.donut.business;

import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @author alevy
 */
public class Pair<H, T> {

    private final H head;
    private final T tail;

    public Pair(H head, T tail) {
        this.head = head;
        this.tail = tail;
    }

    public H head() {
        return head;
    }

    public T tail() {
        return tail;
    }

    public boolean equals(Pair<H, T> obj) {
        return head.equals(obj.head) && tail.equals(obj.tail);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(11, 19).append(head).append(tail).toHashCode();
    }

}
