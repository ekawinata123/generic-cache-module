package org.practice.data;

import lombok.Getter;

@Getter
public class DoublyLinkedList<T> {

    private final Node<T> head;
    private final Node<T> tail;

    public DoublyLinkedList() {
        this.head = new Node<>(null, null, null);
        this.tail = new Node<>(null, this.head, null);
        this.head.setNext(this.tail);
    }

    public Node<T> append(T value) {
        Node<T> lastElementBeforeTail = this.tail.getPrevious();
        lastElementBeforeTail.setNext(new Node<>(value, lastElementBeforeTail.getPrevious(), this.tail));
        return lastElementBeforeTail.getNext();
    }

    public Node<T> prepend(T value) {
        Node<T> movedNode = this.head.getNext();
        Node<T> newNode = new Node<>(value, this.head, movedNode);
        this.head.setNext(newNode);
        movedNode.setPrevious(newNode);
        return newNode;
    }

    public Node<T> removeAfterHead() {
        if (!this.head.getNext().equals(this.tail)) {
            Node<T> removedNode = this.head.getNext();
            this.head.setNext(this.head.getNext()
                    .getNext());
            removedNode.resetReferences();
            return removedNode;
        }
        return null;
    }

    public void swapNode(Node<T> firstNode, Node<T> secondNode) {
        Node<T> firstNodePrevious = firstNode.getPrevious();
        Node<T> secondNodeNext = secondNode.getNext();
        firstNode.setNext(secondNodeNext);
        firstNode.setPrevious(secondNode);
        secondNode.setNext(firstNode);
        secondNode.setPrevious(firstNodePrevious);
        firstNodePrevious.setNext(secondNode);
        secondNodeNext.setPrevious(firstNode);
    }

    @Override
    public String toString() {
        Node<T> iterator = this.head;
        StringBuilder sb = new StringBuilder();
        while (iterator != null) {
            sb.append(iterator)
                    .append("\n");
            iterator = iterator.getNext();
        }
        return sb.toString();
    }
}
