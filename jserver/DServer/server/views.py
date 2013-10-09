#-*-coding:utf-8-*-
from django.http import HttpResponse
from django.db import connection,transaction
from models import Device,Muser,App,downloadtask,wallpapertask,Wallpaper,record,payrequest,spread,feedback
from django.core.files.storage import default_storage
from datetime import *
import  time
import  json
import pytz

def checkDevice(request):
    res={'code':1,'message':''}
    try:
        dev=Device.objects.get(uid=request.GET['uid']);
    except:
        return HttpResponse(json.dumps(res))
    res['code']=0
    res['version']='{"version":"1.1","url":"http://61.147.112.27:8000/spread/zmdr.apk"}'
    res['devmoney']=dev.money
    dict={}
    dict['allin']=dev.allin
    dict['allout']=dev.allout
    dict['downin']=dev.downin
    dict['signin']=dev.signin
    dict['wallpaperin']=dev.wallpaperin
    dict['spreadin']=dev.spreadin
    dict['quickin']=dev.quickin
    dict['money']=dev.money
    dict['spreadnum']=dev.spreadnum
    dict['activenum']=dev.activenum
    res['message']=json.dumps(dict)
    return HttpResponse(json.dumps(res))

def getSpread(request):
    res={'code':1,'message':''}
    try:
        sp=spread.objects.get(uid=request.GET['uid']);
        res['code']=0
        res['message']=sp.fid
        return HttpResponse(json.dumps(res))
    except:
        #if True:
        try:
            sp=spread.objects.filter(uid="")[0]
            sp.uid=request.GET['uid']
            sp.save()
            res['code']=0
            res['message']=sp.fid
            return HttpResponse(json.dumps(res))
        #else:
        except:
            return HttpResponse(json.dumps(res))

def getPayRecord(request):
    res={'code':1,'message':''}
    #if True:
    try:
        prq=payrequest.objects.filter(uid=request.GET['uid']).order_by('-time')[0:10]
    #else:
    except:
        return HttpResponse(json.dumps(res))
    all=[]
    for item in prq:
        dict={}
        dict['info']=item.paytype
        dict['time']=str(item.time)
        dict['status']=item.status
        all.append(dict)
    res['message']=json.dumps(all)
    res['code']=0
    return HttpResponse(json.dumps(res))

def addDevice(request):
    res={'code':1,'message':'错误，请重试'}
    try:
        reg_uid=request.GET['uid']
        reg_uname=request.GET['uname']
        reg_upwd=request.GET['upwd']
        reg_father=request.GET['father']
    except:
        return HttpResponse(json.dumps(res))
    reged=False
    try:
        device=Device.objects.get(uid=reg_uid)
    except Device.DoesNotExist:
        reged=False
    else:
        reged=True
    if(reged):
        res['code']=0
        res['message']='设备曾经注册，可直接使用'
        return HttpResponse(json.dumps(res))
    else:
        reged=False
        try:
            user=Device.objects.get(uname=reg_uname)
        except Device.DoesNotExist:
            reged=False
        else:
            reged=True
        if reged:
            res['message']='用户名已被注册'
            return HttpResponse(json.dumps(res))
        else:
            device=Device()
            device.uid=reg_uid
            device.uname=reg_uname
            try:
                sp=spread.objects.get(fid=reg_father)
                device.fathername=sp.uid
                fdev=Device.objects.get(uid=sp.uid)
                fdev.spreadnum+=1
                fdev.save()
            except:
                pass
            device.money=0
            user=Muser()
            user.uname=reg_uname
            user.upwd=reg_upwd
            device.save()
            user.save()
            res['code']=0
            res['message']='注册成功'
            return HttpResponse(json.dumps(res))


def getDownTask(request):
    res={'code':1,'message':''}
    try:
        uid=request.GET['uid']
        cursor=connection.cursor()
        sql="select appname,packagename,icon,apk,price from server_app where packagename not in (select distinct packagename from server_downloadtask where uid='%s')" % (uid)
        cursor.execute(sql)
        all=cursor.fetchall()
        apps=[]
        for item in all:
            app=App.objects.get(appname=item[0])
            dic={}
            dic['appname']=item[0]
            dic['packagename']=item[1]
            dic['icon']=app.icon.url
            if app.apk and len(app.apk.url)>4:
                dic['apk']=app.apk.url
            elif app.apkurl and len(app.apkurl)>4:
                dic['apk']=app.apkurl
            else:
                continue
            dic['price']=item[4]
            apps.append(dic)
        res['message']=json.dumps(apps)
    except:
        return HttpResponse(json.dumps(res))
    res['code']=0
    return HttpResponse(json.dumps(res))

