## Run the different Tobago versions on tobago-vm.apache.org 

Copy these file to the server and login there:

```
ssh tobago-vm.apache.org

git checkout ...
cd tobago

or 

cd tobago
git pull --rebase

cd tobago-example/tobago-example-demo/src/main/tobago-vm/docker/
```

```
docker build tomcat/ -t tobago/tomcat
docker build apache-proxy/ -t tobago/apache-proxy
```

```
docker-compose pull
docker-compose build
nohup docker-compose up &
```

TODOs:
* Create and use the "docker" or "demo" user.