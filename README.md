# **해축갤 인기 게시물 분석 프로젝트**

이 프로젝트는 해외 축구 갤러리(해축갤)의 인기 게시물과 <br>관련된 트래픽 데이터를 분석하는 데 초점을 맞추고 있습니다. 

프로젝트는 Java로 구현되어 있으며, Jsoup과 Jackson 라이브러리를 사용하여 웹 크롤링과 데이터 처리 기능을 제공합니다.

## **의존성**

프로젝트는 다음 라이브러리에 의존하며, 각 라이브러리의 버전은 아래와 같습니다:

- Jsoup 1.15.3: HTML 파싱 및 웹 크롤링을 위해 사용됩니다.
- Jackson Databind 2.16.1: JSON 데이터 처리를 위해 사용됩니다.
- JSON 20231013: JSON 데이터 생성을 위해 사용됩니다.
- Lombok 1.18.30: 모델 클래스의 보일러플레이트 코드 감소를 위해 사용됩니다.

## **프로젝트 구조**

프로젝트는 크게 **`crawler`**, **`model`**, **`test`**, 그리고 **`resources`** 디렉토리로 구성됩니다.

- **`crawler`**: 웹 크롤링 및 데이터 수집과 관련된 클래스가 위치합니다.
- **`model`**: 데이터 모델을 정의하는 클래스가 위치합니다.
- **`test`**: 테스트 코드를 포함하는 디렉토리입니다.
- **`resources`**: JSON 데이터 파일과 데이터 시각화를 위한 HTML 및 JS 파일이 포함됩니다.

## **빌드 및 실행 방법**

프로젝트는 Gradle을 사용하여 빌드하고 관리합니다. 
<br>다음 단계에 따라 프로젝트를 빌드하고 실행할 수 있습니다.

1. **프로젝트 클론**:

    ```bash
    git clone https://github.com/your-repository/your-project.git
    cd your-project
    ```

2. **Gradle 빌드**:

    ```bash
    ./gradlew build
    ```

3. **실행**:
   프로젝트에는 여러 실행 가능한 클래스가 포함되어 있으며, **`build.gradle`** 파일에서 **`Main-Class`**를 변경하여 원하는 클래스를 실행할 수 있습니다.
<br><br>
   예를 들어, **`PopularPostsHttpConnection`** 클래스를 실행하려면 **`build.gradle`** 파일에서 **`Main-Class`**를 해당 클래스로 설정하고 JAR 파일을 실행합니다.

    ```bash
    java -jar build/libs/your-project-1.0-SNAPSHOT-all.jar
    ```
