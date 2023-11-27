package controller;

import enums.Mode;

public class EventController extends Controller {
    Mode mode;

    public EventController(Mode mode) {
        super();
        this.mode = mode;
    }

    @Override
    public Mode getCommand() {



        return mode;
    }
}
