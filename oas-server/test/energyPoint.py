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

# inquireEnergyBall
url=HOST +"/energyPoint/inquireEnergyBall"
data={}
res=jsonapi.post(url,data,token)
print(res)
if res.get('code') == 0:
    print ("[PASS] /energyPoint/inquireEnergyBall")
else:
    raise Exception('[FAIL] /energyPoint/inquireEnergyBall')

# takeEnergyBall
url=HOST +"/energyPoint/takeEnergyBall"
data={}
res=jsonapi.post(url,data,token)
print(res)
if res.get('code') == 0:
    print ("[PASS] /energyPoint/takeEnergyBall")
else:
    raise Exception('[FAIL] /energyPoint/takeEnergyBall')

# inquireEnergyPoint
url=HOST +"/energyPoint/inquireEnergyPoint"
data={}
res=jsonapi.post(url,data,token)
print(res)
if res.get('code') == 0:
    print ("[PASS] /energyPoint/inquireEnergyPoint")
else:
    raise Exception('[FAIL] /energyPoint/inquireEnergyPoint')

# inquirePower
url=HOST +"/energyPoint/inquirePower"
data={}
res=jsonapi.post(url,data,token)
print(res)
if res.get('code') == 0:
    print ("[PASS] /energyPoint/inquirePower")
else:
    raise Exception('[FAIL] /energyPoint/inquirePower')



