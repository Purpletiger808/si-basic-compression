import java.io.*;
import java.util.*;
import static java.lang.Thread.sleep;


public class Main {
    public static void main(String[] args) throws InterruptedException {
        titleScreen();
        Scanner menuScan = new Scanner(System.in);
        boolean notDone = true;
        while(notDone){
            printMenu();
            String menuChoice = menuScan.next();
            menuScan.nextLine();
            System.out.println();
            sleep(500);
            switch (menuChoice){
                case "A", "a":
                    doCompression();
                    break;
                case "B", "b":
                    doDecompression();
                    break;
                case "C", "c":
                    notDone = false;
                    break;
                default:
                    System.out.println("\t\t\t\tError: Please enter a valid choice. ");
            }
            System.out.println();
        }

        System.out.println("\t\t\t\tSee you later!");

    }



    //General Operation Methods vvv



    public static void titleScreen(){
        System.out.println("""
                \t\t ____   __   ____  __  ___     ___  __   _  _  ____  ____  ____  ____  ____  __  __   __ _     __   __     ___   __ \s
                \t\t(  _ \\ / _\\ / ___)(  )/ __)   / __)/  \\ ( \\/ )(  _ \\(  _ \\(  __)/ ___)/ ___)(  )/  \\ (  ( \\   / _\\ (  )   / __) /  \\\s
                \t\t ) _ (/    \\\\___ \\ )(( (__   ( (__(  O )/ \\/ \\ ) __/ )   / ) _) \\___ \\\\___ \\ )((  O )/    /  /    \\/ (_/\\( (_ \\(  O )
                \t\t(____/\\_/\\_/(____/(__)\\___)   \\___)\\__/ \\_)(_/(__)  (__\\_)(____)(____/(____/(__)\\__/ \\_)__)  \\_/\\_/\\____/ \\___/ \\__/\s
                """);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t A Teaching Project by Tate Broussard");
        System.out.println();
    }

    public static void printMenu(){
        System.out.println("\t\t\t\tPlease select an option: ");
        System.out.println("\t\t\t\tA. Compress a file");
        System.out.println("\t\t\t\tB. Decompress a file");
        System.out.println("\t\t\t\tC. Quit execution");
        System.out.print("\t\t\t\tYour choice: ");
    }

    //Build a string containing the contents of the input  file
    public static StringBuilder getInput(String operationMode) {
        File operationFile = new File(operationMode);
        StringBuilder str = new StringBuilder();
        try {
            Scanner inScan = new Scanner(operationFile);
            System.out.println("\t\t\t\tInput file found. Begin processing.");
            while(inScan.hasNextLine()){
                str.append(inScan.nextLine());
            }
            inScan.close();
        } catch (FileNotFoundException e) {
            System.out.println("\t\t\t\tInput file not found. Ensure input file \" " + operationMode + "\" exists in project folder.");
            throw new RuntimeException(e);
        }
        return str;
    }

    //Actually write the operated string into an output file, depending on the operation
    public static void writeOperation(StringBuilder operationString, String operationMode){
        try{
            File outputFile = new File(operationMode);

            if(outputFile.createNewFile()){ //Create a file with the file object, write the operation string, and close
                FileWriter operationWriter = new FileWriter(outputFile);
                operationWriter.write(String.valueOf(operationString));
                operationWriter.close();
            }else{ //If the file already exists, i.e. failed to be deleted
                System.out.println("\t\t\t\tError: Output file exists. Delete \"" + operationMode + "\" and try again. ");
            }
        } catch (IOException e) { //Just in case
            System.out.println("\t\t\t\tError: Cannot access output file.");
            throw new RuntimeException(e);
        }
    }

    //Clean any previous outputs, if the program was run before
    public static void sanitise(String operationMode){
        File operationFile = new File(operationMode);

        if(operationFile.exists()){
            if(operationFile.delete()){
                System.out.println("\t\t\t\tSanitization successful. Begin execution.");
            }else{
                System.out.println("\t\t\t\tSanitization unsuccessful. Ensure no file called \"" + operationMode + "\" exists. ");
            }
        }else{
            System.out.println("\t\t\t\tSanitization unnecessary. Begin execution.");
        }
    }



    //General Operation Methods ^^^
    //Compression Methods vvv



