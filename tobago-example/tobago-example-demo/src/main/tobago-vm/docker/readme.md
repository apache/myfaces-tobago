## Run the different Tobago versions on tobago-vm.apache.org 

Copy these file to the server and login there:

```
scp * tobago-vm.apache.org:docker
ssh tobago-vm.apache.org
cd docker
```

```
docker build tomcat/ -t tobago/tomcat
docker build apache-proxy/ -t tobago/apache-proxy
```

TODO:
* Installation of docker-compose is needed.

```
docker-compose up
```

TODOs:
* Create and use the "docker" oder "demo" user.