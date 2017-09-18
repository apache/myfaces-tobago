## Run the different Tobago versions on tobago-vm.apache.org 

Copy these file to the server and login there:

```
scp * tobago-vm.apache.org:docker
ssh tobago-vm.apache.org
cd tobago/tobago-example/tobago-example-demo/src/main/tobago-vm/docker/
```

```
docker build tomcat/ -t tobago/tomcat
docker build apache-proxy/ -t tobago/apache-proxy
```

```
docker-compose up
```

TODOs:
* Create and use the "docker" oder "demo" user.