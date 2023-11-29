package view;


import controller.EventController;

import java.sql.Date;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;

public class EventView extends View {

    EventController eventController;
    public EventView(EventController eventController) {
        this.eventController = eventController;
    }

    @Override
    public void show() {
        ResultSet rs = eventController.readDiscount();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try {
            System.out.println("[할인 관리 화면] 새 할인 등록(N), 할인 삭제(E), 할인 관리 종료(X)");
            System.out.printf("---------------------------------------------------------------------------------------------\n");
            System.out.printf("%4s %12s %10s %8s %10s %10s %6s %8s\n",
                    "상품ID", "상품명", "유통사", "할인율", "시작일자", "종료일자", "정가", "할인가");
            System.out.printf("---------------------------------------------------------------------------------------------\n");
            while (rs.next()) {
                Date startDate = rs.getDate(9);
                Date endDate = rs.getDate(10);

                System.out.printf("%4d %12s %10s %8d %16s %16s %6d %8d\n",
                        rs.getLong(1), rs.getString(2), rs.getString(3),
                        rs.getInt(8), (startDate != null) ? sdf.format(startDate) : "-", (endDate != null) ? sdf.format(endDate) : "-",
                        rs.getInt(4), rs.getInt(12)
                );
            }
            System.out.printf("---------------------------------------------------------------------------------------------\n\n\n");
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

}
