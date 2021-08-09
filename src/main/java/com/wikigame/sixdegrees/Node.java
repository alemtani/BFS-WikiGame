package com.wikigame.sixdegrees;

public class Node {
	String val;
	Node next;
	
	public Node(String val) {
		this.val = val;
		this.next = null;
	}
	
	public Node(String val, Node next) {
		this(val);
		this.next = next;
	}
}
