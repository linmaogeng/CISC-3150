import java.util.*;

public class Calculator {
	
	private Stack<String> stack;
	private LinkedList<String> queue;
	private String[] source;
	
	
	public Calculator(String[] source) {
		this.source = source.clone();
	}
	
	public static void main(String[] args) {
		
		if(args.length == 0) {
			System.out.println("Internal mode activated! \r\nPlease enter your expression and hit ENTER." );
			System.out.println("And then, send EOF to begin the computation \r\nRemember to seperate individual token with a space. \r\n");
			Scanner sc = new Scanner(System.in);
			LinkedList<String> input = new LinkedList<>();

			while(sc.hasNext()) 
				input.add(sc.next());
			
			sc.close();
			args = input.toArray(args);
		}
		
		Calculator cal = new Calculator(args);
		
		try {
			cal.solve();
		}
		catch(ArithmeticException ex) {
			System.out.println("Dividing by 0");
		}
		catch(AlgebraFailException ex) {
			System.out.println("Please check your algebra expression.");
		}
		catch(QuitMashingOnYourKeyboardException ex) {
			System.out.println("Operation is not supported.");
		}
		catch(UserIsADumbassException ex) {
			System.out.println("You forget to enter number somewhere.");
		}
		catch(Throwable ex) {
			System.out.println("Something went wrong!!.");
		}
		
	}
	
	public boolean solve() {

		boolean finish = false;
		stack = new Stack<>();
		queue = new LinkedList<>();
		int per = 1;
		stack.push("(");
		
		for(int i=0; i<source.length; i++) {
			
			if(!source[i].equals(")")) {
				if(source[i].equals("(")) 
					per++;
				stack.push(source[i]);
			}
			
			else {
				if(--per < 1) // having extra close parentheses
					if(piority(stack.peek())<0)
						throw new QuitMashingOnYourKeyboardException();
					else
						throw new AlgebraFailException();
				
				queue = stackToQ(stack);
				stack.push(evaluate(queue));
			}
		}
		
		if(per != 1) //having extra open parentheses
			throw new AlgebraFailException();
		
		queue = stackToQ(stack);
		System.out.println(evaluate(queue));
		
		finish = true;
		return finish;
	}
	
	static LinkedList<String> stackToQ(Stack<String> in){
			
		LinkedList<String> list = new LinkedList<>();
		boolean needNum = true;
		while(!in.peek().equals("(")) {
			if(needNum) {
				numericCheck(in.peek());
				list.addFirst(in.pop());
				needNum = false;
			}
			else if(piority(in.peek())>0){
				list.addFirst(in.pop());
				needNum = true;
			}
			else {
				throw new QuitMashingOnYourKeyboardException(); 
			}
		}
		
		in.pop();
		
		if(needNum)
			throw new UserIsADumbassException();
		
		return list;
	}
	
	static String evaluate(LinkedList<String> queue) { //Evaluate expression without ( )
		
		int level = 1;
		LinkedList<String> result = new LinkedList<>();
		
		while(queue.size()!= 1) {
			if(piority(queue.peek()) < level)
				result.addLast(queue.poll());
			
			else if(piority(queue.peek()) > level) {
				result.addLast(queue.poll());
				result.addLast(queue.poll());
			}
			
			else
				result.addLast(doMath(result.pollLast(),queue.poll(),queue.poll()));
			
			if (queue.isEmpty()) {
				level ++;
				queue = result;
				result = new LinkedList<>();
			}
		}
		
		return queue.poll(); 
	}
	
	static String doMath(String one, String operand, String two) {
		Double answer;
		double first,second;
		first = Double.parseDouble(one);
		second = Double.parseDouble(two);
        
		switch (operand){
		
        case "+":
        	answer = first + second;
        	break;
        	
        case "-":
        	answer =  first - second;
        	break;
        	
        case "x":
        case "*":
        	answer =  first * second;
        	break;
        	
        case "/":
        	answer = first / second;        	
        	break;
        	
        case "^":	
        	answer = Math.pow(first, second);
        	break;
        	
		default :
			throw new QuitMashingOnYourKeyboardException();
        }
    	
		if(answer.isInfinite())
    		throw new ArithmeticException();
    	
		if(Math.abs(answer) <= Long.MAX_VALUE) {
			first = answer % 1.0;
			
			if(first == 0.0){
				long round = 0L;
				round += answer;
				return String.valueOf(round);
			}
			else if(1.0 + first == 2.0){
				long round = 1L;
				round += answer;
				return String.valueOf(round);
			}
			else if(1.0 + first == 0.0){
				long round = -1L;
				round += answer;
				return String.valueOf(round);
			}
		}
		
		return String.valueOf(answer);

	}
	
	public static void numericCheck(String in) {
		
		if (piority(in) != -1) {
			throw new UserIsADumbassException();
		}
		
		try {
			Double.parseDouble(in);
		}catch(NumberFormatException ex){
			throw new AlgebraFailException();
		}

	}

	public static int piority(String operand) {
        
		switch (operand){
	        case "+":
	        case "-":
	            return 3;
	        
	        case "*":
	        case "x":
	        case "/":
	            return 2;
	            
	        case "^":
	        	return 1;
	        	
	        case "(":
	        case ")":
	        	return 0;
	        	
	        default :
	        	return -1;
		} 	
	}
}

class AlgebraFailException extends IllegalArgumentException{
	private static final long serialVersionUID = 1L;
}

class QuitMashingOnYourKeyboardException extends IllegalArgumentException{
	private static final long serialVersionUID = 1L;
}

class UserIsADumbassException  extends IllegalArgumentException{
	private static final long serialVersionUID = 1L;
}


