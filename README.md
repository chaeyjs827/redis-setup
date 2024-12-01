## docker-compose 실행
1. 도커 설치 및 실행 후 진행(프로젝트 루트/docker)
```
pwd (루트 디렉토리 일 때)
cd docker
docker-compose up -d
```

2. redis-master 컨테이너 실행 확인
```
docker exec -it redis-master redis-cli
> info replication
```

3. redis-slave 컨테이너 실행 확인
* role:slave와 master_link_status:up이 출력되면, Slave가 정상적으로 Master와 연결되어 있다는 의미
```
docker exec -it redis-slave redis-cli
> info replication
```

4. redis-sentinel 실행 확인
```
docker exec -it redis-sentinel redis-cli -p 26379
> SENTINEL masters (master 상태 확인)
> SENTINEL SLAVES mymaster (Master에 연결된 Slaves 정보를 확인)
```

5. docker network 목록 확인
* master/slave/sentinel 연결 확인
* 실행 안 될 경우 docker-compose의 network name 설정 확인
```
docker network inspect redis-cluster
```

* * *

## 도커 삭제 명령어
* 삭제 순서 : 컨테이너->이미지->불륨
1. 컨테이너 삭제
```
docker container rm -f $(docker ps -aq)
```

2. 이미지 삭제
```
docker rmi -f $(docker images -q)
```

3. 불륨 삭제
```
docker volume rm $(docker volume ls -q)
```
