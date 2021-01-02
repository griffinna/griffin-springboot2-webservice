# Springboot + AWS Study Project :computer:
> 참고 : 스프링부트와 AWS로 혼자 구현하는 웹 서비스 (프리렉 / 이동욱 지음)
   
### INDEX
- [x] 스프링 부트 시작하기
- [x] 테스트 코드를 작성
- [x] JPA로 데이터베이스 다루기
- [x] Mustache 화면구성
- [x] 스프링 시큐리티와 OAuth2.0 로그인 기능
- [x] AWS 서버환경 - AWS EC2
- [x] AWS 데이터베이스환경 - AWS RDS
- [ ] EC2 서버에 프로젝트 배포
- [ ] Travis CI 배포 자동화
- [ ] 무중단서비스

### AWS EC2

- EC2 : Elastic Compute Cloud (AWS에서 제공하는 성능, 용량 등을 유동적으로 사용할 수 있는 서버)
- 1대의 t2.micro만 사용하면 24시간 사용 가능
> 1. 서울 리전 선택
> 2. EC2 인스턴스 시작 
> 3. AMI (Amazon Machine Image) 선택 > Amazon Linux AMI   
EC2 인스턴스를 시작하는 데 필요한 정보를 이미지로 만들어 둔 것  
> 4. 스토리지 : 30GB 까지 프리티어로 가능
> 5. 태그 : 웹 콘솔에서 표기될 태그인 Name 태그를 등록 (본인 서비스의 인스턴스를 나타낼 수 있는 값)
> 6. 방화벽 : SSH (AWS EC2 에 터미널로 접속 ) > 본인 IP에서만 접근 가능하도록 구성하는 것이 안전함
- EIP (Elasic IP, AWS의 고정IP) 할당후 인스턴스와 연결

### EC2 서버 접속
```
ssh -i {pem 키 위치} {EC2의 탄력적 IP주소}
```
- 손쉽게 접속할 수 있도록 설정하기
> ~/.ssh 로 pem 파일 이동 > ssh 실행시 자동으로 읽어 접속
```
cp pem {pem 키 위치} ~/.ssh/
ll ~/.ssh/
```
> pem 키 권한 변경
```
chmod 600 ~/.ssh/pem키이름
``` 
> pem 키가 있는 ~/.ssh 디렉토리에 config 파일 생성
```
vim ~/.ssh/config
```

```
# griffin-springboot2-webservice
Host 원하는서비스명
    HostName ec2의 탄력적IP주소
    User ec2-user
    IdentityFile ~/.ssh/pem키 이름
```
> config 파일 실행권한 설정
```
chmod 700 ~/.ssh/config
```
> ec2 접속하기
```
ssh {config에 등록한 서비스명}
```

#### 1. Java 8 설치
```
sudo yum install -y java-1.8.0-openjdk-devel.x86_64
```
> 인스턴스의 Java 버전을 8로 변경
```
sudo /usr/sbin/alternatives --config java
```
> 불필요한 java 가 있는 경우 삭제
```
sudo yum remove java-1.7.0-openjdk
```
> java 버전 확인
```
java -version
```
#### 2. 타임존변경
```
sudo rm /etc/localtime
sudo ln -s /usr/share/zoneinfo/Asia/Seoul /etc/localtime

date
```
#### 3. 호스트네임 변경
> 각 서버가 어느 서비스인지 표현하기 위해 HOSTNAME 을 변경
```
sudo vim /etc/sysconfig/network
```
> HOSTNAME 으로 되어있는 부분을 원하는 서비스명으로 변경
```
HOSTNAME=griffin-springboot2-webservice
```
> /etc/hosts 에 변경한 hostname 등록
```
sudo vim /etc/hosts

127.0.0.1  등록한HOSTNAME

curl 등록한 호스트 이름
```

### AWS RDS
#### 1. RDS 인스턴스 생성하기
> 1. DBMS - MariaDB 선택
> 2. 템플릿 - 프리티어
> 3. 네트워크 - 퍼블릭 엑세스 기능 on (지정된 IP만 접근 가능하도록 제어)

#### 2. RDS 운영환경에 맞는 파라미터 설정
> 1. 타임존 - Asia/Seoul
> 2. Character Set (utf8mb4 / utf8mb4_general_ci로 설정)
> - character_set_client
> - character_set_connection
> - character_set_database 
> - character_set_filesystem
> - character_set_results
> - character_set_server
> - collation_connection 
> - collation_server   
> 3. Max Connection (150으로 설정)
>
> 파라미터 변경 후, 해당 데이터베이스 옵션 수정 (DB 파라미터 그룹) / 재부팅

