package com.cascv.oas.server.blockchain.config;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;

import lombok.Getter;
import lombok.Setter;

public class TokenClient {
  public static final String EmptyAddress = "0x0000000000000000000000000000000000000000";
  @Getter @Setter private Web3j web3j;
  @Getter @Setter private String contractAddress;
  
  // balance
  public BigInteger getTokenBalance(String fromAddress) {

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
    Transaction transaction = Transaction.createEthCallTransaction(fromAddress, contractAddress, data);

    EthCall ethCall;
    BigInteger balanceValue = BigInteger.ZERO;
    try {
      ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).send();
      List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
      balanceValue = (BigInteger) results.get(0).getValue();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return balanceValue;
  }

  
  // name
  public String getTokenName() {
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
    Transaction transaction = Transaction.createEthCallTransaction(fromAddr, contractAddress, data);

    EthCall ethCall;
    try {
      ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
      List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
      name = results.get(0).getValue().toString();
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }
    return name;
  }

  
  // symbol
  public String getTokenSymbol() {
    String methodName = "symbol";
    String symbol = null;
    List<Type> inputParameters = new ArrayList<>();
    List<TypeReference<?>> outputParameters = new ArrayList<>();

    TypeReference<Utf8String> typeReference = new TypeReference<Utf8String>() {
    };
    outputParameters.add(typeReference);

    Function function = new Function(methodName, inputParameters, outputParameters);

    String data = FunctionEncoder.encode(function);
    Transaction transaction = Transaction.createEthCallTransaction(EmptyAddress, contractAddress, data);

    EthCall ethCall;
    try {
      ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
      List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
      symbol = results.get(0).getValue().toString();
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }
    return symbol;
  }

  // Decimal
  public int getTokenDecimals() {
    String methodName = "decimals";
    int decimal = 0;
    List<Type> inputParameters = new ArrayList<>();
    List<TypeReference<?>> outputParameters = new ArrayList<>();

    TypeReference<Uint8> typeReference = new TypeReference<Uint8>() {
    };
    outputParameters.add(typeReference);

    Function function = new Function(methodName, inputParameters, outputParameters);

    String data = FunctionEncoder.encode(function);
    Transaction transaction = Transaction.createEthCallTransaction(EmptyAddress, contractAddress, data);

    EthCall ethCall;
    try {
      ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
      List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
      decimal = Integer.parseInt(results.get(0).getValue().toString());
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }
    return decimal;
  }

  
  // total supply
  public BigInteger getTokenTotalSupply() {
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
    Transaction transaction = Transaction.createEthCallTransaction(fromAddr, contractAddress, data);

    EthCall ethCall;
    try {
      ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
      List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
      totalSupply = (BigInteger) results.get(0).getValue();
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }
    return totalSupply;
  }
  
  // transfer todo
  public String sendTokenTransaction(String fromAddress, String password, String toAddress, String contractAddress, BigInteger amount) {
    return null;
  }
}
  
