# -*- coding: utf-8 -*-

import jsonapi

#HOST='http://52.14.161.120:8080/api/v1'
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

testEthWallet("test12345678", "12345678")




