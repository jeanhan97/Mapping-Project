import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
/*
 * Name: Ji Eun Han
 * Assignment: Project 04
 * Section: TR 9:40am
 * Lab TA: Chengyu Deng, Matt Delsordo
 * I collaborated with Mackenzie Lee. 
 */

public class PQueue<T extends Comparable<? super T>> extends AbstractCollection<T>{

	public static class Comparatorclass<T> implements Comparator<T>{

		@SuppressWarnings("unchecked")
		public int compare(T object1, T object2) {
			return ((Comparable<T>)object1).compareTo(object2);
		}
		
	}
	
	public Comparator<T> mycomp = new Comparatorclass<T>();
	public int size; 
	public ArrayList<T> arraylist; 
	public int index = 1;
	
	public class Iteratorclass implements Iterator<T>{

		@Override
		public boolean hasNext() {
			return index <= size; 
		}

		@SuppressWarnings("unchecked")
		@Override
		public T next() {
			return (T) arraylist.get(index);
		}
		
	}

	//Priority Queue constructor 
	public PQueue(){
		arraylist = new ArrayList<T>(32);
		arraylist.add(null);
		size = 0;
	}
	
	public PQueue(Comparator<T> comp){
		this();
		mycomp = comp; 
	}
	
	public PQueue(Collection<T> collections){
		this();
		arraylist.addAll(collections);
		size = collections.size();
		
		for (int i = collections.size()/2; i >= 1; i--){
			heapify(i);
		}
	}
	
	public boolean add(T object){
		arraylist.add(object);
		size++;
		int k = size; 
		
		while(k>1 && mycomp.compare(arraylist.get(k/2), object)>0){
			arraylist.set(k, arraylist.get(k/2));
			k /= 2; 
		}
		arraylist.set(k, object);
		return true; 
	}
	
	
	@Override
	public int size() {
		return size; 
	}
	
	public boolean isEmpty(){
		return size == 0;
	}
	
	public T poll(){
		if(isEmpty() == false){
			T temp = arraylist.get(1);
			arraylist.set(1, arraylist.get(size)); //moves the last item to the head of queue
			arraylist.remove(size);
			size--;
			
			if (size >1){
				heapify(1);
			}
			return temp; 	
		}
		return null;
	}
	
	public Iterator<T> iterator(){
		return new Iteratorclass(); 
	}
	
	//O(log(size-root) run time where root is the index at which the re-heaping occurs
	public void heapify(int root){
		T last = arraylist.get(root);
		int child, j = root; 
		
		while (2*j <= size){
			child = 2*j;
			if (child < size && mycomp.compare(arraylist.get(child), arraylist.get(child+1))>0){
				child++;
				
			}
			if (mycomp.compare(last, arraylist.get(child))<=0){
				break;
			} else {
				arraylist.set(j, arraylist.get(child));
				j = child;
			}
		}
		arraylist.set(j, last);
	}
	
	
	
	
	
}
