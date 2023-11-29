package controller;

import enums.Mode;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class ProductController extends Controller {
    Mode mode;

    public ProductController(Mode mode) {
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
                createProduct();
                break;
            case 'D':
            case 'd':
                this.mode = Mode.Event;
                break;
            case 'X':
            case 'x':
                this.mode = Mode.Sale;
                break;
            default:
                break;
        }

        return mode;
    }


    /* 상품 전체 목록 JOIN 후 조회 */
    public ResultSet readProduct() {
        ResultSet rs = null;
        try {
            PreparedStatement stmt = con.prepareStatement(
                    "SELECT *" +
                            "FROM (product NATURAL JOIN provider) NATURAL  JOIN  origin"
            );
            rs = stmt.executeQuery();
        }
        catch (Exception e) {
            System.out.println(e);
        }
        return rs;
    }


    /* Product 테이블 생성 */
    private void createProduct() {
        try {
            PreparedStatement stmt = con.prepareStatement(
            "INSERT INTO product(name, provider, regular_price, barcode, inventory) " +
                "VALUES (?, ?, ?, ?, ?)"
            );

            stmt.setString(1, getInput("제품명"));
            String provider = getInput("유통사");
            stmt.setString(2, provider);
            stmt.setInt(3, Integer.parseInt(getInput("정가(정수)")));
            String barcode = getInput("바코드 번호");
            stmt.setString(4, barcode);
            stmt.setInt(5, Integer.parseInt(getInput("재고(정수)")));
            stmt.execute();

            createProvider(provider);
            createOrigin(barcode);
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    /* Provider 테이블 생성 */
    private void createProvider(String provider) {
        try {
            PreparedStatement stmt = con.prepareStatement(
                "SELECT * FROM provider WHERE provider = ?"
            );
            stmt.setString(1, provider);
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) {
                PreparedStatement cstmt = con.prepareStatement(
                    "INSERT INTO provider(provider, provider_address) VALUES (?, ?)"
                );
                cstmt.setString(1, provider);
                cstmt.setString(2, getInput("유통사 주소"));
                cstmt.execute();
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    /* Origin 테이블 생성 */
    private void createOrigin(String barcode) {
        try {
            PreparedStatement stmt = con.prepareStatement(
                    "SELECT * FROM origin WHERE barcode = ?"
            );
            stmt.setString(1, barcode);
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) {
                PreparedStatement cstmt = con.prepareStatement(
                        "INSERT INTO origin(barcode, origin_country) VALUES (?, ?)"
                );
                cstmt.setString(1, barcode);
                cstmt.setString(2, getInput("제조국"));
                cstmt.execute();
            }
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
