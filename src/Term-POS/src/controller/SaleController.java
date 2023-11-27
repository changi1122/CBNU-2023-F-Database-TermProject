package controller;

import enums.Mode;
import view.View;

public class SaleController extends Controller {
    Mode mode;

    public SaleController(Mode mode) {
        super();
        this.mode = mode;
    }

    @Override
    public Mode getCommand() {



        return mode;
    }
}
