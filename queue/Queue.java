package queue;

// :NOTE: используйте или n, или size в качестве размера очереди (иначе путает)
public interface Queue {
	//Pred: true
	//Post: R == size && immutable(a, 0, n - 1)
    int size();
	
	//Pred: true
	//Post: R == (size == 0) && immutable(a, 0, n - 1)
    boolean isEmpty();
	
	//Pred: x != null
	//Post: size' = size + 1 && a[n'] == x && immutable(a, 0, n' - 1) 
    void enqueue(Object x);
	
	//Pred: size > 0
	//Post: R == a[0] && size' == size - 1 && forall i=1...n - 1: a'[i - 1] == a[i]
    Object dequeue();
	
	//Pred: size > 0
	//Post: R == a[0] && immutable(a, 0, n - 1)
    Object element();
	
	//Pred: true
	//Post: size' == 0
    void clear();
	
	//Pred: true
	//Post: forall i=0...size' - 1: a'[i] != a'[i + 1] && forall i=0...size - 1: a[i] = a[i + 1] = a'[j]: j <= i && forall i=0...size: a.contains(a'[i]) == True
    // :NOTE: под контракт подходит реализация dedup: a={1,2} -> a'={1,2,3}
    void dedup();
}
