from django.conf.urls import patterns, include, url

# Uncomment the next two lines to enable the admin:
from django.contrib import admin
from django.conf import settings

from server.views import getDownTask,addDevice,getSignTask,getWallPaperTask,\
    adddownload,addSign,addpayrequest,addWallpaper,checkDevice

admin.autodiscover()

urlpatterns = patterns('',
    # Examples:
    # url(r'^$', 'DServer.views.home', name='home'),
    # url(r'^DServer/', include('DServer.foo.urls')),

    # Uncomment the admin/doc line below to enable admin documentation:
     url(r'^admin/doc/', include('django.contrib.admindocs.urls')),

    # Uncomment the next line to enable the admin:
    url(r'^admin/', include(admin.site.urls)),
    url(r'^raw/(?P<path>.*)$', 'django.views.static.serve',{'document_root': settings.MEDIA_ROOT },name="raw"),
    url(r'^getDownTask/$',getDownTask),
    url(r'^getSignTask/$',getSignTask),
    url(r'^getWallPaperTask/$',getWallPaperTask),
    url(r'^adddownload/$',adddownload),
    url(r'^addSign/$',addSign),
    url(r'^addpayrequest/$',addpayrequest),
    url(r'^addWallpaper/$',addWallpaper),
    url(r'^addDevice/$',addDevice),
    url(r'^checkDevice/$',checkDevice),
)
