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

        JsonArray records = new JsonArray(); //Creating main JsonArray container
        
        Iterator<String[]> iterator= csv.iterator(); //iterating through CSV
        
        LinkedHashMap<String, HashMap> jsonSchedule = new LinkedHashMap<>(); //schedule type hashmap of hashmaps
        LinkedHashMap<String, String> jsonScheduleType = new LinkedHashMap<>(); // hashmap of accroynm (type) to full name (where)
        
        LinkedHashMap<String, HashMap> jsonSubject = new LinkedHashMap<>(); 
        LinkedHashMap<String, String> jsonSubjectType = new LinkedHashMap<>(); 
        
        
        
        ArrayList<String> scheduleArray = new ArrayList<String>(); //creating an arraylist to store types of schedule types
        ArrayList<String> subjectArray = new ArrayList<String>();        
        
        
        
        if (iterator.hasNext()) {
            String[] headings = iterator.next();
            while (iterator.hasNext()) {
                String[] csvRecord = iterator.next();
                String recType = csvRecord[5]; //csv record for the TYPE
                String recWhere = csvRecord[11]; //csv record for the WHERE
                String recDesc = csvRecord[2].substring(0,3).replaceAll("\\s", ""); //csv record for the Subject Description
                String recName = csvRecord[1]; //csv record for the SubjectName               
                
                for (int i = 0; i < headings.length; ++i) {
                    
                    if (scheduleArray.contains(recType)){ //checking to see if its already in our list
                        
                        assert true; //skipping if already in our array
                        //System.out.println("already contains");

                    }
                    else{

                        scheduleArray.add(csvRecord[5]); //adding to arraylist to be skipped in future
                        jsonScheduleType.put(recType, recWhere); //adding TYPE and WHERE to hashmap
                        //System.out.println(jsonScheduleType); 
                    }
                    
                    if (subjectArray.contains(recDesc)){ //checking to see if its already in our list
                        
                        assert true; //skipping if already in our array
                        //System.out.println("already contains");

                    }
                    else{

                        subjectArray.add(csvRecord[5]); //adding to arraylist to be skipped in future
                        jsonSubjectType.put(recDesc, recName); //adding TYPE and WHERE to hashmap
                        //System.out.println(jsonScheduleType); 
                    }                    

                }
  
                

            }
            jsonSchedule.put("scheduletype", jsonScheduleType);
            records.add(jsonSubject);
            jsonSubject.put("subject", jsonSubjectType);
            records.add(jsonSubject);            
            //System.out.println(jsonSchedule);

        }        
        


        String jsonString = Jsoner.serialize(records);
                
        
        
        
        
        
        if (iterator.hasNext()) {
            String[] headings = iterator.next();
            while (iterator.hasNext()) {
                String[] csvRecord = iterator.next();
                LinkedHashMap<String, String> jsonCourse = new LinkedHashMap<>();
                for (int i = 0; i < headings.length; ++i) {
                    jsonCourse.put(headings[i].toLowerCase(), csvRecord[i]);
                    
                }
                //records.add(jsonCourse);
            }
        }
        

        
        return ""; // remove this!
        
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