    //Execute the compression steps
    public static void doCompression() throws InterruptedException {
        sanitise("compressed.txt");
        sleep(1000);

        StringBuilder uncompressedString = getInput("uncompressed.txt");

        StringBuilder compressedString = compress(uncompressedString);

        writeOperation(compressedString, "compressed.txt");
        System.out.println("\t\t\t\tCompression completed!");
    }


    //Compress the input string, combining the results of counting the number of times each character appears consecutively
    public static StringBuilder compress(StringBuilder uncompressedString) throws InterruptedException {
        StringBuilder compressionBuilding = new StringBuilder();
        char prevChar;
        char curChar;
        int count = 0;
        char[] charArr = String.valueOf(uncompressedString).toCharArray();

        for(int i = 0; i < charArr.length-1; i++){

            if(i%3 == 0){
                System.out.print("\t\t\t\tCompressing .    \r");
            }else if(i%3 == 1){
                System.out.print("\t\t\t\tCompressing . .  \r");
            }else{
                System.out.print("\t\t\t\tCompressing . . .\r");
            }

            prevChar = charArr[i];
            curChar = charArr[i+1];
            if(prevChar == curChar){
                count++;
            } else{
                compressionBuilding.append(count + 1).append(prevChar);
                count = 0;

            }

            if(i+1 == charArr.length-1){
                compressionBuilding.append(count + 1).append(curChar);
                count = 0;
            }
            sleep(300);
        }

        System.out.print("\t\t\t\tCompressing .    \r");
        sleep(300);
        System.out.print("\t\t\t\tCompressing . .  \r");
        sleep(300);
        System.out.print("\t\t\t\tCompressing . . .");
        System.out.println();
        return compressionBuilding;
    }



    //Compression Methods ^^^
    //Decompression Methods vvv



    //Borrowed from GeeksForGeeks
    public static String extractInt(String str)
    {
        // Replacing every non-digit number with a space(" ")
        str = str.replaceAll("[^0-9]", " "); // regular expression

        // Replace all the consecutive white spaces with a single space
        str = str.replaceAll(" +", " ");

        if (str.isEmpty()) {
            return "-1";
        }

        return str;
    }

    //Altered previous method to work on chars
    public static String extractChar(String str)
    {
        // Replacing every non-alphabet char with a space(" ")
        str = str.replaceAll("[^A-Za-z]", " "); // regular expression

        // Replace all the consecutive white spaces with a single space
        str = str.replaceAll(" +", " ");

        if (str.isEmpty()) {
            return "-1";
        }

        return str;
    }

    //Decompress the stringbuilder, assuming the format follows number then letter, by taking all numbers and all letters in order through designated arrays, should have exactly one number per letter
    //Then, adds [letter] to the final decompressed string [number] times
    public static StringBuilder decompress(StringBuilder compressedString) throws InterruptedException{
        StringBuilder decompressedString = new StringBuilder();

        String extractNumString = compressedString.toString();
        String extractedNums = extractInt(extractNumString);
        String[] numStrings = extractedNums.split(" ");
        int[] num = new int[numStrings.length];
        for(int i = 0; i < numStrings.length; i++){
            num[i] = Integer.parseInt(numStrings[i]);
        }

        String extractCharString = compressedString.toString();
        String extractedChars = extractChar(extractCharString);
        String[] charStrings = extractedChars.split(" ");

        for(int i = 0; i < charStrings.length-1; i++){
            int j = 0;
            while (j < num[i]) {
                decompressedString.append(charStrings[i+1]);
                j++;
            }
        }

        System.out.print("\t\t\t\tDecompressing .    \r");
        sleep(300);
        System.out.print("\t\t\t\tDecompressing . .   \r");
        sleep(300);
        System.out.print("\t\t\t\tDecompressing . . .");
        System.out.println();
        return decompressedString;
    }

    //Execute the decompression steps
    public static void doDecompression() throws InterruptedException {
        sanitise("uncompressed.txt");
        sleep(1000);

        StringBuilder compressedString = getInput("compressed.txt");

        StringBuilder decompressedString = decompress(compressedString);

        writeOperation(decompressedString, "uncompressed.txt");
        System.out.println("\t\t\t\tDecompression completed!");
    }
}
