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

Update, rebuild, run: 

```
docker-compose down

docker pull tomcat:8.5-jre8
docker pull debian:stable-slim

docker-compose build

nohup docker-compose up &
```

TODOs:
* Create and use the "docker" or "demo" user.