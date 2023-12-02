package controller;

import enums.Mode;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
            case 'P':
            case 'p':
                this.mode = Mode.Product;
                break;
            case 'S':
            case 's':
                makePayment();
                break;
            case 'N':
            case 'n':
                receiveProduct();
                break;
            case 'H':
            case 'h':
                printAllSales();
                break;
            case '-':
                if (command.substring(1).equals("-"))
                    deleteAllCart();
                else
                    deleteCart(command.substring(1));
            default:
                createCart(command);
                break;
        }



        return mode;
    }

    /* 전체 Cart 담은 상품 조회 */
    public ResultSet readCart() {
        ResultSet rs = null;
        try {
            PreparedStatement stmt = con.prepareStatement(
                    "SELECT P.name, P.provider, C.count, final_price " +
                            "FROM (cart C INNER JOIN cartprice CP ON (C.product_id = CP.product_id)) " +
                            "INNER JOIN product P ON (C.product_id = P.product_id)"
            );
            rs = stmt.executeQuery();
        }
        catch (Exception e) {
            System.out.println(e);
        }
        return rs;
    }

    /* 판매 내역 출력 */
    public void printAllSales() {
        try {
            PreparedStatement outStmt = con.prepareStatement(
                    "SELECT sale_id, created_at, is_income, total_price, payment FROM sale"
            );
            ResultSet outRs = outStmt.executeQuery();

            while (outRs != null && outRs.next()) {
                Long saleId = outRs.getLong(1);
                System.out.println("\n--------------------------");
                System.out.println("판매ID: " + saleId + "  " + "판매일자: " + outRs.getDate(2));
                System.out.println("총 결제액: " + outRs.getInt(4) + "  " + "결제 수단: " +outRs.getString(5));
                System.out.println("\n--------------------------");

                PreparedStatement inStmt = con.prepareStatement(
                        "SELECT name, provider, count, price_paid FROM inventorychange NATURAL JOIN product WHERE sale_id =" + saleId
                );
                ResultSet inRs = inStmt.executeQuery();
                while (inRs != null && inRs.next()) {
                    System.out.print(inRs.getString(1) + " ");
                    System.out.print(inRs.getString(2) + " ");
                    System.out.print(inRs.getInt(3) + " ");
                    System.out.println(inRs.getInt(4));
                }
                System.out.println("\n--------------------------\n");
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }



    /* Cart 생성 */
    private void createCart(String barcode) {
        try {
            PreparedStatement cstmt = con.prepareStatement("SELECT COUNT(*), product_id FROM product WHERE barcode = ?");
            cstmt.setString(1, barcode);
            ResultSet crs = cstmt.executeQuery();

            if (crs.next() && crs.getInt(1) != 0) {
                Long productId = crs.getLong(2);

                ResultSet ccrs = cstmt.executeQuery("SELECT COUNT(*) FROM cart WHERE product_id = " + productId);
                if (ccrs.next() && ccrs.getInt(1) != 0) {
                    // 이미 존재
                    cstmt.execute("UPDATE cart SET count = count + 1 WHERE product_id = " + productId);
                    cstmt.execute("UPDATE cartprice SET count = count + 1, final_price = count * (" +
                            "SELECT regular_price * (100 - IFNULL(rate, 0)) / 100 " +
                            "FROM product p LEFT OUTER JOIN discount d ON (p.product_id = d.product_id) " +
                            "WHERE p.product_id = " + productId +
                            ") WHERE product_id = " + productId);
                }
                else {
                    // 새로 장바구니 넣을 경우
                    cstmt.execute("INSERT INTO cart(count, product_id) VALUES (1, " + productId + ")");
                    cstmt.execute("INSERT INTO cartprice(count, product_id, final_price) VALUES (1, " + productId + ", "
                            + "(SELECT regular_price * (100 - IFNULL(rate, 0)) / 100 AS price " +
                            "FROM product p LEFT OUTER JOIN discount d ON (p.product_id = d.product_id) " +
                            "WHERE p.product_id = " + productId + "))" );
                }
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    /* Cart 담은 상품 줄이기 */
    private void deleteCart(String barcode) {
        try {
            PreparedStatement cstmt = con.prepareStatement("SELECT COUNT(*), product_id FROM product WHERE barcode = ?");
            cstmt.setString(1, barcode);
            ResultSet crs = cstmt.executeQuery();

            if (crs.next() && crs.getInt(1) != 0) {
                Long productId = crs.getLong(2);

                ResultSet ccrs = cstmt.executeQuery("SELECT count FROM cart WHERE product_id = " + productId);
                int nowCount = 0;
                if (ccrs.next() && (nowCount = ccrs.getInt(1)) == 1) {
                    // 1개 남은 걸 없애는 경우
                    cstmt.execute("DELETE FROM cart WHERE product_id = " + productId);
                    cstmt.execute("DELETE FROM cartprice WHERE product_id = " + productId);
                }
                else if (nowCount > 1) {
                    // 2개 이상 존재시
                    cstmt.execute("UPDATE cart SET count = count - 1 WHERE product_id = " + productId);
                    cstmt.execute("UPDATE cartprice SET count = count - 1, final_price = count * (" +
                            "SELECT regular_price * (100 - IFNULL(rate, 0)) / 100 " +
                            "FROM product p LEFT OUTER JOIN discount d ON (p.product_id = d.product_id) " +
                            "WHERE p.product_id = " + productId +
                            ") WHERE product_id = " + productId);
                }
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    /* 담은 상품 모두 지우기 */
    private void deleteAllCart() {
        try {
            PreparedStatement cstmt = con.prepareStatement("DELETE FROM cart");
            cstmt.execute();
            cstmt.execute("DELETE FROM cartprice");
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    /* 결제 */
    private void makePayment() {
        try {
            PreparedStatement stmt = con.prepareStatement("INSERT INTO sale(created_at, is_income, total_price, payment) " +
                    "VALUES (NOW(), FALSE, (SELECT SUM(final_price) FROM cartprice), ?)");
            String payment = getInput("결제 수단");
            stmt.setString(1, payment);
            stmt.execute();

            stmt.execute("INSERT INTO inventorychange(sale_id, product_id, count, price_paid) " +
                    "SELECT LAST_INSERT_ID(), product_id, -count, final_price FROM cart NATURAL JOIN cartprice");

            stmt.execute("UPDATE product P, cartprice C SET P.inventory = (P.inventory - C.count) " +
                    "WHERE P.product_id = C.product_id");

            stmt.execute("DELETE FROM cart");
            stmt.execute("DELETE FROM cartprice");
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    /* 입고 */
    private void receiveProduct() {
        try {
            PreparedStatement stmt = con.prepareStatement("INSERT INTO sale(created_at, is_income, total_price, payment) " +
                    "VALUES (NOW(), TRUE, (SELECT SUM(final_price) FROM cartprice), '입고')");
            stmt.execute();

            stmt.execute("INSERT INTO inventorychange(sale_id, product_id, count, price_paid) " +
                    "SELECT LAST_INSERT_ID(), product_id, count, final_price FROM cart NATURAL JOIN cartprice");

            stmt.execute("UPDATE product P, cartprice C SET P.inventory = (P.inventory + C.count) " +
                    "WHERE P.product_id = C.product_id");

            stmt.execute("DELETE FROM cart");
            stmt.execute("DELETE FROM cartprice");
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }


    // ':' 출력 후 사용자 입력 받기
    private static String getInput(String target) {
        System.out.print(((target.isEmpty()) ? "" : " " + target) + " : ");
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
    }
}
