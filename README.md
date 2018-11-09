# fight
###learn

###jar怎么上传到本地仓库
mvn install:install-file -Dfile=C:\Downloads\pinyin4j-2.5.0.jar -DgroupId=net.sourceforge -DartifactId=pinyin4j -Dversion=2.5.0 -Dpackaging=jar
mvn install:install-file -Dfile=D:\Development\AAA\sqljdbc42.jar -Dpackaging=jar -DgroupId=com.microsoft.sqlserver -DartifactId=sqljdbc4 -Dversion=4.0
###mysql修改time_zone
####1.mysql -uroot
####2.show variables like '%time_zone%';
####3.set global time_zone = '+08:00';

###RabbitMQ环境变量设置
####1.ERLANG_HOME=D:\Applications\erl10.0.1
####2.Path=%ERLANG_HOME%\bin
####3.rabbitmq-plugins enable rabbitmq_management命令进行安装
####4.http://localhost:15672