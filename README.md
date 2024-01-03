# **프로젝트 제목: 축구 갤러리의 게시물 이진 탐색**

## **설명**

이 프로젝트는 [해외축구갤러리](https://github.com/xpmxf4/HaeChuk-Gallery) 프로젝트에 부하 테스트를 하기 전 실제 부하를 측정하기 위해 특정 날짜 범위에 대한 축구 갤러리(디시인사이드 축구 갤러리)의 게시물 수를 추정하려고 설계되었습니다.

프로그램은 특정 대상 날짜에 포함된 게시물이 있는 첫 페이지와 마지막 페이지를 효율적으로 찾기 위해 이진 탐색을 수행합니다. 
웹 스크래핑 기술을 사용하여 페이지 내용을 파싱하고 각 게시물의 날짜를 식별합니다. 식별된 페이지 범위를 기반으로 게시물 수를 추정합니다.

## **기능**

- **대상 날짜에 대한 사용자 입력**: 사용자는 찾고자 하는 대상 날짜를 입력할 수 있습니다. 날짜는 **`yy.MM.dd`** 형식이어야 합니다.
- **이진 탐색 알고리즘**: 대상 날짜를 포함하는 첫 페이지와 마지막 페이지를 찾기 위해 이진 탐색을 구현하여, 대규모 페이지 범위에서 탐색 과정을 최적화합니다.
- **웹 스크래핑**: Jsoup 라이브러리를 사용하여 축구 갤러리 페이지의 HTML 내용을 스크래핑하고 파싱하여 각 게시물의 날짜를 찾습니다.
- **게시물 수 추정**: 대상 날짜의 페이지 범위를 계산하여 게시물 수를 추정합니다.

## **사용 방법**

1. **환경 설정**:
    - 시스템에 Java가 설치되어 있는지 확인하세요.
    - 프로젝트 의존성에 HTML 파싱을 위한 Jsoup 라이브러리를 포함하세요.
2. **프로그램 실행**:
    - **`BinarySearchPosts`** 클래스를 컴파일하고 실행하세요.
    - 프로그램이 실행되면, 사용자에게 **`yy.MM.dd`** 형식의 대상 날짜를 입력하라는 메시지가 표시됩니다.
    - 입력된 날짜에 대한 첫 페이지와 마지막 페이지를 찾은 후, 추정된 게시물 수를 출력합니다.