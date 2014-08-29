package controllers;

import models.Transaction;
import play.Logger;
import play.data.binding.As;
import play.data.validation.Required;
import play.data.validation.Validation;
import play.mvc.Controller;
import play.mvc.results.RenderJson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by renatosierra on 7/29/14.
 */
public class Transactions extends Controller {
    public static void index() {

        List<Transaction> transactions = Transaction.find("order by date desc").from(0).fetch(10);
        render(transactions);
    }
    public static void getTransactions(int start, int length,List<Order> order ,Search search,@Required @As("MM/dd/yyyy") Date initDate ,@Required @As("MM/dd/yyyy") Date endDate,String username,String device) {

        if(Validation.hasErrors()){
            renderJSON("{\"success\":false}");
        }

        List<Transaction> transactions;
        String[] columns = {"t.date","t.response","t.responseTime","t.status","t.errorType","t.transactionType","u.username","d.deviceId"};
        transactions = Transaction.find("select t from Transaction t inner join t.user u inner join t.device d  where  cast(t.date as date) between ? and ?  and  u.username like ? and d.deviceId like ? order by "+  columns[order.get(0).column] +" "+ order.get(0).dir, initDate, endDate, username+'%', device+'%').from(start).fetch(length);
        Long transactionCout = Transaction.count("from Transaction t inner join t.user u inner join t.device d  where  cast( t.date as date ) between ? and ?  and  u.username like ? and d.deviceId like ? order by "+  columns[order.get(0).column] +" "+ order.get(0).dir, initDate, endDate, username+'%', device+'%');
        List<Object> data = new LinkedList<Object>();
        Map<String,Object> parentMap = new HashMap<String,Object>();
        Map<String,Object> map;
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        for(Transaction transaction : transactions){
            map = new HashMap<String,Object>();
            map.put("date",df.format(transaction.getDate()) );
            map.put("request",transaction.getRequest());
            map.put("response",transaction.getResponse());
            map.put("responsetime",transaction.getResponseTime());
            map.put("status",transaction.getStatus()==0? "<a class=\"a-label-status-active\">In Progress</a>": transaction.getStatus()== 1 ? "<a  class=\"a-label-status-blocked\">Failed</a>":"<a class=\"a-label-status-success\">Success</a>");
            map.put("errortype", transaction.getErrorType() != null ? transaction.getErrorType().getName() : "");
            map.put("transactiontype",transaction.getTransactionType().getName());
            map.put("getuser", transaction.getUser()!= null ?  transaction.getUser().getUsername() : "");
            map.put("deviceid", transaction.getDevice() != null ? transaction.getDevice().getDeviceId() : "");
            data.add(map);
        }
        parentMap.put("data", data);
        parentMap.put("recordsTotal",Transaction.count());
        parentMap.put("recordsFiltered",transactionCout);

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
