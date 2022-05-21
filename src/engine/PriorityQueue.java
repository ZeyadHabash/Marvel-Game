package engine;

public class PriorityQueue {
 
   private Comparable[] elements;
   private int nItems;
   private int maxSize;

   public PriorityQueue(int size){
      maxSize = size;
      elements = new Comparable[maxSize];
      nItems=0;
   }
    
   public void insert(Comparable item) {
      
      int i;
      for (i = nItems - 1;i >= 0 && item.compareTo(elements[i]) > 0;i--)
         elements[i + 1] = elements[i];

      elements[i + 1] = item;
      nItems++;
   }
    
   public Comparable remove() {
      nItems--;
      return elements[nItems];
   }
   public Comparable remove(Object o){
      PriorityQueue tempQ = new PriorityQueue(this.size());
      Comparable tempObj = null;
      while(!this.isEmpty()){
         tempObj = this.remove();
         if(!tempObj.equals(o))
            tempQ.insert(tempObj);
      }
      while(!tempQ.isEmpty())
         insert(tempQ.remove());
      return tempObj;
   }
    
   public boolean isEmpty() {
      return (nItems == 0);
   }
    
   public boolean isFull() {
      return (nItems == maxSize);
   }
  
   public Comparable peekMin() {
      return elements[nItems-1];
   }
     
   public int size() {
      return nItems;
   }

   public String toString(){
      String r = "[";
      for(int i=0;i<elements.length;i++){
         r += elements[i] + ", ";
      }
      r+= "]";
      return r;
   }
}