def getSignTask(request):
    res={'code':1,'message':''}
    #if True:
    try:
        uid=request.GET['uid']
        cursor=connection.cursor()
        sql="select appname,packagename,icon,apk,price from server_app where packagename in (select distinct packagename from server_downloadtask where uid='%s' and percent <1)" % (uid)
        cursor.execute(sql)
        all=cursor.fetchall()
        apps=[]
        for item in all:
            app=App.objects.get(appname=item[0])
            downtask=downloadtask.objects.get(packagename=item[1],uid=uid)
            dic={}
            dic['appname']=item[0]
            dic['packagename']=item[1]
            dic['icon']=app.icon.url
            if app.apk and len(app.apk.url)>4:
                dic['apk']=app.apk.url
            elif app.apkurl and len(app.apkurl)>4:
                dic['apk']=app.apkurl
            else:
                continue
            dic['price']=item[4]
            dic['money']=downtask.money
            dic['percent']=downtask.percent
            dic['time']=downtask.time.isoformat()
            now=datetime.now(tz=pytz.utc)
            timespan = timedelta(days=1)
            delta=now-downtask.time
            dic['intro']='当前第'+str(delta.days+1)+"天"
            if True or delta.days>-1:
                apps.append(dic)
        res['message']=json.dumps(apps)
    except:
    #else:
        return HttpResponse(json.dumps(res))
    res['code']=0
    return HttpResponse(json.dumps(res))

def getWallPaperTask(request):
    res={'code':1,'message':''}
    #if True:
    try:
        uid=request.GET['uid']
        new_task=Wallpaper.objects.exclude(name__in=wallpapertask.objects.filter(uid=uid).values_list('wallpapername',flat=True))
        old_task=wallpapertask.objects.filter(uid=uid,percent__lt=1)
        tasks=[]
        for item in new_task:
            dic={}
            dic['name']=item.name
            dic['image']=item.image.url
            dic['leftprice']=item.leftprice
            dic['rightprice']=item.rightprice
            dic['maxmoney']=item.maxmoney
            dic['url']=item.url
            dic['percent']=0
            tasks.append(dic)
        for item in old_task:
            temp=Wallpaper.objects.get(name=item.wallpapername)
            dic={}
            dic['name']=temp.name
            dic['image']=temp.image.url
            dic['leftprice']=temp.leftprice
            dic['rightprice']=temp.rightprice
            dic['maxmoney']=temp.maxmoney
            dic['percent']=item.percent
            dic['url']=temp.url
            tasks.append(dic)
        res['message']=json.dumps(tasks)
    except:
    #else:
        return HttpResponse(json.dumps(res))
    res['code']=0
    return HttpResponse(json.dumps(res))

def addSign(request):
    res={'code':1,'message':''}
    try:
        uid=request.GET['uid']
        package=request.GET['packagename']
    except:
        return HttpResponse(json.dumps(res))
    #if True:
    try:
        moneyChanged=0
        temp=downloadtask.objects.filter(packagename=package,uid=uid,percent__lt=1)
        if len(temp)==0:
            res['code']=0
            res['message']=0
            return HttpResponse(json.dumps(res))
        item=temp[0]
        lasttime=item.time
        now=datetime.now(tz=pytz.utc)
        delta=now-lasttime
        app=App.objects.get(packagename=package)
        if item.percent<.6 and delta.days==1:
            print 'run'
            per=.3
            moneyChanged=app.price*per
            item.money=item.money+moneyChanged
            item.percent=item.percent+per
            item.save()
            dev=Device.objects.get(uid=uid)
            dev.money+=moneyChanged
            dev.signin+=moneyChanged
            dev.allin+=moneyChanged
            dev.save()
            log=record()
            log.uid=uid
            log.type='sign:'+package
            log.amount=moneyChanged
            log.save()
        elif item.percent<.9 and delta.days==4:
            per=.2
            moneyChanged=app.price*per
            item.money=item.money+moneyChanged
            item.percent=item.percent+per
            item.save()
            dev=Device.objects.get(uid=uid)
            dev.money+=moneyChanged
            dev.signin+=moneyChanged
            dev.allin+=moneyChanged
            dev.save()
            log=record()
            log.uid=uid
            log.type='sign:'+package
            log.amount=moneyChanged
            log.save()
    #else:
    except:
        return HttpResponse(json.dumps(res))
    res['code']=0
    res['message']=moneyChanged
    #res['message']=str(delta.days)
    return HttpResponse(json.dumps(res))

