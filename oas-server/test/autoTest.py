# -*- coding: utf-8 -*-

import jsonapi

HOST='http://localhost:8080/api/v1'
NAME="zzz"
PASSWORD="123456"
token=""

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
  url=HOST+"/ethHdWallet/testApi"
  data={}
  res=jsonapi.post(url,data,token)
  if res.get('code') == 0:
    data=res.get('data')
    print  "[PASS] /ethHdWallet/"
  else:
    raise Exception('[FAIL] /ethHdWallet/login')

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

if inquireName(NAME) is False:
  print "user already exists, delete it"
  token=login(NAME, PASSWORD)  
  if token is None:
    raise Exception("[FAIL] user login")
  destroy(token)


data=register(NAME, PASSWORD)
print data
if data:
  print "[PASS] user register"
else:
  raise Exception('[FAIL] user register')

token = login(NAME, PASSWORD)
if token is not None:
  print "[PASS] user login "
else:
  raise Exception('[FAIL] user login')

userInfo=inquireUserInfo(token)
print userInfo
if userInfo:
  print "[PASS] inquireUserInfo"

#testapi(token)

#if destroy(token):
#  print "[PASS] user destroy "



  

