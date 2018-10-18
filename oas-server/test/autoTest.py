# -*- coding: utf-8 -*-

import time
import jsonapi
import codecs

#HOST='https://oas.cascv.com:8080/api/v1'
HOST='http://localhost:8080/api/v1'

filename=time.strftime("%Y-%m-%d-%H_%M_%S",time.localtime(time.time())) 
print ("filename %s" % filename)

#file = open(filename,'w+') 
file = codecs.open("result/"+filename,'w+','utf-8') 


def callRpc(url, data, token=None):
  file.write('#########################\n')
  file.write(url+'\n')
  file.write(str(data)+'\n')
  res=None
  if token is not None:
    file.write("token : %s\n" % token)
    res=jsonapi.post(url,data,token)
  else:
    res=jsonapi.post(url,data)
  file.write(str(res)+'\n')
  file.write('#########################')
  return res

# inquireName
def inquireName(name):
  url=HOST +"/userCenter/inquireName"
  data={"name": name}
  res=callRpc(url,data)
  return res.get('code') == 0
    

# register
def register(name, password):
  url=HOST+"/userCenter/register"
  data={"name":name, "password":password}
  res=callRpc(url,data)
  if res.get('code') == 0:
    return res.get('data')

# registerConfirm
def registerConfirm(uuid, code):

  url=HOST +"/userCenter/registerConfirm"
  data={"uuid": uuid, "code": code}
  res=callRpc(url,data)
  return res.get('code') == 0

# login
def login(name, password):
  url=HOST+"/userCenter/login"
  data={"name":name, "password":password}
  res=callRpc(url,data)
  if res.get('code') == 0:
    print (res)
    data=res.get('data')
    return data.get('token')

# testapi
def testapi(token):
  url=HOST+"/ethWallet/testApi"
  data={}
  res=callRpc(url,data,token)
  if res.get('code') == 0:
    data=res.get('data')
    print  ("[PASS] /ethWallet/testApi")
  else:
    raise Exception('[FAIL] /ethWallet/testApi')

def inquireUserInfo(token):
  url=HOST+"/userCenter/inquireUserInfo"
  data={}
  res=callRpc(url,data,token)
  if res.get('code') == 0:
    return res.get('data')

  
# destroy
def destroy(token):
  url=HOST+"/userCenter/destroy"
  data={}
  res=callRpc(url,data,token)
  print (res)
  if res is not None and res.get('code') == 0:
    return res.get('data')

def destroyUser(name, password):
  if inquireName(name) is False:
    print ("user already exists, delete it")
    token=login(name, password)  
    if token is None:
      raise Exception("[FAIL] user login")
    destroy(token)


# inquirePower
def inquirePower(token):
  url=HOST+"/energyPoint/inquirePower"
  data={}
  res=callRpc(url,data,token)
  print (res)
  if res.get('code') == 0:
    return res.get('data')

# inquireEnergyPoint
def inquireEnergyPoint(token):
  url=HOST+"/energyPoint/inquireEnergyPoint"
  data={}
  res=callRpc(url,data,token)
  print (res)
  if res.get('code') == 0:
    return res.get('data')

# userWalletBalance
def userWalletBalance(token):
  url=HOST+"/userWallet/balanceDetail"
  data={}
  res=callRpc(url,data,token)
  print (res)
  if res.get('code') == 0:
    return res.get('data')

# userWalletTransfer
def userWalletTransfer(token,toUserName, amount):
  url=HOST+"/userWallet/transfer"
  data={"toUserName":toUserName,"value":amount}
  res=callRpc(url,data,token)
  print (res)
  if res.get('code') == 0:
    return res.get('data')
    
def energyPointCheckin(token):
  url=HOST+"/energyPoint/checkin"
  data={}
  res=callRpc(url,data,token)
  print (res)
  if res.get('code') == 0:
    return res.get('data')

def inquireEnergyBall(token):
  url=HOST+"/energyPoint/inquireEnergyBall"
  data={}
  res=callRpc(url,data,token)
  print (res)
  if res.get('code') == 0:
    return res.get('data')


def takeEnergyBall(token, ballId):
  url=HOST+"/energyPoint/takeEnergyBall"
  data={"ballId" : ballId}
  res=callRpc(url,data,token)
  print (res)
  if res.get('code') == 0:
    return res.get('data')

def testRegisterDestroy():
  NAME="oases"
  PASSWORD="12345678"
  registerInfo=register(NAME, PASSWORD)
  if registerInfo is not None:
    print ("[PASS] user register")
  else:
    print ("[FALURE] user register")
  token=login(NAME,PASSWORD)
  if not token:
    raise Exception("[FAIL] login failure")
  userInfo=inquireUserInfo(token)
  if userInfo is not None:
    print ("[PASS] inquireUserInfo")
  else:
    raise Exception("[Fail] inquireUserInfo")
  destroy(token)



def testEnergyPoint():
  NAME="caikov"
  PASSWORD="cai120501"
  token=login(NAME,PASSWORD)
  if token is None:
    raise Exception("[Fail] user login")
  power=inquirePower(token)
  if power is not None:
    print ("power is %d" % power)
  else:
    raise Exception("inquire power failure")

  energyPoint=inquireEnergyPoint(token)
  if energyPoint is not None:
    print ("energy point is %d" % energyPoint)
  else:
    raise Exception("inquire energypoint failure")
  checkin=energyPointCheckin(token)
  if checkin is not None:
    print ("[PASS] energyPoint checkin")
  else:
    raise Exception("[Fail] energyPont checkin")

  res=inquireEnergyBall(token)
  if res is not None:
    print ("[PASS] inquireEnergyBall")
  else:
    raise Exception("[Fail] inquireEnergyBall")

  res=takeEnergyBall(token, 1)
  if res is not None:
    print ("[PASS] takeEnergyBall")
  else:
    raise Exception("[Fail] takeEnergyBall")

