package controllers;

import controllers.WSRequest.Currency;
import controllers.WSRequest.CurrencyByDateRequest;
import gt.gob.banguat.variables.ws.*;
import models.Device;
import models.ErrorType;
import models.Transaction;
import models.TransactionType;
import play.Logger;
import play.mvc.Controller;
import models.User;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.codehaus.jackson.map.ObjectMapper;
import utils.Constants;
import utils.MessageHolder;
import utils.WSClientFactory;

/**
 * Created by renatosierra on 7/11/14.
 */
public class WSGetCurrency  extends Controller {

    public static void getExchangeRate(String data){
        // Starting WS Transaction
        Date today = new Date();
        String formattedDate = new SimpleDateFormat("d/M/yyyy").format(today);
        Transaction transaction = new Transaction();
        transaction.setDate(today);
        transaction.setStatus(Constants.TRANSACTION_STATUS_IN_PROGRESS);
        //Finding Transaction Type by Transaction code
        TransactionType transactionType = TransactionType.find("code = ?",Constants.TRANSACTION_TYPE_GET_DAY_EXCHANGE_RATE).first();
        transaction.setTransactionType(transactionType);

        ObjectMapper objectMapper = new ObjectMapper();

         String exchangeRate ="";
        try{
            Currency request = objectMapper.readValue(data,Currency.class);
            User user = User.findById(request.getUserId());
            if(user!=null){
                transaction.setUser(user);
            }
            Device device = Device.find("deviceid = ? ",request.getDeviceUID()).first();
            if(device != null){
                transaction.setDevice(device);
            }
            //Getting the WS
            TipoCambioSoap tipoCambioSoap = WSClientFactory.getService(TipoCambio.class,TipoCambioSoap.class);
            //Retreiving the information to an object
            Logger.info(formattedDate+"  "+request.getOption());
            DataVariable response = tipoCambioSoap.tipoCambioFechaInicialMoneda(formattedDate,request.getOption());
            //Closing WS
            WSClientFactory.closeService(TipoCambio.class,tipoCambioSoap);
            //Getting information retreived from WS Object
            List<Var> valores = response.getVars().getVar();
            for(Var valor : valores){
                exchangeRate = ""+valor.getVenta();
            }
            //Getting WS Message Info
            MessageHolder.MessageData messageHolderData = MessageHolder.getData();
            //Setting Info to Transaction
            transaction.setRequest(messageHolderData.getRequest());
            transaction.setResponse(messageHolderData.getResponse());
            transaction.setResponseTime(messageHolderData.getElapsedTime());

            transaction.setStatus(Constants.TRANSACTION_STATUS_SUCCESS);
        }catch(Exception e){
            //Getting Exceptions
            ErrorType errorType = ErrorType.find("code = ? ",Constants.ERROR_TYPE_NO_RESPONSE_FROM_SERVER).first();
            transaction.setErrorType(errorType);
            transaction.setStatus(Constants.TRANSACTION_STATUS_FAILED);
        }



        transaction.save();

        render(exchangeRate);
    }
    public static void getExchangeRateByDate(String data){
        // Starting WS Transaction
        Transaction transaction = new Transaction();
        transaction.setDate(new Date());
        transaction.setStatus(Constants.TRANSACTION_STATUS_IN_PROGRESS);
        //Finding Transaction Type by Transaction code
        TransactionType transactionType = TransactionType.find("code = ?",Constants.TRANSACTION_TYPE_GET_EXCHANGE_RATE_BY_DATE).first();
        transaction.setTransactionType(transactionType);
        String exchangeRate ="";
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            //Mapping parameters from the request
            CurrencyByDateRequest request = objectMapper.readValue(data,CurrencyByDateRequest.class);
            User user = User.findById(request.getUserId());
            transaction.setUser(user);
            Device device = Device.find("deviceid = ? ",request.getDeviceUID()).first();
            if(device != null){
                transaction.setDevice(device);
            }
            //Getting the WS
            TipoCambioSoap tipoCambioSoap = WSClientFactory.getService(TipoCambio.class,TipoCambioSoap.class);
            DataVariable response = tipoCambioSoap.tipoCambioRangoMoneda(request.getDate(),request.getDate(),request.getOption());
            //Closing Web Service
            WSClientFactory.closeService(TipoCambio.class,tipoCambioSoap);
            MessageHolder.MessageData messageHolderData = MessageHolder.getData();
            //Setting parameters to the transaction
            transaction.setRequest(messageHolderData.getRequest());
            transaction.setResponse(messageHolderData.getResponse());
            transaction.setResponseTime(messageHolderData.getElapsedTime());
            // Getting Values from response
            ArrayOfVar valores = response.getVars();
            for(Var valor : valores.getVar()){
                exchangeRate = ""+valor.getCompra();
            }
            transaction.setStatus(Constants.TRANSACTION_STATUS_SUCCESS);


        } catch (IOException e) {
            e.printStackTrace();
            ErrorType errorType = ErrorType.find("code = ? ",Constants.ERROR_TYPE_NO_RESPONSE_FROM_SERVER).first();
            transaction.setErrorType(errorType);
            transaction.setStatus(Constants.TRANSACTION_STATUS_FAILED);
        }

        transaction.save();
        render(exchangeRate);
    }
    public static void getCurrencyValues(String data){
        Transaction transaction = new Transaction();
        transaction.setDate(new Date());
        transaction.setStatus(Constants.TRANSACTION_STATUS_IN_PROGRESS);
        //Finding Transaction Type by Transaction code
        TransactionType transactionType = TransactionType.find("code = ?",Constants.TRANSACTION_TYPE_GET_VARIABLES).first();
        transaction.setTransactionType(transactionType);

        ObjectMapper objectMapper = new ObjectMapper();

        List<Variable> variable = new LinkedList<Variable>();
        try{
            Currency request = objectMapper.readValue(data,Currency.class);
            User user = User.findById(request.getUserId());
            if(user!=null){
                transaction.setUser(user);
            }
            Device device = Device.find("deviceid = ? ",request.getDeviceUID()).first();
            if(device != null){
                transaction.setDevice(device);
            }
            //Getting the WS
            TipoCambioSoap tipoCambioSoap = WSClientFactory.getService(TipoCambio.class,TipoCambioSoap.class);
            //Retreiving the information to an object
            InfoVariable response = tipoCambioSoap.variablesDisponibles();
            //Closing WS
            WSClientFactory.closeService(TipoCambio.class,tipoCambioSoap);
            MessageHolder.MessageData messageHolderData = MessageHolder.getData();

            variable = response.getVariables().getVariable();

            transaction.setRequest(messageHolderData.getRequest());
            transaction.setResponse(messageHolderData.getResponse());
            transaction.setResponseTime(messageHolderData.getElapsedTime());

            transaction.setStatus(Constants.TRANSACTION_STATUS_SUCCESS);

        }catch(Exception e){
            //Getting Exceptions
            ErrorType errorType = ErrorType.find("code = ? ",Constants.ERROR_TYPE_NO_RESPONSE_FROM_SERVER).first();
            transaction.setErrorType(errorType);
            transaction.setStatus(Constants.TRANSACTION_STATUS_FAILED);
        }
        transaction.save();

        render(variable);
    }
    public static void requestInformation(String data){


    }

}

