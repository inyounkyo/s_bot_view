package com.bot.api;

import com.sun.source.tree.CompilationUnitTree;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.util.*;

@RestController
@CrossOrigin(origins = "*", methods = RequestMethod.GET)
public class BotApiService {

    static String SQLLITE_DB_PATH = "C:\\projects\\db_sqlite_bot_view\\db_bot_view.db";

    @GetMapping("/getPageing")
    @ResponseBody
    public List<Map<String,Object>> getPageing( @RequestParam Map<String, Object> params ){

        System.out.println(params.get("from_kst_date"));
        System.out.println(params.get("to_kst_date"));
        System.out.println(params.get("sideRef"));
        System.out.println("currPg: "+params.get("currPg"));
        System.out.println("PAGE_ROW_GB: "+params.get("PAGE_ROW_GB"));

        int PAGE_ROW_GB = Integer.parseInt(params.get("PAGE_ROW_GB").toString());

        Connection connection = null;
        PreparedStatement pstmt = null;
        List<Map<String,Object>> list = new ArrayList<>();
        Map<String, Object> map = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:"+SQLLITE_DB_PATH);

            String sql = "SELECT A1.*\n" +
                    ", CASE\n" +
                    " WHEN A1.TOT_ROW % "+PAGE_ROW_GB+" = 0 \n" +
                    " THEN A1.TOT_ROW / "+PAGE_ROW_GB+" \n" +
                    " ELSE (A1.TOT_ROW / "+PAGE_ROW_GB+")+1 \n" +
                    " END AS TOT_PAGE\n" +
                    " FROM(\n" +
                    "  SELECT A.NO\n" +
                    "   , A.TIKER\n" +
                    "   , A.SCR\n" +
                    "   , A.TP0\n" +
                    "   , A.SIDE\n" +
                    "   , A.KST_DATE\n" +
                    "   , A.INCOME_PER\n" +
                    "   , A.PROFIT_KRW\n" +
                    "   , A.ACCOUNT_KRW\n" +
                    "   , A.BID_ASK_PAIR_GR\n" +
                    "   , ROW_NUMBER() OVER(ORDER BY A.KST_DATE DESC ) AS DESC_NO\n" +
                    "   , COUNT(A.NO) OVER() TOT_ROW\n" +
                    "FROM TB_COIN_INCOME A\n" +
                    " WHERE DATE(A.KST_DATE) BETWEEN ? AND ?\n" +
                    ")A1\n" +
                    "LIMIT "+PAGE_ROW_GB+" * ("+Integer.parseInt(params.get("currPg").toString())+"-1),"+PAGE_ROW_GB;

//            if( !params.get("sideRef").toString().equals("ALL")){
//                sql += " and side = ?";
//            }

            //System.out.println(sql);
            pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, params.get("from_kst_date").toString());
            pstmt.setString(2, params.get("to_kst_date").toString());
            //if( !params.get("sideRef").toString().equals("ALL"))
            //    pstmt.setString(3, params.get("sideRef").toString());

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
                map.put("DESC_NO", rs.getString("DESC_NO"));
                map.put("TOT_ROW", rs.getString("TOT_ROW"));
                map.put("TOT_PAGE", rs.getString("TOT_PAGE"));
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