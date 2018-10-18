# -*- coding: utf-8 -*-

import jsonapi

#HOST='https://oas.cascv.com:8080/api/v1'
name="123456"
password="123456"
token=""

# login
url=HOST+"/userCenter/login"
data={"name":name, "password":password}
res=jsonapi.post(url,data)
print(res)
if res.get('code') == 0:
  data=res.get('data')
  token=data.get('token')
  print( "[PASS] /userCenter/login token=%s successfully" % token)
else:
  raise Exception('[FAIL] /userCenter/login')

# inquireUserInfo 
url=HOST+"/userCenter/inquireUserInfo"
data={}
res=jsonapi.post(url,data,token)
print(res)
if res.get('code') == 0:
    print("[PSAA] /userCenter/inquireUserInfo")
else:
    raise Exception('[FAIL] /userCenter/inquireUserInfo')

# destroy
url=HOST+"/userCenter/destroy"
data={}
res=jsonapi.post(url,data,token)
if res.get('code') == 0:
  print ("[PASS] /userCenter/destroy")
else:
  raise Exception('[FAIL] /userCenter/destroy')


