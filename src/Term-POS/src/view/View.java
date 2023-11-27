package view;

import java.io.IOException;

public class View {

    public void show() {
        System.out.println("View 구현이 필요합니다.");
    }

    public void clearConsole() {
        for (int i = 0; i < 24; i++) {
            System.out.println();
        }
    }
}
