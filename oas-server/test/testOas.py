# -*- coding: utf-8 -*-

import jsonapi

HOST='http://52.14.161.120:8080/api/v1'
name="test1111"
password="12345678"
token=""

# register
url=HOST+"/userCenter/register"
data={"name":name, "password":password}
res=jsonapi.post(url,data)
if res.get('code') == 0:
  print "[PASS] /userCenter/register '%s' successfully" % name
else:
  raise Exception('[FAIL] /userCenter/register')

# login
url=HOST+"/userCenter/login"
data={"name":name, "password":password}
res=jsonapi.post(url,data)
if res.get('code') == 0:
  data=res.get('data')
  token=data.get('token')
  print "[PASS] /userCenter/login token=%s successfully" % token
else:
  raise Exception('[FAIL] /userCenter/login')


# destroy
url=HOST+"/userCenter/destroy"
data={}
res=jsonapi.post(url,data,token)
if res.get('code') == 0:
  print "[PASS] /userCenter/destroy"
else:
  raise Exception('[FAIL] /userCenter/destroy')

