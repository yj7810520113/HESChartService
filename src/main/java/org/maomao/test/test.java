package org.maomao.test;

/**
 * Created by Administrator on 2017/7/18.
 */


//第三部
//Third
import org.apache.commons.collections.bag.SynchronizedSortedBag;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;


public class test {
    static List<String> readBylines = new ArrayList<String>();
    static HashMap<String, Integer> xyMap=new HashMap<String, Integer>();
    static int lineCount=0;
//    static Hashtable<Integer,d>

    public static void main(String[] args) {
        // TODO Auto-generated method stub

        String folderPathString = "E:\\huawei\\5\\rImport_ysb_201705010746_06_0.txt";
        String str=getData(folderPathString);

    }
    public static void parseData(String str){
        String[] lines=str.split("\n");
        for(String line:lines){

        }
    }


    public static void forEachFile(String path){
        lineCount=0;
        File file=new File(path);
        File[] files=file.listFiles();
        for(File file2:files){
            if(file2.isDirectory()){
                //System.out.println(files[i].getName());
                //System.out.println(files[i].getAbsolutePath());
                forEachFile(file2.getAbsolutePath());
            }
            else{
                System.out.println(lineCount);
                getData(file2.getPath());

            }
        }

    }

    public static String getData(String path) {
//        readBylines=new ArrayList<String>();
        StringBuffer sb = new StringBuffer("");

        try {
            // read file content from file

            FileReader reader = new FileReader(path);
            BufferedReader br = new BufferedReader(reader);

            String str = null;

            while ((str = br.readLine()) != null) {
//                lineCount++;
//                readBylines.add(str);
                sb.append(str);

            }

            br.close();
            reader.close();

            // // write string to file
            // FileWriter writer = new FileWriter("c://test2.txt");
            // BufferedWriter bw = new BufferedWriter(writer);
            // bw.write(sb.toString());
            //
            // bw.close();
            // writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static void reverseP1P2ToXY(){
        xyMap=new HashMap<String, Integer>();
        for(int i=1;i<readBylines.size();i++){
            String[] lines=readBylines.get(i).split(",");
            for(int j=0;j<lines.length;j++){
                if(j==4){
                    String[] xys=lines[j].split(">");
//					System.out.println(xys);
                    for(String xy:xys){
                        //取出多余的计算值，只需要x，y
                        System.out.println(xy);
                        xy=xy.replaceAll("\\d*?\\.\\d*?<", "<");
                        System.out.println("after:"+xy);
                        if(xyMap.containsKey(xy)){
                            xyMap.replace(xy, xyMap.get(xy)+1);
                        }
                        else {
                            xyMap.put(xy, 1);
                        }
                    }
                }
            }
        }
    }
    public static void saveData(String datasetName,String parentPath){
        String data="";
        if(!(new File(parentPath+"\\P2_countResult.csv").exists())){
            try {
                new File(parentPath+"\\P2_countResult.csv").createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        try {
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件

            FileWriter writer1 = new FileWriter(parentPath+"\\P2_countResult.csv", true);
            //迭代xyMap
            System.out.println(xyMap.size());
            for(Entry<String, Integer> xyEntry:xyMap.entrySet()){
                data+=datasetName.replaceAll("\\..*","")+",";
                //取出x，y值
                String[] xy=xyEntry.getKey().split(":");
                for(String xy1:xy){
                    data+=xy1.replaceAll("\\D", "")+",";
                }
                System.out.println(xyEntry.getValue());
                data+=xyEntry.getValue()+"\n";
            }
            writer1.write(data);
            writer1.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
