package kr.devsnote.file;

import java.io.*;

public class FileTest {

    public static void main(String[] args) {

        String filePath = "/Users/yoonsik/Desktop/fileTest.txt";
        File file = new File(filePath);

        try {
            if (!file.exists()) {
                file.createNewFile();
                System.out.println("File created");
            }
            System.out.println("파일내용 작성 시작 -----");
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, false));
            writer.write("Hello! World!!");
            writer.newLine();
            writer.write("Java 기초 공부시간입니다. ");
            writer.newLine();
            writer.write("끝");
            System.out.println("파일내용 작성 끝 -------");

            // 버퍼 및 스트림 뒷정리
            writer.flush(); // 버퍼의 남은 데이터를 모두 쓰기
            writer.close(); // 스트림 종료

            if (file.exists()){
                BufferedReader reader = new BufferedReader(new FileReader(file));
                System.out.println("파일내용 출력 시작 -------");
                String line = null;
                while ((line = reader.readLine()) != null){
                    System.out.println(line);
                }
                System.out.println("파일내용 출력 끝 -------");
                reader.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