def userWalletInquireAddress(token):
  url=HOST+"/userWallet/inquireAddress"
  data={}
  res=callRpc(url,data,token)
  print (res)
  if res.get('code') == 0:
    return res.get('data')

def testUserWallet():
  NAME="caikov"
  PASSWORD="cai120501"
  token=login(NAME,PASSWORD)
  if token is None:
    raise Exception("[Fail] user login")
  res=userWalletInquireAddress(token)
  if res is not None:
    print ("[PASS] userWalletInquireAddress")
  else:
    raise Exception("[Fail] userWalletInquireAddress")
  
  res=userWalletBalance(token)
  if res is not None:
    print ("[PASS] userWalletBalance")
  else:
    raise Exception("[Fail] userWalletBalance")

def testEthWallet():
  NAME="caikov"
  PASSWORD="cai120501"
  token=login(NAME,PASSWORD)
  if token is None:
    raise Exception("[Fail] user login")
  coin=ethWalletListCoin(token)
  if coin is not None:
    print (coin)
  else:
    raise Exception("eWallet coin failure")

# ethWalletListCoin
def ethWalletListCoin(token):
  url=HOST+"/ethWallet/listCoin"
  data={}
  res=callRpc(url,data,token)
  print (res)
  if res.get('code') == 0:
    return res.get('data.userCoin')

def ethWalletTransfer(token):
  url=HOST+"/ethWallet/transfer"
  data={
    "password":"de4aeffa0d09799523973aae39e5d1dd7e756b87e7b56bd50962d333865b9aa5",
    "contract":"0x6Fe2542DC2B902141C14F085A646A38cDF0BeecF",
    "toUserAddress":"0xb2158c7cf8472bff7145435623f3f8243608da0d",
    "amount":0.001
  }
  res=callRpc(url,data,token)
  print (res)
  if res.get('code') == 0:
    return res.get('data')

def ethWalletMultiTransfer(token):
  url=HOST+"/ethWallet/multiTtransfer"
  data={
    "password":"de4aeffa0d09799523973aae39e5d1dd7e756b87e7b56bd50962d333865b9aa5",
    "contract":"0x6Fe2542DC2B902141C14F085A646A38cDF0BeecF",
    "quota":[
      {"toUserAddress":"0xb2158c7cf8472bff7145435623f3f8243608da0d","amount":0.001},
      {"toUserAddress":"0x762be26b248c3e0a0720ed829af178325198ad4f","amount":0.001}
    ]
  }
  res=callRpc(url,data,token)
  print (res)
  if res.get('code') == 0:
    return res.get('data')

def testUserWalletTransfer():
  fromUserName="caikov"
  fromPassword="cai120501"
  toUserName="cai002"
  amount=15.0
  token=login(fromUserName,fromPassword)
  
  if token is not None:
    print ("[PASS] user login")
  else:
    raise Exception("[Fail] user login")
    
  res=userWalletTransfer(token,toUserName, amount)
  if res is not None:
    print (res)
  else:
    raise Exception("[Fail] user wallet transfer")

def inquirePointFactor(token):
  url=HOST+"/energyPoint/inquirePointFactor"
  data={
    "date":"2018-09",
  }
  res=callRpc(url,data,token)
  print (res)
  if res.get('code') == 0:
    return res.get('data')


def redeemPoint(token):
  url=HOST+"/energyPoint/redeemPoint"
  data={
    "date":"2018-09",
  }
  res=callRpc(url,data,token)
  print (res)
  if res.get('code') == 0:
    return res.get('data')
 


def inquireEnergyPointDetail(token):
  url=HOST+"/energyPoint/inquireEnergyPointDetail"
  data={
    "pageNum" : 1,
    "pageSize" : 3
  }
  res=callRpc(url,data,token)
  print (res)
  if res.get('code') == 0:
    return res.get('data')

def excchangeRateInquire(token):    
  url=HOST+"/exchange/inquire"
  data={
    "time" : "2018-09",
    "currency" : 2
  }
  res=callRpc(url,data,token)
  print (res)
  if res.get('code') == 0:
    return res.get('data')

def inquireCurrentPeriodEnergyPoint(token):
  url=HOST+"/energyPoint/inquireCurrentPeriodEnergyPoint"
  data={
  }
  res=callRpc(url,data,token)
  print (res)
  if res.get('code') == 0:
    return res.get('data')

def inquireNews(token):
  url=HOST+"/energyPoint/inquireNews"
  data={
    "pageSize":3,
    "pageNum" :1
  }
  res=callRpc(url,data,token)
  print (res)
  if res.get('code') == 0:
    return res.get('data')

inquireNews(None)
    
#testUserWalletTransfer()

#testRegisterDestroy()

#token=login("admin","123")
#if token is None:
#  raise Exception("[Fail] login")

#coin=ethWalletListCoin(token)
#if coin is not None:
#  print (coin)
#else:
#  raise Exception("eWallet coin failure")

#register("cai000016", "123");

#testEnergyPoint()
#testUserWallet()
#testEthWallet()

file.close()

