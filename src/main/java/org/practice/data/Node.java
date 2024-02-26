package org.practice.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Node<T> {

    private T value;
    private Node<T> previous;
    private Node<T> next;

    public void resetReferences() {
        this.previous = null;
        this.next = null;
    }

    @Override
    public String toString() {
        return "Node{" +
                "value=" + value +
                '}';
    }
}
