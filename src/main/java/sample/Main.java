package sample;

/* This Main class invokes main() method in the SubMain class extending Application
* because a Main class intended to be packed into an executable jar must not extends another classes */

public class Main {
    public static void main(String[] args) {
//        System.out.println("Running SubMain from Main");
        SubMain.main(args);
    }
}
