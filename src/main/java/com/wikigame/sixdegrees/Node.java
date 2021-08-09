package com.wikigame.sixdegrees;

public class Node {
	String val;
	int deg;
	Node next;
	
	public Node(String val) {
		this.val = val;
		this.deg = 0;
		this.next = null;
	}
	
	public Node(String val, int deg, Node next) {
		this(val);
		this.deg = deg;
		this.next = next;
	}
}
