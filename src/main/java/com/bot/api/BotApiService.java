package com.bot.api;

import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.util.*;

@RestController
@CrossOrigin(origins = "*", methods = RequestMethod.GET)
public class BotApiService {

    static String SQLLITE_DB_PATH = "C:\\projects\\db_sqlite_bot_view\\db_bot_view.db";

    @GetMapping("/getProfitList")
    @ResponseBody
    public List<Map<String,Object>> getProfitList( @RequestParam Map<String, Object> params ){

        System.out.println(params.get("from_kst_date"));
        System.out.println(params.get("to_kst_date"));
        System.out.println(params.get("sideRef"));

        Connection connection = null;
        PreparedStatement pstmt = null;
        List<Map<String,Object>> list = new ArrayList<>();
        Map<String, Object> map = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:"+SQLLITE_DB_PATH);
            String sql = "SELECT * FROM TB_COIN_INCOME " +
                    "WHERE date(KST_DATE) BETWEEN ? and ?";

            if( !params.get("sideRef").toString().equals("ALL")){
                sql += " and side = ?";
            }

            pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, params.get("from_kst_date").toString());
            pstmt.setString(2, params.get("to_kst_date").toString());
            if( !params.get("sideRef").toString().equals("ALL"))
                pstmt.setString(3, params.get("sideRef").toString());

            ResultSet rs = pstmt.executeQuery();

            while(rs.next()){
                map = new HashMap<>();
                map.put("no", rs.getInt("no"));
                map.put("tiker", rs.getString("tiker"));
                map.put("scr", rs.getFloat("scr"));
                map.put("tp0", rs.getFloat("tp0"));
                map.put("side", rs.getString("side"));
                map.put("kst_date", rs.getString("kst_date"));
                map.put("income_per", rs.getFloat("income_per"));
                map.put("profit_krw", rs.getInt("profit_krw"));
                map.put("account_krw", rs.getInt("account_krw"));
                map.put("bid_ask_pair_gr", rs.getString("bid_ask_pair_gr"));

                list.add(map);

            }
            rs.close();
            connection.close();
        }
        catch(SQLException e) {
            System.err.println(e.getMessage());
        }

        return list;
    }


}