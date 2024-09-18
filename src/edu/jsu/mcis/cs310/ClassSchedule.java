package edu.jsu.mcis.cs310;

import com.github.cliftonlabs.json_simple.*;
import com.opencsv.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class ClassSchedule {
    
    private final String CSV_FILENAME = "jsu_sp24_v1.csv";
    private final String JSON_FILENAME = "jsu_sp24_v1.json";
    
    private final String CRN_COL_HEADER = "crn";
    private final String SUBJECT_COL_HEADER = "subject";
    private final String NUM_COL_HEADER = "num";
    private final String DESCRIPTION_COL_HEADER = "description";
    private final String SECTION_COL_HEADER = "section";
    private final String TYPE_COL_HEADER = "type";
    private final String CREDITS_COL_HEADER = "credits";
    private final String START_COL_HEADER = "start";
    private final String END_COL_HEADER = "end";
    private final String DAYS_COL_HEADER = "days";
    private final String WHERE_COL_HEADER = "where";
    private final String SCHEDULE_COL_HEADER = "schedule";
    private final String INSTRUCTOR_COL_HEADER = "instructor";
    private final String SUBJECTID_COL_HEADER = "subjectid";
    
    public String convertCsvToJsonString(List<String[]> csv) {


        
        Iterator<String[]> iterator= csv.iterator(); //iterating through CSV
        
        
        LinkedHashMap<String, String> jsonSchedules = new LinkedHashMap<>(); // hashmap of accroynm (type) to full name (where)
        
        
        LinkedHashMap<String, String> jsonSubjects = new LinkedHashMap<>(); 

        LinkedHashMap<String, HashMap> jsonCourses = new LinkedHashMap<>();   
        
          
        

        ArrayList<String> scheduleArrayCount = new ArrayList<>(); //creating an arraylist to store types of schedule types
        ArrayList<String> subjectArrayCount = new ArrayList<>(); 
        ArrayList<String> courseArrayCount = new ArrayList<>();
        
        ArrayList<String> crnArrayCount = new ArrayList<>(); 
        ArrayList<HashMap> sectionArrayHashMaps = new ArrayList<>();


        

        
        if (iterator.hasNext()) {
            String[] headings = iterator.next();
            while (iterator.hasNext()) {
                String[] csvRecord = iterator.next();
                
                String recType = csvRecord[5]; //csv record for the of class abbreviated (online = ONL etc)
                String recHow = csvRecord[11]; //csv record for the how the class is ran (online vs in person etc)
                
                String recAbbv = csvRecord[2].substring(0,3).replaceAll("\\s", ""); //csv record for the Subject Description
                String recName = csvRecord[1]; //csv record for the SubjectName
                
 
                String recNum = csvRecord[2].substring(csvRecord[2].length() -3);
                String recDesc = csvRecord[3];
                String recNameAndNnum = csvRecord[2];
                int recCredits = Integer.parseInt(csvRecord[6]);
                
                int recCrnInt = Integer.parseInt(csvRecord[0]);
                String recCrn = csvRecord[0];                
                String recSect = csvRecord[4];
                String recStart = csvRecord[7];
                String recEnd = csvRecord[8];
                String recDays = csvRecord[9];
                String recWhere = csvRecord[10];
                String[] recIntructors = csvRecord[12].split(",");
                ArrayList<String> Instructors = new ArrayList<>();

                for (int y = 0; y < recIntructors.length; y++){
                    Instructors.add(recIntructors[y].replaceAll("^\\s+", "").replaceAll("\\s+$", ""));
                }
                


                        
                
                for (int i = 0; i < headings.length; ++i) {
                    
                    if (scheduleArrayCount.contains(recType)){ //checking to see if its already in our list
                        
                        assert true; //skipping if already in our array


                    }
                    else{

                        scheduleArrayCount.add(csvRecord[5]); //adding to arraylist to be skipped in future
                        jsonSchedules.put(recType, recHow); //adding TYPE and WHERE to hashmap

                    }
                    
                    if (subjectArrayCount.contains(recDesc)){ //checking to see if its already in our list
                        
                        assert true; //skipping if already in our array
                        //System.out.println("already contains");

                    }
                    else{

                        subjectArrayCount.add(recDesc); //adding to arraylist to be skipped in future
                        jsonSubjects.put(recAbbv, recName); //adding TYPE and WHERE to hashmap
                        //System.out.println(jsonSchedules); 
                    }
                    
                    
                    if (courseArrayCount.contains(recNameAndNnum)){ //checking to see if its already in our list
                        
                        assert true; //skipping if already in our array


                    }
                    else{

                        courseArrayCount.add(csvRecord[2]); //adding to arraylist to be skipped in future
                        LinkedHashMap<String, Object> jsonCourseDetails = new LinkedHashMap<>();                        
                        jsonCourseDetails.put(SUBJECTID_COL_HEADER, recAbbv);
                        
                        jsonCourseDetails.put(NUM_COL_HEADER, recNum);
                       
                        jsonCourseDetails.put(DESCRIPTION_COL_HEADER, recDesc);
                        
                        jsonCourseDetails.put(CREDITS_COL_HEADER, recCredits);
                        //System.out.println(recAbbv);
                        
                        jsonCourses.put(recNameAndNnum, jsonCourseDetails);

                        
                    }
                    if (crnArrayCount.contains(recCrn)){ //checking to see if its already in our list
                        
                        assert true; //skipping if already in our array

                    }
                    else{

                        crnArrayCount.add(recCrn); //adding to arraylist to be skipped in future
                        LinkedHashMap<String, Object> jsonSectionDetails = new LinkedHashMap<>();
                        
                        jsonSectionDetails.put(CRN_COL_HEADER, recCrnInt);
                        jsonSectionDetails.put(SUBJECTID_COL_HEADER, recAbbv);
                        jsonSectionDetails.put(NUM_COL_HEADER, recNum);
                        jsonSectionDetails.put(SECTION_COL_HEADER, recSect);
                        jsonSectionDetails.put(TYPE_COL_HEADER, recType);
                        jsonSectionDetails.put(START_COL_HEADER, recStart);
                        jsonSectionDetails.put(END_COL_HEADER, recEnd);
                        jsonSectionDetails.put(DAYS_COL_HEADER, recDays);
                        jsonSectionDetails.put(WHERE_COL_HEADER, recWhere);

                        jsonSectionDetails.put(INSTRUCTOR_COL_HEADER, Instructors);
                        
                        
                        
                        sectionArrayHashMaps.add(jsonSectionDetails);
                        
                        
                        
                    }                    
                    

                }
  
               

            }

            
            //System.out.println(jsonSchedule);

        }        
        
        JsonObject mainRecord = new JsonObject();
        mainRecord.put("scheduletype", jsonSchedules);
        mainRecord.put(SUBJECT_COL_HEADER, jsonSubjects);
        mainRecord.put("course", jsonCourses);
        mainRecord.put("section", sectionArrayHashMaps);

        String jsonString = Jsoner.serialize(mainRecord);

            // Print final JSON string after all data is added
        System.out.println(jsonString);

            return jsonString;
        
    }
    
    public String convertJsonToCsvString(JsonObject json) {
        
        return ""; // remove this!
        
    }
    
    public JsonObject getJson() {
        
        JsonObject json = getJson(getInputFileData(JSON_FILENAME));
        return json;
        
    }
    
    public JsonObject getJson(String input) {
        
        JsonObject json = null;
        
        try {
            json = (JsonObject)Jsoner.deserialize(input);
        }
        catch (Exception e) { e.printStackTrace(); }
        
        return json;
        
    }
    
    public List<String[]> getCsv() {
        
        List<String[]> csv = getCsv(getInputFileData(CSV_FILENAME));
        return csv;
        
    }
    
    public List<String[]> getCsv(String input) {
        
        List<String[]> csv = null;
        
        try {
            
            CSVReader reader = new CSVReaderBuilder(new StringReader(input)).withCSVParser(new CSVParserBuilder().withSeparator('\t').build()).build();
            csv = reader.readAll();
            
        }
        catch (Exception e) { e.printStackTrace(); }
        
        return csv;
        
    }
    
    public String getCsvString(List<String[]> csv) {
        
        StringWriter writer = new StringWriter();
        CSVWriter csvWriter = new CSVWriter(writer, '\t', '"', '\\', "\n");
        
        csvWriter.writeAll(csv);
        
        return writer.toString();
        
    }
    
    private String getInputFileData(String filename) {
        
        StringBuilder buffer = new StringBuilder();
        String line;
        
        ClassLoader loader = ClassLoader.getSystemClassLoader();
        
        try {
        
            BufferedReader reader = new BufferedReader(new InputStreamReader(loader.getResourceAsStream("resources" + File.separator + filename)));

            while((line = reader.readLine()) != null) {
                buffer.append(line).append('\n');
            }
            
        }
        catch (Exception e) { e.printStackTrace(); }
        
        return buffer.toString();
        
    }
    
}