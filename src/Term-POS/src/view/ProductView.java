package view;

import controller.ProductController;
import java.sql.ResultSet;

public class ProductView extends View {

    ProductController productController;
    public ProductView(ProductController productController) {
        this.productController = productController;
    }

    @Override
    public void show() {
        ResultSet rs = productController.readProduct();

        try {
            System.out.println("[상품 관리 화면] 상품 등록(N), 할인 관리(D), 상품 관리 종료(X)");
            System.out.printf("------------------------------------------------------------------------------------\n");
            System.out.printf("%4s %12s %10s %9s %8s %8s %3s %8s\n",
                    "상품ID", "상품명", "유통사", "정가", "제조국", "주소", "재고", "바코드번호");
            System.out.printf("------------------------------------------------------------------------------------\n");
            while (rs != null && rs.next()) {
                System.out.printf("%4d %12s %10s %9d %8s %8s %4d %12s\n",
                    rs.getLong(3), rs.getString(4), rs.getString(2),
                    rs.getInt(5), rs.getString(8), rs.getString(7),
                    rs.getInt(6), rs.getString(1)
                );
            }
            System.out.printf("------------------------------------------------------------------------------------\n\n\n");
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }
}
