package org.example;

import java.util.Timer;
import java.util.TimerTask;

public class TimerTest {

    public static void main(String[] args) {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                // 이 부분에 반복 실행하고 싶은 코드를 작성합니다.
                System.out.println("타이머 작동 중: 5초마다 메시지 출력");
            }
        };

        // 타이머를 5초(5000밀리초)마다 실행합니다.
        timer.scheduleAtFixedRate(task, 0, 5000);
    }
}
