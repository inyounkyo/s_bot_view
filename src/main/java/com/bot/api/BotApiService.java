package com.bot.api;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*", methods = RequestMethod.GET)
public class BotApiService {

    //@GetMapping("tourreviewUpdateForm/{boardId}/{num}")
    @GetMapping("/getList")
    @ResponseBody
    public List<Map<String,String>> getList(
            /*@PathVariable int boardId, @PathVariable int num, Model model
              @PathVariable Map<String,int> pathMap, Model model
            * */
            @RequestParam(value="id", required=false) String id ) {
            /*
            * int id = pathMap.get("id");
            int num = pathMap.get("num");*/

        List<Map<String,String>> list = new ArrayList<>();

        Map<String, String> m1 = new HashMap<>();
        m1.put("id", "1");
        m1.put("tiker", "BTC");
        m1.put("side", "BID");
        m1.put("date", "2025-04-18 13:34:30");
        m1.put("income_per", "2.34");
        m1.put("profit_krw", "2.34");
        m1.put("ccount_krw", "96,000");

        Map<String, String> m2 = new HashMap<>();
        m2.put("id", "2");
        m2.put("tiker", "DOGE");
        m2.put("side", "ASK");
        m2.put("date", "2025-04-18 13:34:30");
        m2.put("income_per", "2.34");
        m2.put("profit_krw", "2.34");
        m2.put("ccount_krw", "96,000");

        list.add(m1);
        list.add(m2);

        System.out.println(list);

        return list;
    }
}