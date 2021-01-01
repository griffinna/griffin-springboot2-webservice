# Springboot + AWS Study Project :computer:
> 참고 : 스프링부트와 AWS로 혼자 구현하는 웹 서비스 (프리렉 / 이동욱 지음)
   
### INDEX
- [x] 스프링 부트 시작하기
- [x] 테스트 코드를 작성
- [x] JPA로 데이터베이스 다루기
- [x] Mustache 화면구성
- [x] 스프링 시큐리티와 OAuth2.0 로그인 기능
- [ ] AWS 서버환경 - AWS EC2
- [ ] AWS 데이터베이스환경 - AWS RDS
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