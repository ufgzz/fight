# fight
learn
###jar怎么上传到本地仓库
mvn install:install-file -Dfile=C:\Downloads\pinyin4j-2.5.0.jar -DgroupId=net.sourceforge -DartifactId=pinyin4j -Dversion=2.5.0 -Dpackaging=jar
###mysql修改time_zone
1.mysql -uroot
2.show variables like '%time_zone%';
3.set global time_zone = '+08:00';