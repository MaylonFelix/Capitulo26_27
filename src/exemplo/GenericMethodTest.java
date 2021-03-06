package exemplo;

public class GenericMethodTest {

	public static void main(String[] args) {
		Integer[] intArray = {1,2,3,4,5,6};
		Double[] doubleArray = {1.1,1.2,1.3,1.4,1.5};
		Character[] characterArray = {'a', 'b', 'c', 'd', 'e'};
		Teste[] testeArray = {new Teste("teste1",1),new Teste("teste2",2),new Teste("teste3",3)};
		Integer i = 1;

		printArray(intArray);
		printArray(doubleArray);
		printArray(characterArray);
		printArray(testeArray);
		
		System.out.println("M�ximo: "+ maximo(7, 8, 10));
	}

	/*public static <T> void printArray(T[] inputArray){
		for(T element : inputArray)
			System.out.printf("%s ", element);
		
		System.out.println();
	}*/
	
	public static void printArray(Object[] inputArray){
		for(Object element : inputArray)
			System.out.printf("%s ", element);
		
		System.out.println();
	}
	
	public static <T extends Comparable<T>> T maximo(T x, T y, T z) {
		T max = x;
		if(y.compareTo(max)>0){
			max = y;
		}
		
		if(z.compareTo(max) >0){
			max = z;
		}
		
		return max;
		
	}
}

class Teste{
	public String atributo1;
	public int atributo2;
	
	Teste(String atributo1, int atributo2){
		this.atributo1 =atributo1;
		this.atributo2 = atributo2;
	}
	 @Override
	public String toString() {
		
		return atributo1+" "+atributo2;
	}
}
