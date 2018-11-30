package com.yibing.algorithm.questions;

/*
Description of Question
Given a binary tree, which may not be a complete binary tree, print it in a pretty way. Each node value is unique and each node value has the same length.

Here is an example:

                 a
               /     \
              b         c
          /           /     \
       d            e         f
         \            \      /  \
            g          h   i     j
          /
         k
Two requirements:

1) No two nodes are in the same vertical line.
2) Every vertical line must have one node.

The connection line should be put roughly in the middle of the two nodes it wants to connect.

Solution to Question
For each node, we need to find its proper indentation. For example, node a's indentation is equal its left tree size. node ch 's indentation will be indentation(a) + ch 's left tree size.

Project the tree from top view to a line, the tree will look like this:

d k g b a e h ch i f j

The indentation of each node is decided by its location in this line.

For example, there are 8 other nodes before node i. Node i will have 8 empty space or nodes before it in the final print result.

To get this projected line, we can do an in-order traverse of the tree, then store this information in a Map<NodeValue, indentation>

After we have the map, we can do a level traverse of the tree, print each node value with its indentation in this map.
Why is this question good?
Everyone knows tree traverse. Analyzing this problem and breaking it down to two tree traverses can evaluate candidate's problem solving ability.
Writing two different tree traverses without a bug can evaluate candidate's code ability.
Space and time complexity is also a concern for this question. Candidate shouldn't allocate any space to store the projected line. The only allowed space usage is the Map
Possible follow up
Space complexity O(n) and time complexity O(n)
If each node has different length, how to improve the algorithm?
If each node value is not unique, how to store its indentation information? Obviously, we cannot store it in a Map<NodeValue, indentation> in this case.

 */

import java.util.LinkedList;
import java.util.List;

public class BinaryTreePrinter {
  private static class Node {
    private char ch;
    private Node left;
    private Node right;
    private int indention;

    Node(char ch) {
      this(ch, null, null);
    }

    Node(char ch, Node left, Node right) {
      this.ch = ch;
      this.left = left;
      this.right = right;
    }

    public void setLeft(Node left) {
      this.left = left;
    }

    public void setRight(Node right) {
      this.right = right;
    }
  }

  private int browseBinaryTree(Node root, int indentionOffset) {
    if (root == null) {
      return 0;
    }
    int leftChildSize = browseBinaryTree(root.left, indentionOffset);
    root.indention = indentionOffset + leftChildSize + 1;
    int rightChildSize = browseBinaryTree(root.right, root.indention);
    return leftChildSize + rightChildSize + 1;
  }

  private void addToPrintList(List<Node> toPrintList, Node ... nodes) {
    for (Node node : nodes) {
      if (node != null)
        toPrintList.add(node);
    }
  }

  private void addNodeInfoToPrintLine(StringBuilder lineBuilder, char ch, int indention) {
    for (int i = lineBuilder.length(); i < indention; i ++) {
      lineBuilder.append(' ');
    }
    lineBuilder.append(ch);
  }

  void printBinaryTree(Node root) {
    browseBinaryTree(root, 0);

    for(int i = 0; i < root.indention; i ++) {
      System.out.print(' ');
    }
    System.out.println(root.ch);

    List<Node> toPrintList = new LinkedList<>();
    addToPrintList(toPrintList, root);

    while (!toPrintList.isEmpty()) {
      List<Node> nextToPrintList = new LinkedList<>();
      StringBuilder connBuilder = new StringBuilder();
      StringBuilder lineBuilder = new StringBuilder();
      for(Node node : toPrintList) {
        addToPrintList(nextToPrintList, node.left, node.right);
        if (node.left != null) {
          int connectorIndention = (node.indention + node.left.indention) / 2;
          addNodeInfoToPrintLine(connBuilder, '/', connectorIndention);
          addNodeInfoToPrintLine(lineBuilder, node.left.ch, node.left.indention);
        }
        if (node.right != null) {
          int connectorIndention = (node.indention + node.right.indention + 1) / 2;
          addNodeInfoToPrintLine(connBuilder, '\\', connectorIndention);
          addNodeInfoToPrintLine(lineBuilder, node.right.ch, node.right.indention);
        }
      }
      System.out.println(connBuilder.toString());
      System.out.println(lineBuilder.toString());
      toPrintList = nextToPrintList;
    }
  }

  private static Node constructTree() {
    Node nodeA = new Node('a');
    Node nodeB = new Node('b');
    Node nodeC = new Node('c');
    Node nodeD = new Node('d');
    Node nodeE = new Node('e');
    Node nodeF = new Node('f');
    Node nodeG = new Node('g');
    Node nodeH = new Node('h');
    Node nodeI = new Node('i');
    Node nodeJ = new Node('j');
    Node nodeK = new Node('k');

    nodeA.setLeft(nodeB);
    nodeA.setRight(nodeC);
    nodeB.setLeft(nodeD);
    nodeC.setLeft(nodeE);
    nodeC.setRight(nodeF);
    nodeD.setRight(nodeG);
    nodeE.setRight(nodeH);
    nodeF.setLeft(nodeI);
    nodeF.setRight(nodeJ);
    nodeG.setLeft(nodeK);

    return nodeA;
  }

  public static void main(String[] args) {
    BinaryTreePrinter printer = new BinaryTreePrinter();
    printer.printBinaryTree(constructTree());
  }
}
