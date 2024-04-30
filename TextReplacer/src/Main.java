import java.io.*;
import java.nio.charset.*;
import java.util.*;

public class Main {
	public static void main(String[] args) {

        LinkedHashMap<String,String> map = new LinkedHashMap<String,String>(){{
                /*  put("float.Parse", "Float.parseFloat");
                 put("int.Parse", "Integer.parseInt");
                 put(" bool ", " boolean ");
                 put(".ToCharArray()", "");
                 put("Split(", "split(");
                 put(" string ", " String ");
                 put("Replace(", "replace(");
                 put("Count;", "Count();");
                 put("Eventsexe[index]", "Eventsexe.get(index)");
                 put("List", "ArrayList");
                 put("Layer.LayerArray[this.parentid]", "Layer.LayerArray.get(this.parentid)");
                 put("ArrayArrayList", "ArrayList");
                 put("foreach", "for");
                 put(" in ", " : ");
                 put("'", "\"");
                 put(".Contains(", ".contains(");
                 put("Math.Sqrt", "Math.sqrt");
                 put(".ToString()", "+\"\"");
                 put("Barrages[index2]", "Barrages.get(index2)");*/
                // put(" object ", " Object");
                //  put("LaseArray[this.id]", "LaseArray.get(this.id)");
                // put("@event", "event_");
                //  put("Sonevents[idx]", "Sonevents.get(idx)");
                //  put("Barrages[index1]", "Barrages.get(index1)");
                //    put("results[index2]", "results.get(index2)");
                //   put("results[index3]", "results.get(index3)");
                /*   put("Events[index]", "Events.get(index)");
                 put("Math.Cos", "Math.cos");
                 put("Math.Sin", "Math.sin");
                 put("Math.Sqrt", "Math.sqrt");
                 put("Math.Abs", "Math.abs");*/
                //   put("import com.meng.crazystorm.dummy.Math;", "");
                //    put("Math.Max", "Math.max");
                //       put(".Length", ".length");
                //    put("Main.bgset[type]", "Main.bgset.get(type)");
                //   put("Rectangle?(", "Rectangle(");
                //   put("byte.MaxValue", "255");
                //  put("s.Draw(", "s.draw(");
                //     put("Main.bgset[32+type]", "Main.bgset.get(32+type)");
            //    put("BatchArray[this.id]", "BatchArray.get(this.id)");
              //  put("BatchArray[id]", "BatchArray.get(id)");
            //    put("Math.Min","Math.min");
                put("Time.GE[index]","Time.GE.get(index)");
                put("GE[index]","GE.get(index)");
                
                put("Main.conditions[str4]","Main.conditions.get(str4)");
                put("","");
                put("","");
                put("","");
                put("","");
                put("","");
                put("","");
                put("","");
                put("","");
                put("","");
                put("","");
                put("","");
                put("","");
                put("","");
                put("","");
                put("","");
            }};


		ArrayList<File> fs=new ArrayList<>();
		try {
			getFiles("/storage/emulated/0/AppProjects/CS/app/src/main/java/com/meng/crazystorm/", fs);
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (File f:fs) {
			String s = readString(f);
            //	s = s.replace("com.meng.pt2.tools", "com.meng.mediatool.tools");
            for (Map.Entry<String,String> entry : map.entrySet()) {
                String k = entry.getKey();
                String v = entry.getValue();
                System.out.println(String.format("k=%s,v=%s", k, v));
                s = s.replace(k, v);
                //  throw new NullPointerException("Crazy Thursday need 50 CNY");
            }
			//System.out.println(s);
			saveString(f, s);
		}
		System.out.println("ok");
	}

	public static void getFiles(String path, ArrayList<File> list) throws Exception {
        //目标集合fileList
        HashSet<String> targetFilesFormat = new HashSet<String>(){{
                add(".java");
            }};
        HashSet<String> excpetFiles = new HashSet<String>(){{
                add("md5");
                add("base64");
            }};
        File file = new File(path);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File fileIndex : files) {
                if (fileIndex.isDirectory()) {
                    getFiles(fileIndex.getPath(), list);
                } else {
                    String fileExtend = fileIndex.getName().substring(fileIndex.getName().lastIndexOf("."));
             //       System.out.println(fileExtend);
                    if (targetFilesFormat.contains(fileExtend)  && !excpetFiles.contains(fileIndex.getName().toLowerCase())) {
						list.add(fileIndex);
					}
                }
            }
        }
    }

	public static void saveString(File f, String str) {
		try {
			FileOutputStream fos = new FileOutputStream(f);
			OutputStreamWriter writer = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
			writer.write(str);
			writer.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

	public static String readString(File f) {
		String s = null;
		try {      
			long filelength = f.length();
			byte[] filecontent = new byte[(int) filelength];
			FileInputStream in = new FileInputStream(f);
			in.read(filecontent);
			in.close();
			s = new String(filecontent, StandardCharsets.UTF_8);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;	
	}
}
