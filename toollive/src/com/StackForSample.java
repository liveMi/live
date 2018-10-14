package com;
import java.util.Iterator;

/**
 * 用于存取的基础包结构 
 * @author Administrator
 *
 * @param <Item>
 */
public class StackForSample<Item> implements Iterable<Item>{
	
	private Node first =null;
	
	private int count=0;
	
	public StackForSample() {
		super();
	}

	public Item getFirstAndRemove() {
		if(count==0) {
			return null;
		}
		count--;
		Item data=first.data;
		first=first.next;
		return data;
	}
	
	public void add(Item item) {
		count++;
		Node next=new Node(item);
		next.next=first;
		first = next;
		
	}
	
	public int size() {
		return count;
	}
	
	@Override
	public Iterator<Item> iterator() {
		return new StackIterator();
	}
	
	class StackIterator implements Iterator<Item>{

		Node hedn =first;
		
		public boolean hasNext() {
			return hedn!=null;
		}
		
		public Item next() {
			Item item=hedn.data;
			hedn=hedn.next;
			return item;
		}
	}

	
	private class Node {
        Node next = null;
        Item data;
        public Node(Item item) {
        	this.data=item;
        }
    }
	
}
