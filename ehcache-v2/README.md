#### 创建CacheManager的几种方法
```
 // I、新建一个实例(单例)，允许接受配置参数(配置类、配置文件名、配置文件流等)，如未传入配置参数，将使用默认配置
 cacheManager  = CacheManager.newInstance() ;
 // II、创建一个实例(单例，内部使用#newInstance()方法实现)，参数同上
 cacheManager  = CacheManager.create() ;
 // III、返回一个实例(单例，内部使用#create()方法实现)，无参
 cacheManager  = CacheManager.getInstance() ;
 // IV、创建一个实例(原型)
 cacheManager  = new CacheManager() ;
 ```