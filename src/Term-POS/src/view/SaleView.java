package view;

import controller.SaleController;

import java.sql.ResultSet;

public class SaleView extends View {

    SaleController saleController;

    public SaleView(SaleController saleController) {
        this.saleController = saleController;
    }

    @Override
    public void show() {
        ResultSet rs = saleController.readCart();
        int totalPrice = 0;

        try {
            System.out.println("[POS 화면] 상품 담기(바코드번호), 상품 빼기(-바코드번호), 상품 관리(P), 판매 내역 출력(H)");
            System.out.printf("------------------------------------------------------------------------------------\n");
            System.out.printf("%12s %10s %9s %9s\n",
                    "상품명", "유통사", "수량", "금액");
            System.out.printf("------------------------------------------------------------------------------------\n");
            while (rs != null && rs.next()) {
                int finalPrice = rs.getInt(4);
                totalPrice += finalPrice;
                System.out.printf("%12s %10s %9d %9d\n",
                        rs.getString(1), rs.getString(2), rs.getInt(3), finalPrice
                );
            }
            System.out.printf("------------------------------------------------------------------------------------\n");
            System.out.println("총 금액: " + totalPrice + "      결제(S)   입고(N)  모두 지우기(--)");
            System.out.printf("------------------------------------------------------------------------------------\n\n\n");
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }
}
