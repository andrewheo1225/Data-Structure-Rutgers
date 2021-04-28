package poly;

import java.io.IOException;
import java.util.Scanner;

/**
 * This class implements evaluate, add and multiply for polynomials.
 * 
 * @author runb-cs112
 *
 */
public class Polynomial {
    
    /**
     * Reads a polynomial from an input stream (file or keyboard). The storage format
     * of the polynomial is:
     * <pre>
     *     <coeff> <degree>
     *     <coeff> <degree>
     *     ...
     *     <coeff> <degree>
     * </pre>
     * with the guarantee that degrees will be in descending order. For example:
     * <pre>
     *      4 5
     *     -2 3
     *      2 1
     *      3 0
     * </pre>
     * which represents the polynomial:
     * <pre>
     *      4*x^5 - 2*x^3 + 2*x + 3 
     * </pre>
     * 
     * @param sc Scanner from which a polynomial is to be read
     * @throws IOException If there is any input error in reading the polynomial
     * @return The polynomial linked list (front node) constructed from coefficients and
     *         degrees read from scanner
     */
    public static Node read(Scanner sc) 
    throws IOException {
        Node poly = null;
        while (sc.hasNextLine()) {
            Scanner scLine = new Scanner(sc.nextLine());
            poly = new Node(scLine.nextFloat(), scLine.nextInt(), poly);
            scLine.close();
        }
        return poly;
    }
    
