import controller.Controller;
import controller.EventController;
import controller.ProductController;
import controller.SaleController;
import enums.Mode;
import view.EventView;
import view.ProductView;
import view.SaleView;
import view.View;

public class Main {

    public static void main(String[] args) {

        Mode mode = Mode.Product;
        View currentView = new ProductView();
        Controller currentController =  new ProductController(Mode.Product);

        while (true) {
            currentView.clearConsole();
            currentView.show();

            Mode nextMode = currentController.getCommand();

            if (mode != nextMode) {
                mode = nextMode;
                currentController.closeConnection();
                
                /* 모드 전환 */
                if (mode == Mode.Sale) {
                    currentView = new SaleView();
                    currentController = new SaleController(Mode.Sale);
                }
                else if (mode == Mode.Product) {
                    currentView = new ProductView();
                    currentController = new ProductController(Mode.Product);
                }
                else if (mode == Mode.Event) {
                    currentView = new EventView();
                    currentController = new EventController(Mode.Event);
                }
            }

        }
    }
}