def adddownload(request):
    res={'code':1,'message':''}
    try:
        uid=request.GET['uid']
        package=request.GET['packagename']
    except:
        return HttpResponse(json.dumps(res))
    #if True:
    try:
        temp=downloadtask.objects.filter(packagename=package,uid=uid,)
        if len(temp)>0:
            res['code']=0
            res['message']=0
            return HttpResponse(json.dumps(res))
        app=App.objects.get(packagename=package)
        item=downloadtask()
        item.uid=uid
        item.money=app.price*.5
        item.packagename=package
        item.percent=.5
        item.save()
        dev=Device.objects.get(uid=uid)
        dev.money+=item.money
        dev.downin+=item.money
        dev.allin+=item.money
        dev.downloadcount+=1
        dev.save()
        log=record()
        log.uid=uid
        log.type='download:'+package
        log.amount=item.money
        log.save()

        if(dev.fathername!=None and len(dev.fathername)>4):
            try:
                fatherMoney=item.money*.2
                faDev=Device.objects.get(uid=dev.fathername)
                faDev.money+=fatherMoney
                faDev.spreadin+=fatherMoney
                faDev.allin+=fatherMoney
                log2=record()
                log2.uid=dev.fathername
                log2.type="sondownload:"+package
                log2.amount=fatherMoney
                log2.save()
                if dev.downloadcount==3:
                    faDev.activenum+=1
                    faDev.money+=1.5
                    faDev.spreadin+=1.5
                    faDev.allin+=1.5
                    log3=record()
                    log3.uid=dev.fathername
                    log3.type="sonachieve:"+dev.downloadcount
                    log3.amount=1.5
                    log3.save()
                faDev.save()
            except:
                pass
    #else:
    except:
        return HttpResponse(json.dumps(res))
    res['code']=0
    res['message']=item.money
    return HttpResponse(json.dumps(res))

def addWallpaper(request):
    res={'code':1,'message':''}
    try:
        uid=request.GET['uid']
        wallpapername=request.GET['wallpapername']
        leftorright=request.GET['lorr']
    except:
        res['message']='paramerr'
        return HttpResponse(json.dumps(res))
    #if True:
    try:
        temp=wallpapertask.objects.filter(wallpapername=wallpapername,uid=uid)
        if len(temp)==0:
            item=wallpapertask()
            item.wallpapername=wallpapername
            item.uid=uid
            item.lorr=-1
            item.money=0
            item.percent=0
            item.save()
        temp=wallpapertask.objects.filter(wallpapername=wallpapername,uid=uid)
        if len(temp)>0:
            item=temp[0]
            paper=Wallpaper.objects.get(name=wallpapername)
            changeMoney=0
            if leftorright=='0':
                changeMoney=paper.leftprice if (item.money+paper.leftprice<=paper.maxmoney) else (paper.maxmoney-item.money)
            else:
                changeMoney=paper.rightprice if (item.money+paper.rightprice<=paper.maxmoney) else (paper.maxmoney-item.money)
            item.money+=changeMoney
            item.percent=item.money/paper.maxmoney
            dev=Device.objects.get(uid=uid)
            dev.money+=changeMoney
            dev.wallpaperin+=changeMoney
            dev.allin+=changeMoney
            dev.save()
            log=record()
            log.uid=uid
            log.type='wallpaper:'+wallpapername+str(leftorright)
            log.amount=changeMoney
            log.save()
            item.save()
    #else:
    except:
        return HttpResponse(json.dumps(res))
    res['code']=0
    res['message']=item.money
    return HttpResponse(json.dumps(res))

def addpayrequest(request):
    res={'code':1,'message':''}
    try:
        uid=request.GET['uid']
        paytype=request.GET['paytype']
        spend=float(request.GET['spend'])
    except:
        return HttpResponse(json.dumps(res))
    #if True:
    try:
        device=Device.objects.get(uid=uid)
        if device.money<spend:
            res['message']='余额不足'
            return HttpResponse(json.dumps(res))
        device.money-=spend
        device.allout+=spend
        device.save()
        item=payrequest()
        item.uid=uid
        item.paytype=paytype
        item.spend=spend
        item.status='wait'
        item.save()
        log=record()
        log.uid=uid
        log.type='payrequest:'+paytype
        log.amount=spend
        log.save()
    #else:
    except:
        return HttpResponse(json.dumps(res))
    res['code']=0
    res['message']='申请成功'
    return HttpResponse(json.dumps(res))

def addfeedback(request):
    res={'code':1,'message':''}
    try:
        uid=request.GET['uid']
        txt=request.GET['txt']
    except:
        return HttpResponse(json.dumps(res))
        #if True:
    try:
       feed=feedback()
       feed.uid=uid
       feed.txt=txt
       feed.save()
    #else:
    except:
        return HttpResponse(json.dumps(res))
    res['code']=0
    res['message']='ok'
    return HttpResponse(json.dumps(res))

def addquickin(request):
    res={'code':1,'message':''}
    try:
        uid=request.GET['uid']
        coin=float(request.GET['coin'])
    except:
        return HttpResponse(json.dumps(res))
        #if True:
    try:
        device=Device.objects.get(uid=uid)
        money=coin/1000.0
        device.money+=money
        device.allin+=money
        device.quickin+=money
        device.save()
        log=record()
        log.uid=uid
        log.type='quick:'+str(money)
        log.amount=money
        log.save()
    #else:
    except:
        return HttpResponse(json.dumps(res))
    res['code']=0
    res['message']='申请成功'
    return HttpResponse(json.dumps(res))
