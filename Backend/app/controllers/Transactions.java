package controllers;

import models.Transaction;
import play.Logger;
import play.mvc.Controller;

import java.util.*;

/**
 * Created by renatosierra on 7/29/14.
 */
public class Transactions extends Controller {
    public static void index() {

        List<Transaction> transactions = Transaction.find("order by date desc").from(0).fetch(10);
        render(transactions);
    }
    public static void getTransactions(int start, int length,List<Order> order ,Search search) {
        List<Transaction> transactions;
        String[] columns = {"date","response","responseTime","status","errorType","transactionType","user","device"};
        Logger.info(search.value);
        if(search.value !=""){
           transactions = Transaction.find("select t from Transaction t inner join t.user u where concat(t.date , t.response , t.responseTime ,u.username) like '%"+search.value+"%'   order by "+  columns[order.get(0).column] +" "+ order.get(0).dir ).from(start).fetch(length);
        }else{
           transactions = Transaction.find("order by "+  columns[order.get(0).column] +" "+ order.get(0).dir ).from(start).fetch(length);
        }
        List<Object> data = new LinkedList<Object>();
        Map<String,Object> parentMap = new HashMap<String,Object>();
        Map<String,Object> map;
        for(Transaction transaction : transactions){
            map = new HashMap<String,Object>();
            map.put("date",transaction.getDate().toString());
            //map.put("request",transaction.getRequest());
            //map.put("response",transaction.getResponse());
            map.put("responsetime",transaction.getResponseTime());
            map.put("status",transaction.getStatus());
            map.put("errortype", transaction.getErrorType() != null ? transaction.getErrorType().getName() : "");
            map.put("transactiontype",transaction.getTransactionType().getName());
            map.put("getuser", transaction.getUser()!= null ?  transaction.getUser().getUsername() : "");
            map.put("deviceid", transaction.getDevice() != null ? transaction.getDevice().getDeviceId() : "");
            data.add(map);
        }
        parentMap.put("data", data);
        parentMap.put("recordsTotal",Transaction.count());
        parentMap.put("recordsFiltered",Transaction.count());


        renderJSON(parentMap);
    }
    public class Order{

        public int column;
        public String dir;
    }
    public class Search{

        public String regex;
        public String value;
    }
}