    /**
     * Returns the sum of two polynomials - DOES NOT change either of the input polynomials.
     * The returned polynomial MUST have all new nodes. In other words, none of the nodes
     * of the input polynomials can be in the result.
     * 
     * @param poly1 First input polynomial (front of polynomial linked list)
     * @param poly2 Second input polynomial (front of polynomial linked list
     * @return A new polynomial which is the sum of the input polynomials - the returned node
     *         is the front of the result polynomial
     */
    public static Node add(Node poly1, Node poly2) {
        Node ans = null;
        Node temp = null;
        Node ptr1 = poly1;
        Node ptr2 = poly2;
        // check both null
        if (ptr1 == null && ptr2 == null) {
            return null;
        }
        // check if 1 is null and 2 is not null, then make new node for non-null and
        // return
        if (ptr1 == null && ptr2 != null) {
            Node a = null;
            Node t = null;
            t = new Node(ptr2.term.coeff, ptr2.term.degree, null);
            ptr2 = ptr2.next;
            a = t;
            while (ptr2 != null) {
                a.next = new Node(ptr2.term.coeff, ptr2.term.degree, null);
                a = a.next;
                ptr2 = ptr2.next;
            }
            return t;
        }
        if (ptr2 == null && ptr1 != null) {
            Node a = null;
            Node t = null;
            t = new Node(ptr1.term.coeff, ptr1.term.degree, null);
            ptr1 = ptr1.next;
            a = t;
            while (ptr1 != null) {
                a.next = new Node(ptr1.term.coeff, ptr1.term.degree, null);
                a = a.next;
                ptr1 = ptr1.next;
            }
            return t;
        }
 
        if (ptr1.term.degree == ptr2.term.degree) {
            temp = new Node(ptr1.term.coeff + ptr2.term.coeff, ptr1.term.degree, null);
            ptr1 = ptr1.next;
            ptr2 = ptr2.next;
        }
        // if poly1's cursor degree is higher than poly2
        else if (ptr1.term.degree > ptr2.term.degree) {
            temp = new Node(ptr2.term.coeff, ptr2.term.degree, null);
            ptr2 = ptr2.next;
        }
        // if poly2's cursor degree is lower than poly2
        else if (ptr1.term.degree < ptr2.term.degree) {
            temp = new Node(ptr1.term.coeff, ptr1.term.degree, null);
            ptr1 = ptr1.next;
        }
 
        ans = temp;
 
        while (ptr1 != null && ptr2 != null) {
 
            // if 2 degree is the same, perform addition, and move cursor
 
            if (ptr1.term.degree == ptr2.term.degree) {
                ans.next = new Node(ptr1.term.coeff + ptr2.term.coeff, ptr1.term.degree, null);
                ans = ans.next;
 
                ptr1 = ptr1.next;
                ptr2 = ptr2.next;
            }
            // if poly1's cursor degree is higher than poly2
            else if (ptr1.term.degree > ptr2.term.degree) {
                ans.next = new Node(ptr2.term.coeff, ptr2.term.degree, null);
                ans = ans.next;
 
                ptr2 = ptr2.next;
            }
            // if poly2's cursor degree is lower than poly2
            else if (ptr1.term.degree < ptr2.term.degree) {
                ans.next = new Node(ptr1.term.coeff, ptr1.term.degree, null);
                ans = ans.next;
 
                ptr1 = ptr1.next;
            }
 
        }
 
        while (ptr1 != null) {
            ans.next = new Node(ptr1.term.coeff, ptr1.term.degree, null);
            ans = ans.next;
 
            ptr1 = ptr1.next;
        }
 
        while (ptr2 != null) {
            ans.next = new Node(ptr2.term.coeff, ptr2.term.degree, null);
            ans = ans.next;
 
            ptr2 = ptr2.next;
        }
 
        // check for 0 coeff
 
        Node ptr = temp;
        Node prev = null;
        while (ptr != null && ptr.term.coeff == 0) {
            temp = ptr.next;
            ptr = temp;
        }
 
        while (ptr != null) {
            while (ptr != null && ptr.term.coeff != 0) {
                prev = ptr;
                ptr = ptr.next;
            }
            if (ptr != null) {
                if (ptr.term.coeff == 0) {
                    prev.next = ptr.next;
                    ptr = prev.next;
                }
            }
        }
 
        return temp;
 
    }

    
    /**
     * Returns the product of two polynomials - DOES NOT change either of the input polynomials.
     * The returned polynomial MUST have all new nodes. In other words, none of the nodes
     * of the input polynomials can be in the result.
     * 
     * @param poly1 First input polynomial (front of polynomial linked list)
     * @param poly2 Second input polynomial (front of polynomial linked list)
     * @return A new polynomial which is the product of the input polynomials - the returned node
     *         is the front of the result polynomial
     */
    public static Node multiply(Node poly1, Node poly2) {
        // check if either are null
        if (poly1 == null || poly2 == null) {
            return null;
        }
 
        Node ptr1 = poly1;
        Node ptr2 = poly2;
        Node temp = new Node(ptr1.term.coeff * ptr2.term.coeff, ptr1.term.degree + ptr2.term.degree, null);
        Node ans = temp;
        ptr2 = ptr2.next;
        
        while (ptr1 != null) {
            while (ptr2 != null) {
                ans.next = add(ans.next, new Node(ptr1.term.coeff * ptr2.term.coeff, ptr1.term.degree + ptr2.term.degree, null));
                ptr2 = ptr2.next;
            }
            ptr2 = poly2;
            ptr1 = ptr1.next;
        }
        return temp;

    }
        
    /**
     * Evaluates a polynomial at a given value.
     * 
     * @param poly Polynomial (front of linked list) to be evaluated
     * @param x Value at which evaluation is to be done
     * @return Value of polynomial p at x
     */
    public static float evaluate(Node poly, float x) {
        if(poly == null){
            return 0;
        }
        Node ptr = poly;
        float answer = 0;
        while (ptr != null) {
            double value = ptr.term.coeff * (Math.pow(x, ptr.term.degree));
            answer += value;
            ptr = ptr.next;
        }
        return answer;

    }
    
    /**
     * Returns string representation of a polynomial
     * 
     * @param poly Polynomial (front of linked list)
     * @return String representation, in descending order of degrees
     */
    public static String toString(Node poly) {
        if (poly == null) {
            return "0";
        } 
        
        String retval = poly.term.toString();
        for (Node current = poly.next ; current != null ;
        current = current.next) {
            retval = current.term.toString() + " + " + retval;
        }
        return retval;
    }    
}