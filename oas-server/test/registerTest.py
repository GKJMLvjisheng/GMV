# -*- coding: utf-8 -*-

import jsonapi

#HOST='https://oas.cascv.com:8080/api/v1'
name="a"
password="123456"
token=""

# inquireName
url=HOST +"/userCenter/inquireName"
data={"name": name}
res=jsonapi.post(url,data)
if res.get('code') == 0:
    print ("[PASS] /userCenter/inquireName")
else:
    raise Exception('[FAIL] /userCenter/inquireName')


# register
url=HOST+"/userCenter/register"
data={"name":name, "password":password}
res=jsonapi.post(url,data)
print(res)
if res.get('code') == 0:
  print ("[PASS] /userCenter/register '%s' successfully" % name)
else:
  raise Exception('[FAIL] /userCenter/register')

# registerConfirm
url=HOST +"/userCenter/registerConfirm"
data={"uuid": "USR-0178ea59a6ab11e883290a1411382ce0", "code": 0}
res=jsonapi.post(url,data)
if res.get('code') == 0:
  print ("[PASS] /userCenter/registerConfirm")
else:
    raise Exception('[FAIL] /userCenter/registerConfirm')



