package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
//        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
//        String fileName = "data.csv";
//
//        List<Employee> list = parseCSV(columnMapping, fileName);
//        String json = listToJson(list);
//        writeString(json);

        List<Employee> list2 = parseXML("data.xml");
        String json2 = listToJson(list2);
        writeString(json2);

    }

    public static List<Employee> parseCSV(String[] columnMap, String file) {
        try (CSVReader csvReader = new CSVReader(new FileReader(file))) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMap);
            CsvToBean<Employee> csb = new CsvToBeanBuilder<Employee>(csvReader)
                    .withMappingStrategy(strategy)
                    .build();
            return csb.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String listToJson(List<Employee> list) {
        GsonBuilder gb = new GsonBuilder();
        Gson gson = gb.create();
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        return gson.toJson(list, listType);
    }

    public static void writeString(String json) {
        try (FileWriter file = new
                FileWriter("data2.json")) {
            file.write(json);
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Employee> parseXML(String file) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File(file));
        Node root = doc.getDocumentElement();
        List<Employee> list = new ArrayList<>();
        int totalEmployee = 0;
        return listEmployee(root, list, totalEmployee);
    }

    public static List<Employee> listEmployee(Node node, List<Employee> employeeList, int totalEmployee) {
        Employee empl = new Employee(0, null, null, null, 0);
        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node_ = nodeList.item(i);
            if (Node.ELEMENT_NODE == node_.getNodeType()) {
                if (("employee".equals(node_.getParentNode().getNodeName()))) {
                    Element element = (Element) node_;
                    switch (totalEmployee) {
                        case 0:
                            empl.id = Long.parseLong(element.getTextContent());
                            totalEmployee++;
                            break;
                        case 1:
                            empl.firstName = element.getTextContent();
                            totalEmployee++;
                            break;
                        case 2:
                            empl.lastName = element.getTextContent();
                            totalEmployee++;
                            break;
                        case 3:
                            empl.country = element.getTextContent();
                            totalEmployee++;
                            break;
                        case 4:
                            empl.age = Integer.parseInt(element.getTextContent());
                            employeeList.add(empl);
                            totalEmployee = 0;
                            break;
                    }
                }
                listEmployee(node_, employeeList, totalEmployee);
            }
        }
        return employeeList;
    }
}