package fr._42.blinnea.computorv1.equation;

import com.sun.javaws.exceptions.InvalidArgumentException;

public interface Equation {
    Equation add(Equation equation);
    Equation subtract(Equation equation);
    Equation multiply(Equation equation);
    Equation divide(Equation equation);
    Equation raise(Equation equation);
    Equation additiveInverse();
    Equation multiplicativeInverse();
    Equation clone();
    String toString();

    default Equation add(Member member) {
        return add(member.toEquation());
    }
    
    default Equation subtract(Member member) {
        return subtract(member.toEquation());
    }
    
    default Equation multiply(Member member) {
        return multiply(member.toEquation());
    }
    
    default Equation divide(Member member) {
        return divide(member.toEquation());
    }
    
    default Equation raise(Member member) {
        return raise(member.toEquation());
    }

    interface Member {
        Equation toEquation();

        default Equation add(Member member) {
            return toEquation().add(member);
        }

        default Equation subtract(Member member) {
            return toEquation().subtract(member);
        }

        default Equation divide(Member member) {
            return toEquation().divide(member);
        }

        default Equation multiply(Member member) {
            return toEquation().multiply(member);
        }

        default Equation raise(Member member) {
            return toEquation().raise(member);
        }

        default Equation add(Equation equation) {
            return toEquation().add(equation);
        }

        default Equation subtract(Equation equation) {
            return toEquation().subtract(equation);
        }

        default Equation divide(Equation equation) {
            return toEquation().divide(equation);
        }

        default Equation multiply(Equation equation) {
            return toEquation().multiply(equation);
        }

        default Equation raise(Equation equation) {
            return toEquation().raise(equation);
        }
    }

    class EquationException extends IllegalArgumentException {
        public EquationException(String message) {
            super(message);
        }
    }
}
