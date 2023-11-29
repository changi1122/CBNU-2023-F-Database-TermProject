package controller;

import enums.Mode;

import java.util.Scanner;

public class SaleController extends Controller {
    Mode mode;

    public SaleController(Mode mode) {
        super();
        this.mode = mode;
    }

    @Override
    public Mode getCommand() {
        Scanner sc = new Scanner(System.in);

        String command = sc.nextLine();
        if (command.isEmpty())
            return mode;
        char action = command.charAt(0);

        switch(action) {
            default:
                
                break;
        }



        return mode;
    }
}
