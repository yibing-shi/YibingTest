package com.yibing.algorithm.questions;

import java.util.HashMap;

public class LRUCache<K, V> {
  private static class Node<K,V> {
    K key;
    V value;
    Node<K,V> prev;
    Node<K,V> next;

    Node(K key, V value) {
      this.key = key;
      this.value = value;
      this.next = this;
      this.prev = this;
    }

    @Override
    public String toString() {
      return "Node{" +
          "key=" + key +
          ", value=" + value +
          '}';
    }
  }

  private Node<K, V> head;
  private HashMap<K, Node<K, V>> index;
  private int capacity;

  public LRUCache(int capacity) {
    this.head = null;
    this.index = new HashMap<>();
    this.capacity = capacity;
  }

  public void put(K key, V val) {
    Node<K,V> node = index.get(key);
    if(node != null) {
      node.value = val;
      adjustLocationOfNode(node);
      return;
    }

    //New item
    node = new Node<>(key, val);
    insertNodeToHead(node);
    index.put(key, node);

    if (this.index.size() > this.capacity) {
      evictItem();
    }
  }

  private void insertNodeToHead(Node<K,V> node) {
    if (this.head == null) {
      this.head = node;
      this.head.prev = node;
      this.head.next = node;
      return;
    }

    node.next = this.head;
    node.prev = this.head.prev;

    this.head.prev.next = node;
    this.head.prev = node;

    this.head = node;
  }

  private void adjustLocationOfNode(Node<K, V> node) {
    node.prev.next = node.next;
    node.next.prev = node.prev;

    insertNodeToHead(node);
  }

  private void evictItem() {
    Node<K, V> tail = this.head.prev;
    this.head.prev = tail.prev;
    tail.prev.next = this.head;
    this.index.remove(tail.key);
  }

  public V get(K key) {
    Node<K,V> node = this.index.get(key);
    if (node == null) {
      return null;
    }

    adjustLocationOfNode(node);
    return node.value;
  }
}
