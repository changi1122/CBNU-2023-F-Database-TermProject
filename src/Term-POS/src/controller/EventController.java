package controller;

import enums.Mode;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class EventController extends Controller {
    Mode mode;

    public EventController(Mode mode) {
        super();
        this.mode = mode;
    }

    @Override
    public Mode getCommand() {
        Scanner sc = new Scanner(System.in);

        String command = sc.nextLine();
        char action = (command.isEmpty()) ? ' ' : command.charAt(0);

        switch(action) {
            case 'N':
            case 'n':
                createDiscount();
                break;
            case 'E':
            case 'e':
                deleteDiscount();
                break;
            case 'X':
            case 'x':
                this.mode = Mode.Product;
                break;
            default:
                break;
        }

        return mode;
    }


    /* 상품 전체 목록 JOIN 후 조회 */
    public ResultSet readDiscount() {
        ResultSet rs = null;
        try {
            PreparedStatement stmt = con.prepareStatement(
                    "SELECT *, regular_price * (100 - rate) / 100 AS price" +
                            " FROM product p LEFT OUTER JOIN discount d ON (p.product_id = d.product_id)"
            );
            rs = stmt.executeQuery();
        }
        catch (Exception e) {
            System.out.println(e);
        }
        return rs;
    }


    /* Discount 테이블 생성 */
    private void createDiscount() {
        try {
            PreparedStatement stmt = con.prepareStatement(
                    "INSERT INTO discount(rate, start_date, end_date, product_id) " +
                            "VALUES (?, ?, ?, ?)"
            );

            int rate = Integer.parseInt(getInput("할인율(정수%)"));
            if (rate < 0 || rate > 100) {
                System.out.print("올바른 할인률을 입력하세요.");
                return;
            }
            stmt.setInt(1, rate);
            stmt.setString(2, getInput("시작일자(2023-00-00)"));
            stmt.setString(3, getInput("종료일자(2023-00-00)"));
            stmt.setInt(4, Integer.parseInt(getInput("상품ID")));
            stmt.execute();
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    /* Discount 테이블 삭제 */
    private void deleteDiscount() {
        try {
            PreparedStatement stmt = con.prepareStatement(
                    "DELETE FROM discount WHERE product_id = ?"
            );
            stmt.setInt(1, Integer.parseInt(getInput("할인을 삭제할 상품 ID")));
            stmt.execute();
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
