# Run the different Tobago versions on tobago-vm.apache.org 

The demo on tobago-vm works with docker. 
Each user with an account on the vm and 
member of the docker group can manage the containers.

How to become root access is described on the
[Apache site](https://reference.apache.org/committer/opie).

There is a copy of the source code repo of Tobago in the directory 
```/opt/docker/tobago/``` which contains the docker-compose configuration 
in ```tobago-example/tobago-example-demo/src/main/tobago-vm/docker/```.

## Initial installation

Copy these file to the server and login there:

```
ssh tobago-vm.apache.org

cd
git clone https://github.com/apache/myfaces-tobago
cd ~/myfaces-tobago/tobago-example/tobago-example-demo/src/main/tobago-vm/docker/

docker-compose up -d
```

## Simple Update (only restart needed containers)

only minor changes like version, or staging for votes: 

```
cd ~/myfaces-tobago/tobago-example/tobago-example-demo/src/main/tobago-vm/docker/
git pull --rebase
docker-compose build
docker-compose up -d
```

## Full Update

Update, rebuild, run: 

```
cd ~/myfaces-tobago/tobago-example/tobago-example-demo/src/main/tobago-vm/docker/
git pull --rebase
docker-compose down
docker pull tomcat:8.5-jdk8-adoptopenjdk-hotspot
docker pull debian:buster-slim
docker-compose build
docker-compose up -d
```
