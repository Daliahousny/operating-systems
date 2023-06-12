package os.assignment;

import java.io.File;
import java.io.IOException;
import java.util.*;
import static os.assignment.Commands.*;

public class OSAssignment {

   public static void main(String[] args) throws IOException {
       
       String current = "C:";
       while (true){
           Scanner sc=new Scanner(System.in);
           String input=sc.nextLine();
           String [] strArray =input.split(" ");
           strArray=redirect(strArray,current);
           
           if(strArray[0].equals("pwd")){
               pwd(current);
           }
           else if (strArray[0].equals("cd")){
               current=cd(strArray,current);
           }
           else if(strArray[0].equals("ls")){
               ls(strArray,current);
           }
           else if(strArray[0].equals("cp")){
               boolean copied=false;
               copied=Copy(strArray, current);
               if(copied==true){
                   System.out.println("Copied Succeessfully");
               }
               else{
                   System.out.println("Failed to copy");
               }
           }
           else if(strArray[0].equals("mv")){
               Move(strArray,current);
           }
           else if (strArray[0].equals("rm")) {
               Remove(strArray , current);
           }
           else if (strArray[0].equals("mkdir")) {
               makeDir(strArray , current);
           }
           else if (strArray[0].equals("rmdir")) {
               removeDir(strArray , current);
           }
           else if(strArray[0].equals("clear")) {
               clear();
           }
           else if(strArray[0].equals("cat")){
               Cat(strArray, current);
           }
           else if(strArray[0].equals("date")){
               Date(strArray, current);
           }
           else if(strArray[0].equals("more")){
               if(strArray.length==2){
                   String path=strArray[1];
                   if(!path.contains("\\")){
                       path=current.concat("\\").concat(strArray[1]);
                   }
                   File file =new File(path);
                   if (!file.exists()){
                       System.out.println(file.getName() + " is not a found");
                   }
                   else{
                       if(!file.isDirectory()){
                           String [] fileContent=readFile(file);
                           More(fileContent);
                       }
                       else{
                           System.out.println(file.getName() + " is not a file");
                       }
                   }
               }
               else{
                   System.out.println("Input Error");
               }
           }
           else if(strArray[0].equals("man")){
               if(strArray.length==2){
                   String [] arr= Manual(strArray[1]);
                   for (int i=0 ;i<arr.length;i++){
                       System.out.println(arr[i]);
                   }
               }
               else if(strArray.length==4 && strArray[2].equals("|")&&strArray[3].equals("more")){
                   String [] arr= Manual(strArray[1]);
                   More(arr);
               }
           }
           else if (strArray[0].equals("exit")){
               break;
           }
           else{
               System.out.println("INPUT ERROR");
           }
           out=System.out;
       }
   }
}
