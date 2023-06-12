package os.assignment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Commands {
    public static PrintStream out = System.out;
    static FileOutputStream fout;
    
    static void Remove(String [] str , String c){
        if (str[1].equals("-r")){
            if(str.length == 3){
                File file = new File(pathHandling(str[2] , c));
                if(file.isDirectory()){
                    File flist[] = file.listFiles();
                    for (int i=0 ;i<flist.length ; i++){
                        flist[i].delete();
                    }
                    file.delete();
                    System.out.println("Deleted Successfully");
                }
                else {
                    System.out.println(file.getName() + " is not a directory ");
                }
            }
            else{
                System.out.println("Error, Please check the arguments again");
            }
        }
        else if (str[1].contains("\\*.")){
            if(str.length==2){
                String [] newStr =str[1].split("\\*");
                String path=pathHandling(newStr[0].substring(0, newStr[0].length()-1) , c);
                File folder = new File(path);
                File flist[] = folder.listFiles();
                for (int i=0 ;i<flist.length ; i++){
                    if(flist[i].getName().contains(".txt")){
                        flist[i].delete();
                    }
                }
                folder.delete();
                System.out.println("Deleted Successfully");
            }
            else{
                System.out.println("Error, Please check the arguments again");
            }
        }
        else if(str[1].contains("-f")|| str[1].contains("--force")){
            String path = pathHandling ( str[2] , c);
            File file =new File(path);
            if(file.isDirectory()){
                System.out.println(file.getName()+" is directory");
            }
            else{
                if(file.exists()){
                    file.delete();
                    System.out.println("Deleted Successfully");
                }
                else{
                    System.out.println("File doesn't exist");
                }
            }
        }
        else{
            for(int i=1; i<str.length ; i++){
                String path = pathHandling (str[i] , c);
                File file =new File(path);
                if(!file.isDirectory()){
                    if(file.exists() && file.canWrite()){
                        file.delete();
                        System.out.println("Deleted Successfully");
                    }
                    else if (file.exists() && !file.canWrite()){
                        System.out.println("Are you sure you want to delete this file ?");
                        Scanner scanner=new Scanner(System.in);
                        String answer=scanner.nextLine();
                        if(answer.startsWith("y")|| answer.startsWith("Y")){
                            boolean deleted =file.delete();
                            if (deleted == true ){
                                System.out.println("Deleted Successfully");
                            }
                            else {
                                System.out.println("Failed to delete ");
                            }
                        }
                    }
                    else{
                        System.out.println("File doesn't exist");
                    }
                }
                else {
                    System.out.println(file.getName()+" is directory");
                }
            }
        }
    }
    static void makeDir (String [] str ,String c){
        if (str.length == 2 ){
            String path = pathHandling ( str[1] , c);
            File folder =new File(path);
            if (!folder.exists()){
                folder.mkdir();
                System.out.println("Folder created successfully");
            }
            else {
                System.out.println(folder.getName() + " Does already exist");
            }
        }
        else {
            System.out.println("Error in the arguments");
        }
    }
    static void removeDir (String [] str ,String c){
        if (str.length == 2 ){
            String path = pathHandling ( str[1] , c);
            File folder =new File(path);
            if (folder.exists()){
                if(!folder.isDirectory()){
                    System.out.println(folder.getName() + " is not a directory");
                }
                else{
                    File flist[] = folder.listFiles();
                    if (flist.length!=0){
                        System.out.println(folder.getName() + " is not empty");
                    }
                    else {
                        folder.delete();
                        System.out.println(folder.getName() + " Deleted Successfully");
                    }
                }
            }
            else {
                System.out.println(folder.getName() + " Does not exist");
            }
        }
        else {
            System.out.println("Error in the arguments");
        }
    }
    static void clear() { //clear terminal screen
        try
        {
            if(System.getProperty("os.name").contains("Windows"))
            {
                new ProcessBuilder("cmd","/c","cls").inheritIO().start().waitFor();
            }
            else
            {
                Runtime.getRuntime().exec("clear");
            }
        }
        catch(IOException | InterruptedException ex) {}
    }
    static void pwd(String c){
        out.println(c);
    }
    static void ls(String [] str,String c){
        if(str.length==1){
            File file=new File(c);
            String[] list = file.list();
            for(String dirList:list){
                out.println(dirList);
            }
        }
        else if(str.length==2) {
            if(str[1].equals("-a")){
                File file=new File(c);
                String[] list = file.list();
                for(String dirList:list){
                    file=new File(c+"\\"+dirList);
                    boolean writable=file.canWrite();
                    boolean readable=file.canRead();
                    boolean executable=file.canExecute();
                    boolean isDir=file.isDirectory();
                    if(isDir){
                        out.print("d"); // its a directory
                    }
                    else{
                        out.print("-"); //is a file
                    }
                    if(readable){
                        out.print("r"); //open the file
                    }
                    else{
                        out.print("-"); //not readable
                    }
                    if(writable){
                        out.print("w");
                    }
                    else{
                        out.print("-");
                    }
                    if(executable){
                        out.print("x");
                    }
                    else{
                        out.print("-");
                    }
                    out.print("\t"+dirList);
                    out.println();
                }
            }
            else{ //its a folder
                out.println(str[1]);
                File file=new File(str[1]);
                if(file.exists())
                {
                    String[] list = file.list();
                    for(String dirList:list){
                        out.println(dirList);
                    }
                }
                else{
                    System.out.println(file.getName()+" is not exist");
                }
            }
        }
        else if(str.length==3){
            if(str[1].equals("|")&& str[2].equals("more")){
                File file=new File(c);
                String[] list = file.list();
                More(list);
            }
            else if(str[2].equals("-a")){
                File file=new File(str[2]); //ls-a w el folder
                if (file.exists()){
                    String[] list = file.list();
                    for(String dirList:list){
                        file=new File(str[2]+"\\"+dirList);
                        boolean writable=file.canWrite();
                        boolean readable=file.canRead();
                        boolean executable=file.canExecute();
                        boolean isDir=file.isDirectory();
                        if(isDir){
                            out.print("d"); // its a directory
                        }
                        else{
                            out.print("-"); //is a file
                        }
                        if(readable){
                            out.print("r"); //open the file
                        }
                        else{
                            out.print("-"); //not readable
                        }
                        if(writable){
                            out.print("w");
                        }
                        else{
                            out.print("-");
                        }
                        if(executable){
                            out.print("x");
                        }
                        else{
                            out.print("-");
                        }
                        out.print("\t"+dirList);
                        out.println();
                    }
                }
                else{
                    System.out.println(file.getName()+" is not exist");
                }
            }
            else {
                System.out.println("Input Error");
            }
        }
        else {
            System.out.println("Input Error");
        }
    }
    static String [] redirect(String [] strArray,String current) throws FileNotFoundException{
        boolean result = Arrays.stream(strArray).anyMatch(">"::equals);
        if(result )
        {
            if(Pattern.matches("^[A-Za-z]:",strArray[strArray.length-1])) //if file name starts with a captial or small letter and : its an absolute p
            {
                fout=new FileOutputStream(strArray[strArray.length - 1]); //representation of file
                
            }
            else //if not an absolute path
            {
                fout=new FileOutputStream(current+"\\"+strArray[strArray.length - 1]); //make file where current points at
                
            }
            //FileOutputStream fout=new FileOutputStream(strArray[strArray.length - 1]); //representation of file
            out=new PrintStream(fout);
            String [] r=new String[strArray.length-2]; //new array with size -2
            System.arraycopy(strArray,0,r,0,strArray.length-2);
            strArray=r;
            
        }
        
        boolean result2 = Arrays.stream(strArray).anyMatch(">>"::equals);
        if(result2 )
        {
            if(Pattern.matches("^[A-Za-z]:",strArray[strArray.length-1])) //if file name starts with a captial or small letter and : its an absolute p
            {
                fout=new FileOutputStream(strArray[strArray.length - 1],true); //representation of file
                
            }
            else //if not an absolute path
            {
                fout=new FileOutputStream(current+"\\"+strArray[strArray.length - 1],true); //make file where current points at
                
            }
            //FileOutputStream fout=new FileOutputStream(strArray[strArray.length - 1]); //representation of file
            out=new PrintStream(fout);
            String [] r=new String[strArray.length-2]; //new array with size -2
            System.arraycopy(strArray,0,r,0,strArray.length-2);
            strArray=r;
        }
        return strArray;
    }
    static String cd(String [] str, String c){
        if(str.length!=2){
            out.println("There's error in Arguments");
        }
        else {
            String path=pathHandling ( str[1] , c);
            File file=new File(path);
            if(!file.exists()){
                out.println("folder doesn't exist");
            }
            else{
                if(!file.isDirectory()){
                    out.println("it is not a directory");
                }
                else{
                    c=path;
                }
            }
        }
        return c;
    }
    static boolean Copy(String [] str ,String c) {
        boolean copied=false;
        if (str.length==3){
            String filename;
            String sourceDir=pathHandling(str[1],c) ;
            String destDir=pathHandling(str[2],c);
            File sourceFile =new File(sourceDir);
            if(sourceFile.exists()){
                if (sourceFile.isDirectory()){
                    System.out.println(sourceFile.getName()+" is directory");
                }
                else {
                    File destFile =new File(destDir);
                    if(destFile.isDirectory()){
                        filename=sourceFile.getName();
                        try {
                            destFile=new File(destDir,filename);
                            destFile.createNewFile();
                            try (Scanner scanner = new Scanner(sourceFile);
                                    FileWriter filewriter = new FileWriter(destFile)) {
                                while (scanner.hasNext()){
                                    filewriter.write(scanner.nextLine());
                                    filewriter.write('\n');
                                    filewriter.flush();
                                }
                                filewriter.close();
                                scanner.close();
                            }
                            copied=true;
                        }
                        catch (IOException ex) {
                            System.out.println("Input Error");
                        }
                    }
                    else{
                        try {
                            destFile=new File(destDir);
                            destFile.createNewFile();
                            try (Scanner scanner = new Scanner(sourceFile);
                                    FileWriter filewriter = new FileWriter(destFile)) {
                                while (scanner.hasNext()){
                                    filewriter.write(scanner.nextLine());
                                    filewriter.write('\n');
                                    filewriter.flush();
                                }
                                filewriter.close();
                                scanner.close();
                            }
                            copied=true;
                        }
                        catch (IOException ex) {
                            System.out.println("Input Error");
                        }
                    }
                }
            }
            else{
                System.out.println(sourceFile.getName()+ " is not exists");
            }
        }
        else if(str.length>3){
            String destDir = pathHandling(str[str.length-1] , c);
            File destFile =new File(destDir);
            int flag=1;
            if (destFile.isDirectory()){
                for(int i=1 ; i<str.length-1 ; i++){
                    String sourceDir=pathHandling(str[i],c) ;
                    File sourceFile =new File(sourceDir);
                    String filename=sourceFile.getName();
                    if(sourceFile.exists()){
                        if (sourceFile.isDirectory()){
                            System.out.println(sourceFile.getName()+" is directory");
                        }
                        else {
                            try {
                                File copiedfile = new File(destDir , filename);
                                copiedfile.createNewFile();
                                try (Scanner scanner = new Scanner(sourceFile); FileWriter filewriter = new FileWriter(copiedfile)) {
                                    while (scanner.hasNext()){
                                        filewriter.write(scanner.nextLine());
                                        filewriter.write('\n');
                                        filewriter.flush();
                                    }
                                    filewriter.close();
                                    scanner.close();
                                }
                                flag++;
                            }
                            catch (IOException ex) {
                                System.out.println("Error");
                            }
                        }
                    }
                    else{
                        System.out.println(sourceFile.getName()+ " is not exists");
                    }
                }
                if (flag==str.length-1){
                    copied=true;
                }
            }
            else{
                System.out.println(destFile.getName()+ " is not a directory");
            }
        }
        else{
            System.out.println("Arguments Error");
        }
        return copied;
    }
    static void Cat (String [] str ,String c) throws FileNotFoundException {
        for(int i=1; i<str.length ; i++){
            String path = pathHandling ( str[i] , c);
            File file =new File(path);
            if (!file.exists()){
                System.out.println(file.getName() + " is not a found");
            }
            else{
                if(file.isDirectory()){
                    System.out.println(file.getName() + " is not a file");
                }
                else{
                    Scanner scanner =new Scanner(file);
                    while (scanner.hasNext()){
                        System.out.println(scanner.nextLine());
                    }
                }
            }
            System.out.println("");
        }
    }
    static boolean More (String [] lines){
        boolean exit=false;
        double p=0.5;
        int i=0;
        while(p<1){
            int maxLines=lines.length;
            maxLines=(int)(lines.length*p);
            for(; i<maxLines ; i++){
                System.out.println(lines[i]);
            }
            System.out.print((p*100)+"% of the file is represented "+"Press y or Y to continue : ");
            Scanner input = new Scanner(System.in);
            String in=input.next();
            if(in.equalsIgnoreCase("y")){
                p+=0.25;
            }
            else if(in.equalsIgnoreCase("q")){
                exit=true;
                return exit;
            }
        }
        if(i<lines.length){
            for(; i<lines.length ; i++){
                System.out.println(lines[i]);
            }
        }
        System.out.println("-----THE END OF FILE-----");
        return exit;
    }
    static String [] readFile (File file ) throws FileNotFoundException{
        Scanner scanner=new Scanner(file);
        String data="";
        while (scanner.hasNext()){
            data+=scanner.nextLine()+ "\n";
        }
        scanner.close();
        String [] lines = data.split("\n");
        return lines;
    } 
    static String pathHandling (String path , String c){
        String absolute=c;
        if (path.contains(":\\")){
            absolute=path;
        }
        else{
            if(path.startsWith("\\")){
                absolute=c.concat(path);
            }
            else{
                absolute=c.concat("\\").concat(path);
            }
        }
        return absolute;
    }
    static void Move (String [] str , String c){
        if (str.length==3){
            File destFile = new File(pathHandling(str[2], c));
            if(destFile.canWrite()){
                File sourceFile = new File(pathHandling(str[1], c));
                if (sourceFile.exists()){
                    boolean copied,moved=false;
                    copied = Copy(str,c);
                    if(copied==true){
                        moved=sourceFile.delete();
                        if(moved==true){
                            System.out.println("Moved Successfully");
                        }
                    }
                    if (moved==false ){
                        System.out.println("Failed to move");
                    }
                }
                else{
                    System.out.println(sourceFile.getName()+" is not exist.");
                }
            }
            else{
                System.out.println("Are you sure you want to continue? ");
                Scanner scanner=new Scanner(System.in);
                String answer=scanner.nextLine();
                if(answer.startsWith("y")|| answer.startsWith("Y")){
                    File sourceFile = new File(pathHandling(str[1], c));
                    if (sourceFile.exists()){
                        boolean copied,moved=false;
                        copied = Copy(str,c);
                        if(copied==true){
                            moved=sourceFile.delete();
                            if(moved==true){
                                System.out.println("Moved Successfully");
                            }
                        }
                        if (moved==false ){
                            System.out.println("Failed to move");
                        }
                    }
                    else{
                        System.out.println(sourceFile.getName()+" is not exist.");
                    }
                }
                else {
                    System.out.println("Failed to move");
                }
            }
        }
        else if (str.length>3){
            if(str.length==4 && (str[1].equals("-f") || str[1].equals("--force"))){
                File sourceFile = new File(pathHandling(str[2], c));
                if (sourceFile.exists()){
                    boolean copied,moved=false;
                    String [] newStr={"cp",str[2],str[3]};
                    copied = Copy(newStr,c);
                    if(copied==true){
                        moved=sourceFile.delete();
                        if(moved==true){
                            System.out.println("Moved Successfully");
                        }
                    }
                    if (moved==false ){
                        System.out.println("Failed to move");
                    }
                }
                else{
                    System.out.println(sourceFile.getName()+" is not exist.");
                }
            }
            else{
                String destDir=pathHandling(str[str.length-1], c);
                File destFile = new File(destDir);
                if(destFile.isDirectory()){
                    if(destFile.canWrite()){
                        for(int i=1 ;i<str.length-1 ; i++){
                            File sourceFile = new File(pathHandling(str[i], c));
                            if (sourceFile.exists()){
                                boolean copied,moved=false;
                                String [] newStr={"cp",str[i],destDir};
                                copied = Copy(newStr,c);
                                if(copied==true){
                                    moved=sourceFile.delete();
                                    if(moved==true){
                                        System.out.println("Moved Successfully");
                                    }
                                }
                                if (moved==false ){
                                    System.out.println("Failed to move");
                                }
                            }
                            else{
                                System.out.println(sourceFile.getName()+" is not exist.");
                            }
                        }
                    }
                    else {
                        System.out.println("Are you sure you want to continue? ");
                        Scanner scanner=new Scanner(System.in);
                        String answer=scanner.nextLine();
                        if(answer.startsWith("y")|| answer.startsWith("Y")){
                            for(int i=1 ;i<str.length-1 ; i++){
                                File sourceFile = new File(pathHandling(str[i], c));
                                if (sourceFile.exists()){
                                    boolean copied,moved=false;
                                    String [] newStr={"cp",str[i],destDir};
                                    copied = Copy(newStr,c);
                                    if(copied==true){
                                        moved=sourceFile.delete();
                                        if(moved==true){
                                            System.out.println("Moved Successfully");
                                        }
                                    }
                                    if (moved==false ){
                                        System.out.println("Failed to move");
                                    }
                                }
                                else{
                                    System.out.println(sourceFile.getName()+" is not exist.");
                                }
                            }
                        }
                    }
                }
                else{
                    System.out.println(destFile.getName()+" is not a directory");
                }
            }
        }
        else {
            System.out.println("Input Error");
        }
    }
    static void Date(String [] str,String c){
        Date date=new Date();
        System.out.println(date);
    }
    static String [] Manual(String str) {
        if(str.equals("pwd")){
            String [] pwd=new String[12];
            pwd[0]="NAME";
            pwd[1]="\t pwd -- return working directory name";
            pwd[2]="SYNOPSIS";
            pwd[3]="\t [-L | -P]";
            pwd[4]="DESCRIPTION";
            pwd[5]="\t The pwd utility writes the absolute pathname of the current working directory to the standard output.";
            pwd[6]="\t Some shells may provide a builtin pwd command which similiar or identical to this utility. Consult the builtin(1) manual page.";
            pwd[7]="\t The options are as follows:";
            pwd[8]="\t -L \t Display the logical current working directory.";
            pwd[9]="\t -P \t Display the physical current working directory (all symbolic links resolved.";
            pwd[10]="\t If no options specified the -L option is assumed.";
            pwd[11]="Manual page pwd(1) Line 1 (press h for help or q for quit ";
            return pwd;
        }
        else if(str.equals("ls")){
            String [] ls=new String[13];
            ls[0]="NAME";
            ls[1]="\t ls -- list directory contents";
            ls[2]="SYNOPSIS";
            ls[3]="\t ls [OPTION]....[FILE]....";
            ls[4]="DESCRIPTION";
            ls[5]="\t List information about the FILEs (the current directory by default).";
            ls[6]="Sort entries alpahbetically if none of -cftuvSUX nor --sort is specified.";
            ls[7]="\t Mandatory arguments to long options are mandatory for short options too.";
            ls[8]="\t -a, --all ";
            ls[9]="\t \t don't ignore entries statring with .";
            ls[10]="\t -A, --almost-all";
            ls[11]="\t \t don't list implied . and ..";
            ls[12]="Manual page ls(1) Line 1 (press h for help or q for quit) ";
            return ls;
        }
        else if(str.equals("mkdir")){
            String [] mkdir=new String[20];
            mkdir[0]="NAME";
            mkdir[1]="\t mkdir --make directories";
            mkdir[2]="SYNOPSIS";
            mkdir[3]="\t mkdir [OPTION]....DIRECTORY....";
            mkdir[4]="DESCRIPTION";
            mkdir[5]="\t Create the DIRECTORY(ies) if they don't already exist." ;
            mkdir[6]="\t Mandatory items to long options are mandatory for short options too.";
            mkdir[7]="\t -m, --mode=MODE";
            mkdir[8]="\t \t set file mode as in (chmod), not a=rwx - umamsk";
            mkdir[9]="\t -p, --parents";
            mkdir[10]="\t \t no error if existing, make parent directories as needed";
            mkdir[11]="\t -v, --verbose";
            mkdir[12]="\t \t print a message for each created directory";
            mkdir[13]="\t -Z \t set SELinux security context of each created directory to the default type. ";
            mkdir[14]="\t --context[=CTX]";
            mkdir[15]="\t \t like -Z or if CTX is specified then set the SELinux or SMACK security context to CTX";
            mkdir[16]="\t --help \t display this help and exit";
            mkdir[17]="\t --version";
            mkdir[18]="\t \t output version information and exit";
            mkdir[19]="Manual page mkdir(1) Line 1 (press h for help or q for quit";
            return mkdir;
        }
        else if(str.equals("sudo")){
            String [] sudo=new String[12];
            sudo[0]="NAME";
            sudo[1]="\t sudo, sudoedit-execute a command as another user";
            sudo[2]="SYNOPSIS";
            sudo[3]="\t sudo -h | -K | -k | -V";
            sudo[4]="\t sudo -v [-AknS] [-g group] [-h host] [-p prompt] [-u user]";
            sudo[5]="\t sudo -l [-AknS] [-g group] [-h host] [-p prompt] [-U user] [-u user] [command]";
            sudo[6]="\t sudo [-AbEHnPS] [-C num] [-g group] [-h host] [-p prompt] [-u user] [VAR=value] [-i | -s] [command]";
            sudo[7]="\t sudoedit [-AknS] [-C num] [-g group] [-h host] [-p prompt] [-u user] file...";
            sudo[8]="DESCRIPTION";
            sudo[9]="\t sudo allows a permitted user to execute a command as the superuser or another user, as specified by the privacy policy.";
            sudo[10]="\t user's real (not effective) user ID is used to determine the user name with which to query the security policy.";
            sudo[11]="\t sudo supports a plugin architecture for security policies and input/output logging. Third parties can develop and distribute.";
            return sudo;
        }
        String [] err = new String[1];
        err[0]="These command not supported";
        return err;
    }
}