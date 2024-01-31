# **DCInside Football Crawler**

이 프로젝트는 DCInside의 'football_new8' 갤러리에서 데이터를 크롤링하여
분석하는 목적으로 개발되었습니다.

주요 기능은 게시글의 조회수 추적, 인기 게시글 추적 및 시각화 등을 포함합니다.

## **폴더 구조**

프로젝트는 다음과 같은 구조로 구성되어 있습니다:

- **`src/main/java/org/example/`**: 주요 Java 소스 코드가 포함된 디렉토리입니다.
- **`src/main/java/org/example/json/`**: 크롤링 결과를 저장하는 JSON 파일이 위치하는 디렉토리입니다.
- **`src/main/java/org/example/test/`**: 테스트를 위한 Java 소스 코드가 위치하는 디렉토리입니다.
- **`src/main/java/org/example/visualization/`**: 크롤링 데이터를 시각화하기 위한 HTML 및 JavaScript 파일이 위치하는 디렉토리입니다.

### **주요 Java 파일 설명**

- **`BinarySearchPosts.java`**: 이진 검색을 통해 특정 날자에 생성된 게시물을 크롤링하는 클래스입니다.
- **`DCPopularPostsTracker.java`**: 인기 게시글의 조회수 추적하는 클래스입니다.
- **`SumViewCountsByDate.java`**: 특정 날짜에 대한 게시글 조회수를 합산하는 클래스입니다.
- **`TotalViewCountCrawler.java`**: 특정 날짜의 게시글 조회수를 크롤링하는 클래스입니다.

### **JSON 파일**

- **`dc_popular_posts2.json`**, **`popular_posts.json`**: 인기 게시글의 데이터를 저장하는 JSON 파일입니다.

### **테스트 파일**

- **`PopularPostsHttpConnection.java`**, **`PopularPostsJsoupDirect.java`**, **`TimerTest.java`**: 다양한 방식으로 게시글 데이터를 크롤링 및 테스트하는 Java 파일입니다.

### **시각화 파일**

- **`ResultVisualization.html`**: 크롤링 결과를 시각화하는 HTML 파일입니다.
- **`script1.js`**: 시각화에 필요한 JavaScript 파일입니다.

시각화 결과는 **`ResultVisualization.html`** 파일을 웹 브라우저에서 열어 확인할 수 있습니다.

## **라이선스**

이 프로젝트는 MIT 라이선스 하에 배포됩니다.