package fr._42.blinnea.computorv1;

import fr._42.blinnea.computorv1.tokenizer.Tokenizer;

public class Main implements Loggable {
    public static void main(String[] args) {
        Loggable.setupDefaultLogger();
        Tokenizer tokenizer = new Tokenizer("1*2=2^3^(1+1)/5*X");
        Parser parser = new Parser(tokenizer.iterator());
        System.out.println(parser.parse());
    }
}
