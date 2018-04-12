# online_origin

This code is for checking destination single IP or Multiple IPs' online status using "PING" and "http Response". it is based on **Streamsets 3.1**



## Getting Started

This source code is based on Streamsets 3.1, Therefore you need to know basis of streamsets and custom origin's concept.

you can find it from https://github.com/streamsets/tutorials/blob/master/tutorial-origin/readme.md



### Prerequisites

I used latest version of IntelliJ and oracle Jdk 8-162. OpenJdk compatibility has not been tested. But should be no problem.

```
Intellij, and JDK 8 or later
```

### Installing

following command should be used to build "online_origin_snapshot.tar.gz".

The "tar.gz" file should be copied into "${Streamsets_root}/user-lib" and decompressed.

```
mvn package -DskipTests 
....
cp [streamsets_root]/etc/user/lib/online_origin[version].tar.gz
tar -xvf online_origin[version].tar.gz
```



Also special permision should be required to get TCP connection and Command console.

open the  "${Streamsets_root}/etc/sdc-security.properties" 

and put line as below;

```
grant codebase "file://${sdc.dist.dir}/user-libs/online_origin/-" {
  permission java.security.AllPermission;
};
```

You Should restart the Streamsets. if you paste files while streamsets is running.

### Running in Streamsets





## Acknowledgments

- Hat tip to anyone who's code was used
- Inspiration
- etc