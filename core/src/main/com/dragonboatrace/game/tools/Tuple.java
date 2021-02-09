package main.com.dragonboatrace.game.tools;

// >>>> Added in assessment 2 <<<<
/**
 * A simple helper class for storing two different object.
 * 
 * @author Jacob Turner
 */
public class Tuple<A, B> {
    /**
     * The first item in the Tuple.
     */
    private A a;
    /**
     * The second item in the Tuple.
     */
    private B b;

    /**
     * Creates a new Tuple without instantiating its values
     */
    public Tuple() {

    }

    /**
     * Creates a new Tuple containing the two paramaters.
     * 
     * @param a     The first item in the Tuple.
     * @param b     The second item in the Tuple.
     */
    public Tuple(A a, B b) {
        this.a = a;
        this.b = b;
    }
    
    /**
     * Get the first item in the Tuple.
     * 
     * @return Object of type A.
     */
    public A fst() {
        return this.a;
    }

    /**
     * Get the second item in the Tuple.
     * 
     * @return Object of type B.
     */
    public B snd() {
        return  this.b;
    }

    /**
     * Get a string representation of the Tuple.
     * 
     * @return String representation of the Tuple.
     */
    public String toString() {
        return String.format("Tuple<%s, %s>", this.a.toString(), this.b.toString());
    }
}