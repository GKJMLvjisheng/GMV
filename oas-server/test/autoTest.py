# -*- coding: utf-8 -*-

import jsonapi

#HOST='http://18.219.19.160:8080/api/v1'
HOST='http://localhost:8080/api/v1'

# inquireName

def inquireName(name):
  url=HOST +"/userCenter/inquireName"
  data={"name": name}
  res=jsonapi.post(url,data)
  return res.get('code') == 0
    

# register
def register(name, password):
  url=HOST+"/userCenter/register"
  data={"name":name, "password":password}
  res=jsonapi.post(url,data)
  if res.get('code') == 0:
    return res.get('data')

# registerConfirm
def registerConfirm(uuid, code):

  url=HOST +"/userCenter/registerConfirm"
  data={"uuid": uuid, "code": code}
  res=jsonapi.post(url,data)
  return res.get('code') == 0

# login
def login(name, password):
  url=HOST+"/userCenter/login"
  data={"name":name, "password":password}
  res=jsonapi.post(url,data)
  if res.get('code') == 0:
    data=res.get('data')
    return data.get('token')

# testapi
def testapi(token):
  url=HOST+"/ethWallet/testApi"
  data={}
  res=jsonapi.post(url,data,token)
  if res.get('code') == 0:
    data=res.get('data')
    print  ("[PASS] /ethWallet/testApi")
  else:
    raise Exception('[FAIL] /ethWallet/testApi')

def inquireUserInfo(token):
  url=HOST+"/userCenter/inquireUserInfo"
  data={}
  res=jsonapi.post(url,data,token)
  if res.get('code') == 0:
    return res.get('data')

  
# destroy
def destroy(token):
  url=HOST+"/userCenter/destroy"
  data={}
  res=jsonapi.post(url,data,token)
  return res.get('code') == 0

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
  res=jsonapi.post(url,data,token)
  print res
  if res.get('code') == 0:
    return res.get('data')

# inquireEnergyPoint
def inquireEnergyPoint(token):
  url=HOST+"/energyPoint/inquireEnergyPoint"
  data={}
  res=jsonapi.post(url,data,token)
  print res
  if res.get('code') == 0:
    return res.get('data')

# userWalletBalance
def userWalletBalance(token):
  url=HOST+"/userWallet/balanceDetail"
  data={}
  res=jsonapi.post(url,data,token)
  print res
  if res.get('code') == 0:
    return res.get('data')

# ethWalletListCoin
def ethWalletListCoin(token):
  url=HOST+"/ethWallet/listCoin"
  data={}
  res=jsonapi.post(url,data,token)
  print res
  if res.get('code') == 0:
    return res.get('data')
    
    
    
def testEthWallet(name, password):
  destroyUser(name, password)
  data=register(name, password)
  if data is None:
    raise Exception('[FAIL] user register')
  else:
    print "[PASS] user register"
  token = login(name, password)
  if token is None:
    raise Exception('[FAIL] user login')
  else :
    print "[PASS] user login"

  if destroy(token):
    print ("[PASS] user destroy ")

token=login("caikov","cai120501")
if not token:
  raise Exception("[FAIL] login failure")

power=inquirePower(token)
if power is not None:
  print "power is %d" % power
else:
  raise Exception("inquire power failure")

energyPoint=inquireEnergyPoint(token)
if energyPoint is not None:
  print "energy point is %d" % energyPoint
else:
  raise Exception("inquire energypoint failure")

s=userWalletBalance(token)
if s is not None:
  print s
else:
  raise Exception("userWallet balance failure")

coin=ethWalletListCoin(token)
if coin is not None:
  print coin
else:
  raise Exception("eWallet coin failure")




