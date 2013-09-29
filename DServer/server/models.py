#coding=utf-8
from django.db import models
from django.contrib import admin

#app
class App(models.Model):
    appname=models.CharField(max_length=32)
    packagename=models.CharField(max_length=100)
    icon=models.ImageField(upload_to='img/%m-%Y/')
    apk=models.FileField(upload_to='apk/%m-%Y/')
    price=models.FloatField()
    def __unicode__(self):
        return self.appname

class AppAdmin(admin.ModelAdmin):
    list_display = ('appname','packagename','price')
    search_fields = ('appname','packagename','price')

admin.site.register(App,AppAdmin)

#Wallpaper
class Wallpaper(models.Model):
    name=models.CharField(max_length=32)
    image=models.ImageField(upload_to='img/%m-%Y/')
    leftprice=models.FloatField()
    rightprice=models.FloatField()
    maxmoney=models.FloatField()
    url=models.CharField(max_length=100)

    def __unicode__(self):
        return self.name

class WallpaperAdmin(admin.ModelAdmin):
    list_display = ('name','leftprice','rightprice','maxmoney','url')
    search_fields = ('name','leftprice','rightprice','maxmoney','url')

admin.site.register(Wallpaper,WallpaperAdmin)

#mobile user
class Muser(models.Model):
    uname=models.CharField(max_length=30,unique=True)
    upwd=models.CharField(max_length=16)
    uphone=models.CharField(max_length=16)
    def __unicode__(self):
        return self.uname

class MuserAdmin(admin.ModelAdmin):
    list_display = ('uname','uphone')
    search_fields = ('uname','uphone')

admin.site.register(Muser,MuserAdmin)

#mobile device
class Device(models.Model):
    uid=models.CharField(max_length=50,unique=True)
    uname=models.CharField(max_length=30)
    money=models.FloatField()
    downloadcount=models.IntegerField(default=0)
    fathername=models.CharField(max_length=30)
    def __unicode__(self):
        return self.uid

class DeviceAdmin(admin.ModelAdmin):
    list_display = ('uid','uname','money')
    search_fields = ('uid','uname','money')

admin.site.register(Device,DeviceAdmin)

#download task
class downloadtask(models.Model):
    uid=models.CharField(max_length=50)
    packagename=models.CharField(max_length=100)
    time=models.DateTimeField(auto_now=True)
    money=models.FloatField()
    percent=models.FloatField()

    def __unicode__(self):
        return self.uid

class downloadtaskAdmin(admin.ModelAdmin):
    list_display = ('uid','packagename','time','money','percent')
    search_fields = ('uid','packagename','time','money','percent')

admin.site.register(downloadtask,downloadtaskAdmin)


#wallpaper task
class wallpapertask(models.Model):
    uid=models.CharField(max_length=50)
    wallpapername=models.CharField(max_length=100)
    time=models.DateTimeField(auto_now=True)
    lorr=models.IntegerField()
    money=models.FloatField()
    percent=models.FloatField()

    def __unicode__(self):
        return self.uid

class wallpapertaskAdmin(admin.ModelAdmin):
    list_display = ('uid','wallpapername','time','lorr','money','percent')
    search_fields = ('uid','wallpapername','time','lorr','money','percent')

admin.site.register(wallpapertask,wallpapertaskAdmin)


#payrequest
class payrequest(models.Model):
    uid=models.CharField(max_length=50)
    paytype=models.CharField(max_length=100)
    time=models.DateTimeField(auto_now=True)
    spend=models.FloatField()
    status=models.CharField(max_length=50)
    def __unicode__(self):
        return self.uid

class payrequestAdmin(admin.ModelAdmin):
    list_display = ('uid','paytype','time')
    search_fields = ('uid','paytype','time')

admin.site.register(payrequest,payrequestAdmin)

#record
class record(models.Model):
    uid=models.CharField(max_length=50)
    type=models.CharField(max_length=100)
    amount=models.FloatField()
    time=models.DateTimeField(auto_now=True)
    def __unicode__(self):
        return self.uid+self.type

class recordAdmin(admin.ModelAdmin):
    list_display = ('uid','type','amount','time')
    search_fields = ('uid','type','amount','time')

admin.site.register(record,recordAdmin)

#apks
class spread(models.Model):
    uid=models.CharField(max_length=50)
    fid=models.CharField(max_length=100)
    fpth=models.FloatField()
    def __unicode__(self):
        return self.uid

class spreadAdmin(admin.ModelAdmin):
    list_display = ('uid','fid','fpth')
    search_fields = ('uid','fid','fpth')

admin.site.register(spread,spreadAdmin)