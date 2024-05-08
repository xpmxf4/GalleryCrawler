# 해축갤 인기 게시물 분석 프로젝트

이 프로젝트는 해외 축구 갤러리(해축갤)의 인기 게시물과 관련된 트래픽 데이터를 분석하는 데 초점을 맞추고 있습니다. 프로젝트는 Java로 구현되어 있으며, Jsoup 라이브러리를 사용하여 웹 크롤링 기능을 제공합니다.

## 목차
1. [프로젝트 구조](#프로젝트-구조)
2. [주요 기능](#주요-기능)
3. [의존성](#의존성)
4. [설치 및 실행 방법](#설치-및-실행-방법)
5. [사용 예시](#사용-예시)
6. [라이선스](#라이선스)

## 프로젝트 구조
프로젝트는 다음과 같은 구조로 이루어져 있습니다:
```
src/
├── main/
   ├── java/
      └── com/
          └── example/
              └── dcinsidecrawler/
                  ├── tracker/
                  │   ├── DateBasedPostTracker.java
                  │   └── IDateBasedPostTracker.java
                  ├── provider/
                  │   └── GalleryUrlProvider.java
                  ├── popular/
                  │   └── PopularPostIntervalTracker.java
                  └── DCInsideCrawlerApplication.java
```

## 주요 기능
- 날짜 기반 게시물 추적 및 분석
- 특정 날짜 범위 내의 게시물 수 추정
- 특정 날짜 범위 내의 게시물 조회수 합계 계산
- 갤러리 URL 생성 및 관리
- 인기 게시물 주기적 추적 및 기록

## 의존성
프로젝트는 다음 라이브러리에 의존합니다:
- Jsoup: HTML 파싱 및 웹 크롤링을 위해 사용됩니다.

## 설치 및 실행 방법
1. 프로젝트 클론:
   ```bash
   git clone https://github.com/your-repository/your-project.git
   cd your-project
   ```

2. 프로젝트 빌드:
   ```bash
   ./gradlew build
   ```

3. 프로젝트 실행:
   ```bash
   java -jar build/libs/your-project.jar
   ```

## 사용 예시
```java
DateBasedPostTracker postTracker = new DateBasedPostTracker();
String startDate = "2023-01-01";
String endDate = "2023-12-31";
int viewCounts = postTracker.getViewCountsBetweenDates(startDate, endDate);
System.out.println("조회수 합계: " + viewCounts);
```