#### 3. 내 PC 에서 RDS 접속
> 1. 데이터베이스 보안그룹 선택   
> 2. EC2에 사용된 보안그룹의 그룹ID 를 RDS 보안그룹의 인바운드로 추가   
> MYSQL/Aurora | TCP | 3306 | 사용자지정 | {EC2 보안그룹ID}   
> MYSQL/Aurora | TCP | 3306 | 내 IP | {내 IP}
> 3. Database 플러그인 설치 (RDS정보 페이지에서 엔드포인트 확인)
```
use griffin_springboot_webserivce;

show variables like 'c%';

alter database griffin_springboot_webserivce
CHARACTER SET = 'utf8mb4'
COLLATE = 'utf8mb4_general_ci'
;

select @@time_zone, now();

CREATE TABLE TEST (
    id bigint(20) not null auto_increment,
    content varchar(255) default null,
    primary key (id)
) engine = InnoDB;

insert into TEST(content) value ('테스트');

select * from TEST;
```

#### 4. EC2 에서 RDS 접근 확인
> EC2 에 MySQL CLI 설치
```
ssh griffin-springboot2-webservice

sudo yum install mysql

mysql -u 계정 -p -h Host주소

mysql> show databases;
+-------------------------------+
| Database                      |
+-------------------------------+
| griffin_springboot_webserivce |
| information_schema            |
| innodb                        |
| mysql                         |
| performance_schema            |
| test                          |
+-------------------------------+
6 rows in set (0.00 sec)
```

### EC2 서버에 프로젝트 배포
#### 1. EC2 에 프로젝트 clone
```
# git 설치
sudo yum install git
git --version

# 디렉토리 생성
mkdir ~/app && mkdir ~/app/step1
cd ~/app/step1

# git clone
git clone 프로젝트주소
```

- 테스트수행
```
./gradlew test
```
> 성공
```
BUILD SUCCESSFUL in 2m 26s
5 actionable tasks: 5 executed
```

> 실패 : -bash: ./gradlew: Permission denied
```
# 실행 권한을 추가한 뒤 다시 테스트 진행
chmod +x ./gradlew
```

#### 2. 배포 스크립트 만들기
> ~/app/step1/deploy.sh
```
REPOSITORY=/home/ec2-user/app/step1
PROJECT_NAME=griffin-springbbot2-webservice

cd /$REPOSITORY/$PROJECT_NAME/

echo "> Git Pull"
git pull

echo "> 프로젝트 Build 시작"
./gradlew build

echo "> step1 디렉토리 이동"
cd $REPOSITORY

echo "> Build 파일 복사"
cp $REPOSITORY/$PROJECT_NAME/build/libs/*.jar $REPOSITORY/

echo "> 현재 구동중인 애플리케이션 pid 확인"
CURRENT_PID=$(pgrep -f ${PROJECT_NAME}.*.jar)

echo "> 현재 구동중인 애플리케이션 pid : $CURRENT_PID"
if [ -z "$CURRENT_PID" ]; then
	echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다."
else
	echo "> kill -15 $CURRENT_PID"
	kill -15 $CURRNET_PID
	sleep 5
fi

echo "> 새 애플리케이션 배포"
JAR_NAME=$(ls -tr $REPOSITORY/ | grep jar | tail -n 1)

echo "> JAR Name: $JAR_NAME"
nohup java -jar $REPOSITORY/$JAR_NAME 2>&1 &

# nohup : 애플리케이션 실행자가 터미널을 종료해도 애플리케이션은 계속 구동
```
> deploy.sh 실행권한 추가 및 실행
```
chmod +x ./deploy.sh

./deploy.sh

> Git Pull
이미 업데이트 상태입니다.
> 프로젝트 Build 시작

Deprecated Gradle features were used in this build, making it incompatible with Gradle 5.0.
Use '--warning-mode all' to show the individual deprecation warnings.
See https://docs.gradle.org/4.10.3/userguide/command_line_interface.html#sec:command_line_warnings

BUILD SUCCESSFUL in 4s
6 actionable tasks: 1 executed, 5 up-to-date
> step1 디렉토리 이동
> Build 파일 복사
> 현재 구동중인 애플리케이션 pid 확인
> 현재 구동중인 애플리케이션 pid : 
> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다.
> 새 애플리케이션 배포
> JAR Name: griffin-springboot2-webservice-1.0-SNAPSHOT.jar
```

> 외부 Security 파일 등록하기
> - ClientId 와 ClientSecret 설정을 서버에 직접 가지고 있도록 설정
```
vim /home/ec2-user/app/application-oauth.properties

# deploy 수정
nohup java -jar \-Dspring.config.location=classpath:/application.properties,/home/ec2-user/app/application-oauth.properties \/$REPOSITORY/$JAR_NAME 2>&1 &

```