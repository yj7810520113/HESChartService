//分别修改行数和文件名即可
//在运行前补全文件的xy和括号情况
package org.maomao.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;


public class dataset_x_y {
    static List<String> readBylines = new ArrayList<String>();
    static HashMap<String, Integer> xyMap=new HashMap<String, Integer>();

    public static void main(String[] args) {
        // TODO Auto-generated method stub

        String folderPathString = "C:\\Users\\Administrator\\Desktop\\maomao\\妹子数据集\\xy\\dataset";
        forEachFile(folderPathString);

    }
    public static void forEachFile(String path){
        File file=new File(path);
        File[] files=file.listFiles();
        for(File file2:files){
            if(file2.isDirectory()){
                //System.out.println(files[i].getName());
                //System.out.println(files[i].getAbsolutePath());
                forEachFile(file2.getAbsolutePath());
            }
            else{
                getData(file2.getPath());
                reverseP1P2ToXY();
                //System.out.println(file2.getParent());
                saveData(file2.getName(),file2.getParent());

            }
        }
    }

    public static void getData(String path) {
        readBylines=new ArrayList<String>();
        try {
            // read file content from file
            StringBuffer sb = new StringBuffer("");

            FileReader reader = new FileReader(path);
            BufferedReader br = new BufferedReader(reader);

            String str = null;

            while ((str = br.readLine()) != null) {
                readBylines.add(str);

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
    }

    public static void reverseP1P2ToXY(){
        xyMap=new HashMap<String, Integer>();
        for(int i=1;i<readBylines.size();i++){
            String[] lines=readBylines.get(i).split(",");
            for(int j=0;j<lines.length;j++){
//                修改这里的j==4和j==6
                if(j==6){
                    String[] xys=lines[j].replace(">:",">;").split(";");
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
//                    System.out.println("key:"+xy1.replaceAll("\\D",""));
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
