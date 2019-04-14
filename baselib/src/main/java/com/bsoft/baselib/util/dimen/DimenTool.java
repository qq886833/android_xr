package com.bsoft.baselib.util.dimen;


import com.bsoft.baselib.util.NumUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class DimenTool {
    private static final String LOCALPATH = "baselib/src/main/res";
    private static final int[] sw = {240, 320, 360, 400, 440, 480, 520, 560, 640, 680, 720, 760, 800, 840, 920, 960, 1040, 1080, 1200};

    private static String referPath = LOCALPATH + "/values/dimens.xml";

    public static void gen() {
        //以此文件夹下的dimens.xml文件内容为初始值参照
        File referFile = new File(referPath);
        BufferedReader reader = null;

        ArrayList<StringBuilder> swArray = new ArrayList<>();
        for (int i : sw) {
            swArray.add(new StringBuilder());
        }

        try {
            reader = new BufferedReader(new FileReader(referFile));
            String tempString;
            while ((tempString = reader.readLine()) != null) {
                if (tempString.contains("</dimen>")) {
                    String start = tempString.substring(0, tempString.indexOf(">") + 1);
                    String end = tempString.substring(tempString.lastIndexOf("<") - 2);
                    Double num = null;
                    try {
                        num = Double.parseDouble
                                (tempString.substring(tempString.indexOf(">") + 1,
                                        tempString.indexOf("</dimen>") - 2));
                    } catch (NumberFormatException e) {
                        continue;
                    }

                    //根据不同的尺寸，计算新的值，拼接新的字符串，并且结尾处换行。
                    for (int i = 0; i < swArray.size(); i++) {
                        StringBuilder builder = swArray.get(i);
                        builder.append(start).append(NumUtil.formatTwo(num * sw[i] / 360)).append(end).append("\n");
                    }

                } else {
                    for (int i = 0; i < swArray.size(); i++) {
                        StringBuilder builder = swArray.get(i);
                        builder.append(tempString).append("\r\n");
                    }
                }
            }
            reader.close();

            //写入
            for (int i = 0; i < swArray.size(); i++) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(LOCALPATH).append("/values-sw");
                stringBuilder.append(sw[i]);
                stringBuilder.append("dp/dimens.xml");
                File file = new File(stringBuilder.toString());
                writeFile(file, swArray.get(i).toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    private static void writeFile(File file, String text) {
        createFile(file);
        PrintWriter out = null;
        try {
            out = new PrintWriter(file);
            out.println(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.close();
    }

    private static void createFile(File file) {
        if (file.exists() && file.isFile()) {
            file.delete();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        File parentFile = file.getParentFile();
        if (parentFile.exists()) {
            if (parentFile.isFile()) {
                parentFile.delete();
                parentFile.mkdirs();
            }
        } else {
            parentFile.mkdirs();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        gen();
    }
}
