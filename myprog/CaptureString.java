import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class CaptureString {
    static List<String> StringLiterals = new ArrayList<String>();

    public static void main(String[] args) throws Exception {
        long startTime = System.nanoTime();
        System.out.println("Program Started");
        String folder = "/Users/richa/Desktop/prog/javafiles/";
        Queue<File> queue = new LinkedList<>();
        queue.add(new File(folder));
        /* loop until queue is empty - */
        while (!queue.isEmpty()) {
            /* get next file/directory from the queue */
            File current = queue.poll();
            File[] fileDirList = current.listFiles();
            if (fileDirList != null) {
                // Enqueue all directories and fetch all strings from all files.
                for (File fd : fileDirList) {
                    if (fd.isDirectory())
                        queue.add(fd);
                    else
                        fetchStrings(fd);
                }
            }
        }
        for (int j = 0; j < StringLiterals.size(); j++) {
            System.out.println(StringLiterals.get(j));
        }
        createExcel(StringLiterals, "/Users/richa/Desktop/prog/StringLiteral.xlsx");
        long elapsedTime = System.nanoTime() - startTime;
        System.out.println("Total execution time in millis: " + elapsedTime / 1000000);
        System.out.println("Program Ended");
    }

    public static void fetchStrings(File file) throws Exception {
        if (file.isFile() && file.getName().endsWith(".java")) {
            FileReader fr = new FileReader(file.getPath());
            char literal = (char) (34);
            boolean flag = false;
            int i;
            String currentString = "";
            while ((i = fr.read()) != -1) {
                // if (!flag) {
                // if ((char) i == literal) {
                // flag = true;
                // }
                // } else {
                // currentString += (String.valueOf((char) i));
                // if ((char) i == literal) {
                // flag = false;
                // StringLiterals.add(currentString.replace("\"", ""));
                // currentString = "";
                // }
                // }
                if ((char) i == literal) {
                    flag = flag ? false : true;
                    if (currentString != "") {
                        StringLiterals.add(currentString.replace("\"", ""));
                    }
                }
                if (flag) {
                    currentString += (String.valueOf((char) i));
                } else {
                    currentString = "";
                }
            }
            fr.close();
        }
    }

    public static void createExcel(List<String> literals, String path) throws Exception {

        HSSFWorkbook workbook = new HSSFWorkbook();
        // invoking creatSheet() method and passing the name of the sheet to be created
        HSSFSheet sheet = workbook.createSheet("Sheet1");
        // creating the 0th row using the createRow() method
        HSSFRow rowhead = sheet.createRow((short) 0);
        rowhead.createCell(0).setCellValue("String Literals");
        for (int j = 0; j < literals.size(); j++) {
            HSSFRow row = sheet.createRow((short) (j + 1));
            row.createCell(0).setCellValue(literals.get(j));
        }
        FileOutputStream fileOut = new FileOutputStream(path);
        workbook.write(fileOut);
        // closing the Stream
        fileOut.close();
        // closing the workbook
        workbook.close();
        // prints the message on the console
        System.out.println("Excel file has been generated successfully.");
    }
}