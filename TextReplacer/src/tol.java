
import java.io.File;public class tol 
{
     public static void main(String... args){
         String s1 = Main.readString(new File("/storage/emulated/0/AppProjects/CS/1.txt"))
         .replace("\n","").replace(" ","");
         String s2 = Main.readString(new File("/storage/emulated/0/AppProjects/CS/2.txt"))
         .replace("\n","").replace(" ","");
         System.out.println(s1.equals(s2));
     }   
}
