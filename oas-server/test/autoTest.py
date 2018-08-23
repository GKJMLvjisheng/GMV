# -*- coding: utf-8 -*-

import jsonapi

HOST='http://52.14.161.120:8080/api/v1'
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
  return res.get('code') == 0

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
  print res
  if res.get('code') == 0:
    data=res.get('data')
    print  "[PASS] /ethHdWallet/"
  else:
    raise Exception('[FAIL] /ethHdWallet/login')


  
# destroy
def destroy(token):
  url=HOST+"/userCenter/destroy"
  data={}
  res=jsonapi.post(url,data,token)
  return res.get('code') == 0

if inquireName(NAME) is True:
  print "user already exists, delete it"
  token=login(NAME, PASSWORD)  
  destroy(token)

if register(NAME, PASSWORD):
  print "[PASS] user register"
else:
  raise Exception('[FAIL] user register')

token = login(NAME, PASSWORD)
if token is not None:
  print "[PASS] user login "
else:
  raise Exception('[FAIL] user login')
if destroy(token):
  print "[PASS] user destroy "



  

