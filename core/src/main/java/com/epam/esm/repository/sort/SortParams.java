package com.epam.esm.repository.sort;

import lombok.Getter;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Getter
public class SortParams {
    private final Map<String, Object> sortParamsMap = new HashMap<String,Object>();

    public SortParams(List<String> sortingColumnNames, List<String> sortingTypes){
        for (int i = 0; i < sortingColumnNames.size() && i < sortingTypes.size(); i++){
            sortParamsMap.put(sortingColumnNames.get(i),sortingTypes.get(i));
        }
    }

    public SortParams(List<String> sortingColumnNames, String sortingType){
        for (int i = 0; i < sortingColumnNames.size(); i++){
            sortParamsMap.put(sortingColumnNames.get(i),sortingType);
        }
    }

//    public List<String> makeDBQueryForm(List<String> params){
//        List<String> result = new ArrayList<String>();
//        String regEx = "[A-Z]";
//        Pattern pattern = Pattern.compile(regEx);
//        for (String param : params){
//            Matcher matcher = pattern.matcher(param);
//            while(matcher.find()){
//                String matchLetter = matcher.group();
//                param = param.replace(matchLetter, "_" + matchLetter.toLowerCase());
//            }
//            System.out.println(param);
//            result.add(param);
//        }
//        return result;
//    }
}
