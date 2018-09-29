package com.cascv.oas.server.blockchain.config;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.tx.ChainId;
import org.web3j.utils.Numeric;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CoinClient {
  public static final String EmptyAddress = "0x0000000000000000000000000000000000000000";
  @Getter @Setter private Map<String, Web3j> providerMap;
  @Getter @Setter private String defaultNet;
  @Getter @Setter private String token;
  
  public Set<String> listNetwork(){
	  if (providerMap == null)
		  return null;
	  return providerMap.keySet();
  }
  
  public BigInteger ethBalance(String net, String fromAddress) {
    BigInteger balance = null;
    try {
      Web3j web3j = providerMap.get(net);
      EthGetBalance ethGetBalance = web3j.ethGetBalance(fromAddress, DefaultBlockParameterName.LATEST).send();
      balance = ethGetBalance.getBalance();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return balance;
  }
  
  // balance
  public BigDecimal balanceOf(String net, String fromAddress, String contract, BigDecimal weiFactor) {
	String methodName = "balanceOf";
    List<Type> inputParameters = new ArrayList<>();
    List<TypeReference<?>> outputParameters = new ArrayList<>();
    Address address = new Address(fromAddress);
    inputParameters.add(address);

    TypeReference<Uint256> typeReference = new TypeReference<Uint256>() {
    };
    outputParameters.add(typeReference);
    Function function = new Function(methodName, inputParameters, outputParameters);
    String data = FunctionEncoder.encode(function);
    Transaction transaction = Transaction.createEthCallTransaction(fromAddress, contract, data);
    
    EthCall ethCall;
    BigInteger balanceInt = BigInteger.ZERO;
        
    try {
      
      Web3j web3j = providerMap.get(net);
      log.info("net {} webj3 {}", net, web3j);
      ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).send();
      List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
      balanceInt = (BigInteger) results.get(0).getValue();
      log.info("balance of results {}", results);
    } catch (IOException e) {
      e.printStackTrace();
    }
    BigDecimal balance = new BigDecimal(balanceInt);
    return balance.divide(weiFactor);
  }

  
  // name
  public String nameOf(String net, String contract) {
    String methodName = "name";
    String name = null;
    String fromAddr = EmptyAddress;
    List<Type> inputParameters = new ArrayList<>();
    List<TypeReference<?>> outputParameters = new ArrayList<>();

    TypeReference<Utf8String> typeReference = new TypeReference<Utf8String>() {
    };
    outputParameters.add(typeReference);

    Function function = new Function(methodName, inputParameters, outputParameters);

    String data = FunctionEncoder.encode(function);
    Transaction transaction = Transaction.createEthCallTransaction(fromAddr, contract, data);

    EthCall ethCall;
    try {
      Web3j web3j = providerMap.get(net); 
      ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
      List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
      name = results.get(0).getValue().toString();
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }
    return name;
  }

  
  // symbol
  public String symbolOf(String net, String contract) {
    String methodName = "symbol";
    String symbol = null;
    List<Type> inputParameters = new ArrayList<>();
    List<TypeReference<?>> outputParameters = new ArrayList<>();

    TypeReference<Utf8String> typeReference = new TypeReference<Utf8String>() {
    };
    outputParameters.add(typeReference);

    Function function = new Function(methodName, inputParameters, outputParameters);

    String data = FunctionEncoder.encode(function);
    Transaction transaction = Transaction.createEthCallTransaction(EmptyAddress, contract, data);

    EthCall ethCall;
    try {
      Web3j web3j = providerMap.get(net); 
      ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
      List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
      symbol = results.get(0).getValue().toString();
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }
    return symbol;
  }

  // Decimal
  public BigDecimal weiFactorOf(String net, String contract) {
    String methodName = "decimals";
    Integer decimal = 0;
    List<Type> inputParameters = new ArrayList<>();
    List<TypeReference<?>> outputParameters = new ArrayList<>();

    TypeReference<Uint8> typeReference = new TypeReference<Uint8>() {
    };
    outputParameters.add(typeReference);

    Function function = new Function(methodName, inputParameters, outputParameters);

    String data = FunctionEncoder.encode(function);
    Transaction transaction = Transaction.createEthCallTransaction(EmptyAddress, contract, data);

    EthCall ethCall;
    try {
      Web3j web3j = providerMap.get(net); 
      ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
      List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
      decimal = Integer.parseInt(results.get(0).getValue().toString());
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }
    return BigDecimal.TEN.pow(decimal);
  }

  
  // total supply
  public BigDecimal supplyOf(String net, String contract, BigDecimal weiFactor) {
    String methodName = "totalSupply";
    String fromAddr = EmptyAddress;
    BigInteger totalSupply = BigInteger.ZERO;
    List<Type> inputParameters = new ArrayList<>();
    List<TypeReference<?>> outputParameters = new ArrayList<>();

    TypeReference<Uint256> typeReference = new TypeReference<Uint256>() {
    };
    outputParameters.add(typeReference);

    Function function = new Function(methodName, inputParameters, outputParameters);

    String data = FunctionEncoder.encode(function);
    Transaction transaction = Transaction.createEthCallTransaction(fromAddr, contract, data);

    EthCall ethCall;
    try {
      Web3j web3j = providerMap.get(net); 
      ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
      List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
      totalSupply = (BigInteger) results.get(0).getValue();
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }
    BigDecimal supply = new BigDecimal(totalSupply);
    return supply.divide(weiFactor);
  }
  
  // transfer todo
  public String transfer(String net, String fromAddress, String privateKey, String toAddress, 
		  String contract, BigInteger amount, BigInteger gasPrice, BigInteger gasLimit) {
	  BigInteger nonce;
	  EthGetTransactionCount ethGetTransactionCount = null;
	  Web3j web3j = providerMap.get(net);
	  try {
		  ethGetTransactionCount = web3j.ethGetTransactionCount(fromAddress, DefaultBlockParameterName.PENDING).send();
	  } catch (IOException e) {
		  e.printStackTrace();
	  }
	  if (ethGetTransactionCount == null) 
		  return null;
	  nonce = ethGetTransactionCount.getTransactionCount();
    BigInteger value = BigInteger.ZERO;
    String methodName = "transfer";
	  List<Type> inputParameters = new ArrayList<>();
	  List<TypeReference<?>> outputParameters = new ArrayList<>();
	  Address tAddress = new Address(toAddress);
	  Uint256 tokenValue = new Uint256(amount);
	  inputParameters.add(tAddress);
	  inputParameters.add(tokenValue);
	  TypeReference<Bool> typeReference = new TypeReference<Bool>() {
	  };
	  outputParameters.add(typeReference);
	  Function function = new Function(methodName, inputParameters, outputParameters);
	  String data = FunctionEncoder.encode(function);

	  byte chainId = ChainId.NONE;
    String signedData = null;
    String txHash = null;
    try {
    	log.info("[transfer] data {}", data);
		  signedData = this.signTransaction(nonce, gasPrice, gasLimit, contract, value, data, chainId, privateKey);
		  if (signedData != null) {
		    EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(signedData).send();
		    txHash=ethSendTransaction.getTransactionHash();
		  }
	  } catch (IOException e) {
      e.printStackTrace();
	  }
    return txHash;
  }
  
  public String multiTransfer(String net, String fromAddress, String privateKey, List<String> toAddress, 
		  String contract, List<BigInteger> amount,BigInteger gasPrice, BigInteger gasLimit) {
	  BigInteger nonce;
	  EthGetTransactionCount ethGetTransactionCount = null;
	  Web3j web3j = providerMap.get(net);
	  try {
	    ethGetTransactionCount = web3j.ethGetTransactionCount(fromAddress, DefaultBlockParameterName.PENDING).send();
    } catch (IOException e) {
	    e.printStackTrace();
    }
	  if (ethGetTransactionCount == null) 
	    return null;
	  nonce = ethGetTransactionCount.getTransactionCount();
	  BigInteger value = BigInteger.ZERO;
	  String methodName = "multiTransfer";
	  List<Type> inputParameters = new ArrayList<>();
	  List<TypeReference<?>> outputParameters = new ArrayList<>();
	  
	  List<Address> tAddress = new ArrayList<>();
	  Integer index = 0;
	  for (String s : toAddress) {
	    log.info("[transfer] address {}", s);
	    tAddress.add(new Address(s));
	    index++;
	  }
	  List<Uint256> tokenValue = new ArrayList<>();
	  index=0;
	  for (BigInteger bint : amount) {
		  log.info("[transfer] amount {}", bint);
		  tokenValue.add(new Uint256(bint));
	    index++;
	  }
    inputParameters.add(new DynamicArray(tAddress));
    inputParameters.add(new DynamicArray(tokenValue));
	  TypeReference<Bool> typeReference = new TypeReference<Bool>() {
	  };
	  outputParameters.add(typeReference);
	  Function function = new Function(methodName, inputParameters, outputParameters);
	  String data = FunctionEncoder.encode(function);
	  byte chainId = ChainId.NONE;
    String signedData = null;
    String txHash = null;
    try {
    	log.info("[multiTransfer] data {}", data);
		  signedData = this.signTransaction(nonce, gasPrice, gasLimit, contract, value, data, chainId, privateKey);
		  if (signedData != null) {
		    EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(signedData).send();
        txHash = ethSendTransaction.getTransactionHash();
		  }
	  } catch (IOException e) {
      e.printStackTrace();
	  }
    return txHash;
  }
  
  public String signTransaction(
		  BigInteger nonce, 
		  BigInteger gasPrice, 
		  BigInteger gasLimit, 
		  String to,
		  BigInteger value, 
		  String data, 
		  byte chainId, 
		  String privateKey) throws IOException {
    byte[] signedMessage;
    RawTransaction rawTransaction = RawTransaction.createTransaction(
        nonce,
        gasPrice,
        gasLimit,
        to,
        value,
        data);

    if (privateKey.startsWith("0x")) {
      privateKey = privateKey.substring(2);
    }
    ECKeyPair ecKeyPair = ECKeyPair.create(new BigInteger(privateKey, 16));
    Credentials credentials = Credentials.create(ecKeyPair);

    if (chainId > ChainId.NONE) {
      signedMessage = TransactionEncoder.signMessage(rawTransaction, chainId, credentials);
    } else {
      signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
    }
    String hexValue = Numeric.toHexString(signedMessage);
    return hexValue;
  }
}
  
