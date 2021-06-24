package fr._42.blinnea.computorv1;

import fr._42.blinnea.computorv1.equation.PolynomialEquation;
import fr._42.blinnea.computorv1.tokenizer.Tokenizer;

import java.util.logging.Level;

public class App implements Loggable {
    public static void main(String[] args) {
        try {
            Tokenizer tokenizer = new Tokenizer(args[0]);
            Parser parser = new Parser(tokenizer.iterator());
            PolynomialEquation equation = (PolynomialEquation) parser.parse();
            System.out.println("Reduced form: " + equation + " = 0");
            System.out.println("Polynomial degree: " + equation.getHighestExponent());
            System.out.println(equation.getSolution());
        } catch (IndexOutOfBoundsException ex) {
            System.out.println("Missing command line argument");
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        }
    }
}
