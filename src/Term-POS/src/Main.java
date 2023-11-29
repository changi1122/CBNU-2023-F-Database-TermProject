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
        Controller currentController =  new ProductController(Mode.Product);
        View currentView = new ProductView((ProductController) currentController);

        while (true) {
            currentView.clearConsole();
            currentView.show();

            Mode nextMode = currentController.getCommand();

            if (mode != nextMode) {
                mode = nextMode;
                currentController.closeConnection();
                
                /* 모드 전환 */
                if (mode == Mode.Sale) {
                    currentController = new SaleController(Mode.Sale);
                    currentView = new SaleView();
                }
                else if (mode == Mode.Product) {
                    currentController = new ProductController(Mode.Product);
                    currentView = new ProductView((ProductController) currentController);
                }
                else if (mode == Mode.Event) {
                    currentController = new EventController(Mode.Event);
                    currentView = new EventView((EventController) currentController);
                }
            }

        }
    }
}